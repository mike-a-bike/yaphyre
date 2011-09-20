package yaphyre;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yaphyre.geometry.Vector;
import yaphyre.raytracer.RayTracer;
import yaphyre.raytracer.Scene;

public class VerySimpleRaytracer {

  private static final Logger LOGGER = LoggerFactory.getLogger(VerySimpleRaytracer.class);

  public static void main(String... args) throws IOException {

    LOGGER.info("Starting renderer");

    LOGGER.debug("Setup camera");
    Vector cameraPosition = new Vector(0, 25, -100);
    Vector lookAt = new Vector(0, 1, 0);
    Vector cameraDirection = new Vector(cameraPosition, lookAt);

    double frameWidth = 12d;
    double frameHeight = 9d;

    // int imageWidth = 800;
    // int imageHeight = 600;

    int imageWidth = 1024;
    int imageHeight = 768;

    // int imageWidth = 1280;
    // int imageHeight = 960;

    // int imageWidth = 1920;
    // int imageHeight = 1440;

    LOGGER.debug("Setup scene");
    Scene scene = SceneReader.createSimpleScene();

    RayTracer rayTracer = new RayTracer();
    rayTracer.setScene(scene);

    LOGGER.debug("Render image");
    BufferedImage renderedImage = rayTracer.render(imageWidth, imageHeight, frameWidth, frameHeight, cameraPosition, cameraDirection);

    LOGGER.debug("Write image file");
    File imageFile = new File("color.png");
    FileOutputStream imageFileStream = new FileOutputStream(imageFile);
    ImageIO.write(renderedImage, "PNG", imageFileStream);
    imageFileStream.close();

    LOGGER.info("Image written to: " + imageFile.getAbsolutePath());

    LOGGER.info("Renderer finished");

  }

}
