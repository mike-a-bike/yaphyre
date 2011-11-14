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

import static yaphyre.math.MathUtils.EPSILON;

import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yaphyre.cameras.OrthographicCamera;
import yaphyre.cameras.AbstractCamera.BaseCameraSettings;
import yaphyre.cameras.OrthographicCamera.OrthographicCameraSettings;
import yaphyre.core.CollisionInformation;
import yaphyre.core.Lightsource;
import yaphyre.core.Sampler;
import yaphyre.core.Shape;
import yaphyre.films.ImageFile;
import yaphyre.films.ImageFile.FileType;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector3D;
import yaphyre.samplers.SinglePointSampler;
import yaphyre.util.Color;
import yaphyre.util.RenderStatistics;

/**
 * This is it. This is the raytracer which actually creates an image from all
 * the scene garbage.<br/>
 * TODO:
 * <ul>
 * <li>Implement the correct camera handling (rotation matrix, look at, ...)</li>
 * </ul>
 * 
 * @author Michael Bieri
 */
public class RayTracer {

  private static final Logger LOGGER = LoggerFactory.getLogger(RayTracer.class);

  private static final int MAX_ITERATIONS = 10;

  private Scene scene;

  private Camera camera;

  private Sampler sampler;

  public void setScene(Scene scene) {
    this.scene = scene;
  }

  public Scene getScene() {
    return this.scene;
  }

  public void setSampler(Sampler sampler) {
    this.sampler = sampler;
  }

  public BufferedImage render(int imageWidth, int imageHeight, double frameWidth, double frameHeight, Point3D cameraPosition, Vector3D cameraDirection) {

    ImageFile imageFile = new ImageFile(imageWidth, imageHeight, FileType.PNG);
    BaseCameraSettings<ImageFile> baseSettings = BaseCameraSettings.create(cameraPosition, cameraPosition.add(cameraDirection), imageFile);
    OrthographicCameraSettings orthoSettings = OrthographicCameraSettings.create(frameWidth, frameHeight);
    yaphyre.core.Camera<ImageFile> camera = new OrthographicCamera<ImageFile>(baseSettings, orthoSettings);

    LOGGER.debug("Camera initialized: ".concat(camera.toString()));

    this.camera = setupCamera(imageWidth, imageHeight, frameWidth, frameHeight, cameraPosition, cameraDirection);

    long renderStart = System.nanoTime();
    LOGGER.info("Start rendering");
    LOGGER.info("{}", this.scene);

    if (this.sampler == null) {
      LOGGER.warn("No sampler set -> initializing default sampler: {}", SinglePointSampler.class.getSimpleName());
      this.sampler = new SinglePointSampler();
    }

    for (int y = 0; y < imageHeight; y++) {
      for (int x = 0; x < imageWidth; x++) {
        Point2D basePoint = new Point2D(x, y);

        Color color = Color.BLACK;
        int sampleCount = 0;
        for (Point2D samplePoint : this.sampler.getUnitSquareSamples()) {
          sampleCount++;
          Ray eyeRay = this.camera.createEyeRay(basePoint.add(samplePoint));
          RenderStatistics.incEyeRays();
          color = color.add(traceRay(eyeRay, 1));
        }
        color = color.multiply(1d / sampleCount);

        this.camera.setColor(x, y, color.clip());
      }
    }

    printRenderStatistics(renderStart);

    return this.camera.createColorImage();

  }

  private void printRenderStatistics(long renderStart) {
    LOGGER.info("Rendering finished in {}ms", (System.nanoTime() - renderStart) / 1000 / 1000);
    LOGGER.info("{} eye rays calculated", RenderStatistics.getEyeRays());
    LOGGER.info("{} secondary rays calculated", RenderStatistics.getSecondaryRays());
    LOGGER.info("{} shadow rays calculated", RenderStatistics.getShadowRays());
    LOGGER.info("{} rays where cancelled", RenderStatistics.getCancelledRays());
  }

  protected Color traceRay(Ray ray, int iteration) {

    if (iteration > MAX_ITERATIONS) {
      RenderStatistics.incCancelledRays();
      return Color.BLACK;
    }
    iteration++;

    CollisionInformation shapeCollisionInfo = this.scene.getCollidingShape(ray, Shape.NO_INTERSECTION, false);

    if (shapeCollisionInfo != null) {
      Point2D uvCoordinates = shapeCollisionInfo.getCollisionShape().getMappedSurfacePoint(shapeCollisionInfo.getCollisionPoint());
      Color objectColor = shapeCollisionInfo.getCollisionShape().getShader().getColor(uvCoordinates);
      Color ambientColor = objectColor.multiply(shapeCollisionInfo.getCollisionShape().getShader().getMaterial(uvCoordinates).getAmbient());
      Color lightColor = calculateLightColor(shapeCollisionInfo, uvCoordinates, objectColor);
      Color reflectedColor = calculateReflectedColor(ray, iteration, shapeCollisionInfo, uvCoordinates);
      Color refractedColor = Color.BLACK;

      return ambientColor.add(lightColor).add(reflectedColor).add(refractedColor);
    }

    return Color.BLACK;
  }

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

  private Color calculateReflectedColor(Ray ray, int iteration, CollisionInformation shapeCollisionInfo, Point2D uvCoordinates) {
    Color reflectedColor = Color.BLACK;
    double reflectionValue = shapeCollisionInfo.getCollisionShape().getShader().getMaterial(uvCoordinates).getReflection();
    if (reflectionValue > 0d) {
      // reflected = eye - 2 * (eye . normal) * normal
      Normal3D normal = shapeCollisionInfo.getCollisionShape().getNormal(shapeCollisionInfo.getCollisionPoint());
      Vector3D reflectedRayDirection = ray.getDirection().sub(normal.scale(2d * ray.getDirection().dot(normal))).normalize();
      Point3D reflectedRayStartPoint = shapeCollisionInfo.getCollisionPoint().add(reflectedRayDirection.scale(EPSILON));
      Ray reflectedRay = new Ray(reflectedRayStartPoint, reflectedRayDirection);
      RenderStatistics.incSecondaryRays();
      reflectedColor = traceRay(reflectedRay, iteration).multiply(reflectionValue);
    }
    return reflectedColor;
  }

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

}
