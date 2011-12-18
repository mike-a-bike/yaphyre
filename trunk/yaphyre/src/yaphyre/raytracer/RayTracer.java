/*
 * Copyright 2011 Michael Bieri
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package yaphyre.raytracer;

import static yaphyre.geometry.MathUtils.EPSILON;

import java.awt.image.BufferedImage;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yaphyre.cameras.AbstractCamera.BaseCameraSettings;
import yaphyre.cameras.PerspectiveCamera;
import yaphyre.cameras.PerspectiveCamera.PerspectiveCameraSettings;
import yaphyre.core.CameraSample;
import yaphyre.core.CollisionInformation;
import yaphyre.core.Film;
import yaphyre.core.Lightsource;
import yaphyre.core.RenderWindow;
import yaphyre.core.Sampler;
import yaphyre.core.Shape;
import yaphyre.films.ImageFile;
import yaphyre.films.ImageFile.FileType;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Transformation;
import yaphyre.geometry.Vector3D;
import yaphyre.samplers.SinglePointSampler;
import yaphyre.util.Color;
import yaphyre.util.RenderStatistics;

import com.google.common.annotations.Beta;

/**
 * This is the class which exposes the rendering algorithm to the caller. It
 * takes some basic information like the scene description and the camera setup
 * to do its work. Since the raytracing algorithm is suited excellently for
 * parallelization, it slices up the work to use all the available CPUs within a
 * system. The parallelization can be prevented (for debugging purposes for
 * example) by calling the {@link #useSingleThreadedRenderer()} method to
 * enforce single threaded rendering.
 * 
 * @author Michael Bieri
 */
public class RayTracer {

  private static final Logger LOGGER = LoggerFactory.getLogger(RayTracer.class);

  /**
   * Tuning factor for parallelization: Number of image slices per cpu to
   * render.
   */
  private static final int SLICES_PER_CORE = 2;

  /**
   * Tuning factor for parallelization: How long to wait between checks if the
   * rendering is finished.
   */
  private static final int THREAD_POLL_TIMEOUT = 1000;

  /** Hard limit after which the rendering is canceled. */
  private static final int MAX_ITERATIONS = 10;

  private Scene scene;

  @Beta
  private Camera camera;

  private Sampler sampler;

  private boolean useSingleTreadedRendering = false;

  public void setScene(Scene scene) {
    this.scene = scene;
  }

  public Scene getScene() {
    return this.scene;
  }

  public void setSampler(Sampler sampler) {
    this.sampler = sampler;
  }

  /**
   * Call this method to prevent the usage of all available CPUs. This forces
   * the renderer to use no additional threads and perform all its work on the
   * main thread. Use this for debugging purposes only, since it slows the
   * rendering process down significantly.
   */
  public void useSingleThreadedRenderer() {
    this.useSingleTreadedRendering = true;
  }

  /**
   * Get the number of CPUs in the system.
   * 
   * @return The number of available CPUs.
   */
  private int getNumberOfCPUs() {
    return Runtime.getRuntime().availableProcessors();
  }

  /**
   * This method is the public entry point to start the rendering. It takes all
   * the needed arguments in order to setup and execute the rendering process.
   * Notice: This method does no rendering on its own, but calls the
   * {@link #renderWindow(yaphyre.core.Camera, Sampler, RenderWindow, Transformation)}
   * method which renders a part of the image designated by the given
   * {@link RenderWindow}.<br/>
   * This method is responsible for preparing the rendering and managing the
   * multi-threaded execution of the rendering process.
   * 
   * @param imageWidth
   *          The number of pixels the image is wide.
   * @param imageHeight
   *          The number of pixels the image is high.
   * @param frameWidth
   *          The virtual frame width (used for orthographic projection)
   * @param frameHeight
   *          The virtual frame height (used for orthographic projection)
   * @param cameraPosition
   *          The position of the camera in 3D space.
   * @param cameraDirection
   *          The direction in which the camera is oriented.
   * 
   * @return A {@link BufferedImage} which contains the rendered image. This can
   *         be saved or used for further transformation.
   */
  public BufferedImage render(int imageWidth, int imageHeight, double frameWidth, double frameHeight, Point3D cameraPosition, Vector3D cameraDirection) {

    ImageFile imageFile = new ImageFile(imageWidth, imageHeight, FileType.PNG);
    BaseCameraSettings<ImageFile> baseSettings = BaseCameraSettings.create(cameraPosition, cameraPosition.add(cameraDirection), imageFile);
    PerspectiveCameraSettings perspSetings = PerspectiveCameraSettings.create(((double) imageWidth) / ((double) imageHeight), 25d);
    yaphyre.core.Camera<ImageFile> camera = new PerspectiveCamera<ImageFile>(baseSettings, perspSetings);
    // OrthographicCameraSettings orthoSettings =
    // OrthographicCameraSettings.create(frameWidth, frameHeight);
    // yaphyre.core.Camera<ImageFile> camera = new
    // OrthographicCamera<ImageFile>(baseSettings, orthoSettings);

    LOGGER.debug("Camera initialized: ".concat(camera.toString()));

    this.camera = this.setupCamera(imageWidth, imageHeight, frameWidth, frameHeight, cameraPosition, cameraDirection);

    LOGGER.info("{}", this.scene);

    if (this.sampler == null) {
      LOGGER.warn("No sampler set -> initializing default sampler: {}", SinglePointSampler.class.getSimpleName());
      this.sampler = new SinglePointSampler();
    }

    // TODO move this transformation into the camera
    Transformation rasterToCamera = Transformation.rasterToUnitSquare(imageWidth, imageHeight);

    long duration = 0l;
    long cpuTime = 0l;

    if (this.useSingleTreadedRendering) {

      LOGGER.info("Using single threaded rendering");

      RenderWindow renderWindow = new RenderWindow(0, imageWidth, 0, imageHeight);

      duration = System.nanoTime();
      this.renderWindow(camera, this.sampler, renderWindow, rasterToCamera);
      duration = (System.nanoTime() - duration) / 1000l / 1000l;
      cpuTime = duration;

    } else {

      // Slicing up the work
      int numberOfCores = this.getNumberOfCPUs();
      int numberOfRenderingTasks = numberOfCores * SLICES_PER_CORE;
      int sliceWidth = (imageWidth + (numberOfRenderingTasks - 1)) / numberOfRenderingTasks;
      List<RenderCallable> slices = new ArrayList<RayTracer.RenderCallable>();

      LOGGER.info("Using mutli threaded renderer with {} cores", numberOfCores);
      LOGGER.info("Splitting rendering into {} slices", numberOfRenderingTasks);

      for (int i = 0; i < numberOfRenderingTasks; i++) {
        int sliceId = i + 1;
        int sliceStart = i * sliceWidth;
        int sliceEnd = Math.min(imageWidth, (i + 1) * sliceWidth);

        LOGGER.debug("Preparing slice {} [{}, {}]", new Object[] { sliceId, sliceStart, sliceEnd });

        RenderWindow window = new RenderWindow(sliceStart, sliceEnd, 0, imageHeight);
        slices.add(new RenderCallable(sliceId, camera, this.sampler, window, rasterToCamera));
      }

      ExecutorService renderingExecutor = Executors.newFixedThreadPool(numberOfCores);

      long renderStart = System.nanoTime();

      try {
        LOGGER.info("Start rendering");

        List<Future<Long>> renderResults = renderingExecutor.invokeAll(slices);
        boolean allDone;
        do {
          Thread.sleep(THREAD_POLL_TIMEOUT);
          allDone = true;
          for (Future<Long> result : renderResults) {
            allDone &= result.isDone();
          }
        } while (!allDone);

        duration = (System.nanoTime() - renderStart) / 1000 / 1000;
        for (Future<Long> result : renderResults) {
          cpuTime += result.get();
        }

        renderingExecutor.shutdown();

      } catch (Exception e) {
        LOGGER.error("Error while rendering", e);
      } finally {
        try {
          renderingExecutor.shutdownNow();
        } catch (Exception e) {
          LOGGER.error("Could not shutdown the rendering engines!", e);
        }
      }

    }

    this.printRenderStatistics(duration, cpuTime);

    return this.camera.createColorImage();

  }

  /**
   * This contains the actual render loop. Use this method for creating the
   * color informations. The results are recorded by the {@link Film} which is
   * in the camera (just like in real life...).
   * 
   * @param camera
   *          The {@link yaphyre.core.Camera} containing the {@link Film} which
   *          records all the information.
   * @param sampler
   *          The {@link Sampler} to use. This allows for sub pixel rendering
   *          used for a very simple anti aliasing.
   * @param window
   *          The {@link RenderWindow} describing the part of the image to
   *          render.
   * @param rasterToCamera
   *          The transformation from the raster space into the camera space.
   *          THis is here for optimization.
   */
  private void renderWindow(yaphyre.core.Camera<?> camera, Sampler sampler, RenderWindow window, Transformation rasterToCamera) {

    for (int v = window.getYMin(); v < window.getYMax(); v++) {
      for (int u = window.getXMin(); u < window.getXMax(); u++) {
        CameraSample sample = new CameraSample();

        Point2D rasterPoint = new Point2D(u, v);
        sample.setRasterPoint(rasterPoint);

        Color color = Color.BLACK;
        int sampleCount = 0;

        for (Point2D samplePoint : sampler.getUnitSquareSamples()) {
          sampleCount++;
          Point2D cameraCoordinates = rasterToCamera.transform(rasterPoint.add(samplePoint));
          Ray eyeRay = camera.getCameraRay(cameraCoordinates);
          RenderStatistics.incEyeRays();
          color = color.add(this.traceRay(eyeRay, 1));
        }
        color = color.multiply(1d / sampleCount);

        camera.getFilm().addCameraSample(sample, color);

        this.camera.setColor(u, v, color.clip());
      }
    }

  }

  /**
   * The core of the raytracing algorithm. This method traces a single ray
   * through the scene. If it hits an object secondary rays may be generated
   * which in turn are traced again by this method. It calls itself recursively
   * in order to calculate effects like reflection and refraction.
   * 
   * @param ray
   *          The {@link Ray} to follow trough the scene.
   * @param iteration
   *          The iteration depth. Prevents the algorithm from being stuck in an
   *          endless loop.
   * 
   * @return The {@link Color} the origin of the {@link Ray} "sees" in the
   *         scene.
   */
  protected Color traceRay(Ray ray, int iteration) {

    if (iteration > MAX_ITERATIONS) {
      RenderStatistics.incCancelledRays();
      return Color.BLACK;
    }

    CollisionInformation shapeCollisionInfo = this.scene.getCollidingShape(ray, Shape.NO_INTERSECTION, false);

    if (shapeCollisionInfo != null) {
      Point2D uvCoordinates = shapeCollisionInfo.getCollisionShape().getMappedSurfacePoint(shapeCollisionInfo.getCollisionPoint());
      Color objectColor = shapeCollisionInfo.getCollisionShape().getShader().getColor(uvCoordinates);
      Color ambientColor = (iteration == 1) ? objectColor.multiply(shapeCollisionInfo.getCollisionShape().getShader().getMaterial(uvCoordinates).getAmbient()) : Color.BLACK;
      Color lightColor = this.calculateLightColor(shapeCollisionInfo, uvCoordinates, objectColor);
      Color reflectedColor = this.calculateReflectedColor(ray, iteration + 1, shapeCollisionInfo, uvCoordinates);
      Color refractedColor = Color.BLACK;

      return ambientColor.add(lightColor).add(reflectedColor).add(refractedColor);
    }

    return Color.BLACK;
  }

  /**
   * For a given point, calculate the hue and brightness contributed by the
   * lights in the scene.
   * 
   * @param shapeCollisionInfo
   *          The {@link CollisionInformation} describing where the point lies
   *          and of which {@link Shape} it is a part of.
   * @param uvCoordinates
   *          The u/v coordinates on the shape.
   * @param objectColor
   *          The {@link Color} of the shape.
   * 
   * @return The Color of the light which depends on the distance and the angle
   *         under which the light is 'seen'.
   */
  private Color calculateLightColor(CollisionInformation shapeCollisionInfo, Point2D uvCoordinates, Color objectColor) {
    Color lightColor = Color.BLACK;
    for (Lightsource lightsource : this.scene.getLightsources()) {

      Vector3D lightVectorDirection = new Vector3D(shapeCollisionInfo.getCollisionPoint(), lightsource.getPosition()).normalize();

      double lightIntensity = lightsource.getIntensity(shapeCollisionInfo.getCollisionPoint(), this.scene);

      if (lightIntensity > 0d) {
        Normal3D shapeNormal = shapeCollisionInfo.getCollisionShape().getNormal(shapeCollisionInfo.getCollisionPoint());
        double diffuse = shapeCollisionInfo.getCollisionShape().getShader().getMaterial(uvCoordinates).getDiffuse();
        diffuse *= Math.abs(lightVectorDirection.dot(shapeNormal));
        diffuse *= lightIntensity;
        lightColor = lightColor.add(objectColor.multiply(lightsource.getColor()).multiply(diffuse));
      }

    }
    return lightColor;
  }

  /**
   * If the shape has any reflective attributes (not diffuse reflection), this
   * method handles the contribution of that. It creates a reflected ray based
   * on the incoming ray and the normal of the surface at the collision point.
   * The origin of that ray is the point of collision (with a small correction
   * to prevent self-shadowing). It the calls {@link #traceRay(Ray, int)} to
   * determine the {@link Color} information at that point.
   * 
   * @param ray
   *          The incoming ray.
   * @param iteration
   *          The iteration (used to cancel the recursive nature of the
   *          algorithm).
   * @param shapeCollisionInfo
   *          The {@link Shape} for which the reflection is calculated.
   * @param uvCoordinates
   *          The u/v coordinates on the {@link Shape}.
   * 
   * @return The {@link Color} for what the reflected ray 'sees' in the scene.
   */
  private Color calculateReflectedColor(Ray ray, int iteration, CollisionInformation shapeCollisionInfo, Point2D uvCoordinates) {
    Color reflectedColor = Color.BLACK;
    double reflectionValue = shapeCollisionInfo.getCollisionShape().getShader().getMaterial(uvCoordinates).getReflection();
    if (reflectionValue > 0) {
      // reflected = eye - 2 * (eye . normal) * normal
      Normal3D normal = shapeCollisionInfo.getCollisionShape().getNormal(shapeCollisionInfo.getCollisionPoint());
      Vector3D reflectedRayDirection = ray.getDirection().sub(normal.scale(2d * ray.getDirection().dot(normal))).normalize();
      Point3D reflectedRayStartPoint = shapeCollisionInfo.getCollisionPoint().add(reflectedRayDirection.scale(EPSILON));
      Ray reflectedRay = new Ray(reflectedRayStartPoint, reflectedRayDirection);
      RenderStatistics.incSecondaryRays();
      reflectedColor = this.traceRay(reflectedRay, iteration).multiply(reflectionValue);
    }
    return reflectedColor;
  }

  /**
   * Print some statistics, like number of rays, rendering time, total CPU time
   * and some more. The two parameter are used to calculate the factor by which
   * the rendering was speed up by using multiple CPUs.
   * 
   * @param duration
   *          The duration of the rendering (the time the caller had to wait)
   * @param cpuTime
   *          The total time used by all involved CPUs. This is at least the
   *          same as the value of <code>duration</code> but when using more
   *          than one CPU this value may be higher.
   */
  private void printRenderStatistics(long duration, long cpuTime) {
    LOGGER.info("Rendering finished in: {}ms", duration);
    if (LOGGER.isDebugEnabled()) {
      if (this.useSingleTreadedRendering) {
        LOGGER.debug("Single threaded rendering");
      } else {
        LOGGER.debug("Number of available CPUS: {}", this.getNumberOfCPUs());
        LOGGER.debug("Effective CPU time: {}ms", cpuTime);
        LOGGER.debug(MessageFormat.format("Parallelization gain: {0,number,0.000}", ((double) cpuTime) / ((double) duration)));
      }
    }
    LOGGER.info("{} eye rays calculated", RenderStatistics.getEyeRays());
    LOGGER.info("{} secondary rays calculated", RenderStatistics.getSecondaryRays());
    LOGGER.info("{} shadow rays calculated", RenderStatistics.getShadowRays());
    LOGGER.info("{} rays where cancelled", RenderStatistics.getCancelledRays());
  }

  /**
   * This is only temporary. Setup a {@link Camera}. This will be needed no
   * longer as soon as the {@link yaphyre.core.Camera} implementation is
   * finished.
   */
  @Beta
  private Camera setupCamera(int width, int height, double frameWidth, double frameHeight, Point3D cameraPosition, Vector3D cameraDirection) {

    int imageArraySize = width * height;
    Camera camera = new Camera();
    camera.position = cameraPosition;
    camera.direction = cameraDirection.normalize();
    camera.width = width;
    camera.height = height;
    camera.minX = cameraPosition.getX() - frameWidth / 2;
    camera.maxX = camera.minX + frameWidth;
    camera.minY = cameraPosition.getY() - frameHeight / 2;
    camera.maxY = camera.minY + frameHeight;
    camera.stepX = frameWidth / (width);
    camera.stepY = frameHeight / (height);
    camera.depthChannel = new short[imageArraySize];
    camera.colorChannel = new double[imageArraySize][4];

    return camera;
  }

  /**
   * Implementation of the {@link Callable} interface in order to parallelize
   * the rendering process.
   * 
   * @author Michael Bieri
   */
  private class RenderCallable implements Callable<Long> {

    private final int sliceId;

    private final yaphyre.core.Camera<?> camera;

    private final Sampler sampler;

    private final RenderWindow window;

    private final Transformation rasterToCamera;

    public RenderCallable(int sliceId, yaphyre.core.Camera<?> camera, Sampler sampler, RenderWindow window, Transformation rasterToCamera) {
      this.sliceId = sliceId;
      this.camera = camera;
      this.sampler = sampler;
      this.window = window;
      this.rasterToCamera = rasterToCamera;
    }

    /**
     * Implements the {@link Callable#call()} method. The only thing this method
     * does is calling the
     * {@link RayTracer#renderWindow(yaphyre.core.Camera, Sampler, RenderWindow, Transformation)}
     * method in order to render the content of its allocated raster area.
     * 
     * @return The duration in milliseconds
     */
    @Override
    public Long call() throws Exception {
      LOGGER.debug("Starting slice {}", this.sliceId);
      long startTime = System.nanoTime();
      RayTracer.this.renderWindow(this.camera, this.sampler, this.window, this.rasterToCamera);
      long duration = (System.nanoTime() - startTime) / 1000 / 1000;
      LOGGER.debug("Slice {} done in {}ms", new Object[] { this.sliceId, duration });
      return duration;
    }

  }

}
