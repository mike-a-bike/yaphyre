package yaphyre.raytracer;

import java.awt.image.BufferedImage;
import java.text.MessageFormat;

import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector;
import yaphyre.lights.Lightsources;
import yaphyre.shapes.Shapes;
import yaphyre.util.Color;
import yaphyre.util.RenderStatistics;

/**
 * This is it. This is the raytracer which actually creates an image from all
 * the scene garbage.<br/>
 * TODO:
 * <ul>
 * <li>Implement the correct camera handling (rotation matrix, look at, ...)</li>
 * <li>Check-in the source to google-code (YaPhyRe)</li>
 * </ul>
 * 
 * @author Michael Bieri
 */
public class RayTracer {

  private static final int MAX_ITERATIONS = 10;

  private Scene scene;

  private Camera camera;

  private static final double EPSILON = 1e-5;

  public void setScene(Scene scene) {
    this.scene = scene;
  }

  public Scene getScene() {
    return scene;
  }

  public BufferedImage render(int imageWidth, int imageHeight, double frameWidth, double frameHeight, Vector cameraPosition, Vector cameraDirection) {

    this.camera = this.setupCamera(imageWidth, imageHeight, frameWidth, frameHeight, cameraPosition, cameraDirection);

    long renderStart = System.nanoTime();
    System.out.println("Start rendering");
    System.out.println(this.scene);

    for (int y = 0; y < imageHeight; y++) {
      for (int x = 0; x < imageWidth; x++) {

        Ray eyeRay = this.camera.createEyeRay(x, y);
        RenderStatistics.incEyeRays();

        Color color = this.traceRay(eyeRay, 1);

        this.camera.setColor(x, y, color.clip());
      }
    }

    printRenderStatistics(renderStart);

    return this.camera.createColorImage();

  }

  private void printRenderStatistics(long renderStart) {
    System.out.println(MessageFormat.format("Rendering finished in {0,number}ms", (System.nanoTime() - renderStart) / 1000 / 1000));
    System.out.println(MessageFormat.format("{0} eye rays calculated", RenderStatistics.getEyeRays()));
    System.out.println(MessageFormat.format("{0} secondary rays calculated", RenderStatistics.getSecondaryRays()));
    System.out.println(MessageFormat.format("{0} shadow rays calculated", RenderStatistics.getShadowRays()));
    System.out.println(MessageFormat.format("{0} rays where cancelled", RenderStatistics.getCancelledRays()));
  }

  protected Color traceRay(Ray ray, int iteration) {

    if (iteration > MAX_ITERATIONS) {
      RenderStatistics.incCancelledRays();
      return Color.BLACK;
    }
    iteration++;

    CollisionInformations shapeCollisionInfo = this.scene.getCollidingShape(ray, Shapes.NO_INTERSECTION, false);

    if (shapeCollisionInfo != null) {
      Color objectColor = shapeCollisionInfo.getCollisionShape().getColor(shapeCollisionInfo.getCollisionPoint());

      Color ambientColor = objectColor.multiply(shapeCollisionInfo.getCollisionShape().getShader().getMaterial().getAmbient());

      Color lightColor = calculateLightColor(shapeCollisionInfo, objectColor);

      Color reflectedColor = calculateReflectedColor(ray, iteration, shapeCollisionInfo);

      Color refractedColor = Color.BLACK;

      return ambientColor.add(lightColor).add(reflectedColor).add(refractedColor);
    }

    return Color.BLACK;
  }

  private Color calculateLightColor(CollisionInformations shapeCollisionInfo, Color objectColor) {
    Color lightColor = Color.BLACK;
    for (Lightsources lightsource : this.scene.getLightsources()) {

      Vector lightVectorDirection = new Vector(shapeCollisionInfo.getCollisionPoint(), lightsource.getPosition()).unitVector();

      double lightIntensity = lightsource.getIntensity(shapeCollisionInfo.getCollisionPoint(), this.scene);

      if (lightIntensity > 0d) {
        Vector shapeNormal = shapeCollisionInfo.getCollisionShape().getNormal(shapeCollisionInfo.getCollisionPoint());
        double diffuse = shapeCollisionInfo.getCollisionShape().getShader().getMaterial().getDiffuse();
        diffuse *= Math.abs(lightVectorDirection.dot(shapeNormal));
        diffuse *= lightIntensity;
        lightColor = lightColor.add(objectColor.multiply(lightsource.getColor()).multiply(diffuse));
      }

    }
    return lightColor;
  }

  private Color calculateReflectedColor(Ray ray, int iteration, CollisionInformations shapeCollisionInfo) {
    Color reflectedColor = Color.BLACK;
    double reflectionValue = shapeCollisionInfo.getCollisionShape().getShader().getMaterial().getReflection();
    if (reflectionValue > 0d) {
      // reflected = eye - 2 * (eye . normal) * normal
      Vector normal = shapeCollisionInfo.getCollisionShape().getNormal(shapeCollisionInfo.getCollisionPoint());
      Vector reflectedRayDirection = ray.getDirection().sub(normal.scale(2d * ray.getDirection().dot(normal))).unitVector();
      Vector reflectedRayStartPoint = shapeCollisionInfo.getCollisionPoint().add(reflectedRayDirection.scale(EPSILON));
      Ray reflectedRay = new Ray(reflectedRayStartPoint, reflectedRayDirection);
      RenderStatistics.incSecondaryRays();
      reflectedColor = traceRay(reflectedRay, iteration).multiply(reflectionValue);
    }
    return reflectedColor;
  }

  private Camera setupCamera(int width, int height, double frameWidth, double frameHeight, Vector cameraPosition, Vector cameraDirection) {

    int imageArraySize = width * height;
    Camera camera = new Camera();
    camera.position = cameraPosition;
    camera.direction = cameraDirection.unitVector();
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
