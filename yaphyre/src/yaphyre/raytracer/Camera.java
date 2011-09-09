/**
 *
 */
package yaphyre.raytracer;

import java.awt.image.BufferedImage;

import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector;
import yaphyre.util.Color;

public class Camera {

  public static final int RED = 0;

  public static final int GREEN = 1;

  public static final int BLUE = 2;

  public static final int ALPHA = 3;

  public Vector position;

  public Vector direction;

  public int width;

  public int height;

  public double minX;

  public double maxX;

  public double stepX;

  public double minY;

  public double maxY;

  public double stepY;

  public short[] depthChannel;

  public double[][] colorChannel;

  public void setColor(int x, int y, Color color) {
    int pixelIndex = y * width + x;
    colorChannel[pixelIndex][RED] = color.getRed();
    colorChannel[pixelIndex][GREEN] = color.getGreen();
    colorChannel[pixelIndex][BLUE] = color.getBlue();
    colorChannel[pixelIndex][ALPHA] = 1d;
  }

  public void setDepth(int x, int y, double depth) {
    int pixelIndex = y * width + x;
    depthChannel[pixelIndex] = (short)depth;
  }

  public Ray createEyeRay(int x, int y) {
    double yCoordinate = this.minY + y * this.stepY;
    double xCoordinate = this.minX + x * this.stepX;
    return new Ray(new Vector(xCoordinate, yCoordinate, this.position.getZ()), this.direction);
  }

  public BufferedImage createDepthImage() {
    BufferedImage result = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);

    int pixelIndex = 0;
    for (int y = this.height - 1; y >= 0; y--) {
      for (int x = 0; x < this.width; x++) {
        int depth = depthChannel[pixelIndex];
        int opaque = (depth < 255) ? 255 : 0;
        int argb = ((opaque << 24) | (depth << 16) | (depth << 8) | depth);
        result.setRGB(x, y, argb);
        pixelIndex++;
      }
    }

    return result;
  }

  public BufferedImage createColorImage() {
    BufferedImage result = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);

    int pixelIndex = 0;
    for (int y = this.height - 1; y >= 0; y--) {
      for (int x = 0; x < this.width; x++) {
        int red = Color.toByteValue(colorChannel[pixelIndex][RED]) & 0xff;
        int green = Color.toByteValue(colorChannel[pixelIndex][GREEN]) & 0xff;
        int blue = Color.toByteValue(colorChannel[pixelIndex][BLUE]) & 0xff;
        int alpha = Color.toByteValue(colorChannel[pixelIndex][ALPHA]) & 0xff;
        int argb = ((alpha << 24) | (red << 16) | (green << 8) | blue);
        result.setRGB(x, y, argb);
        pixelIndex++;
      }
    }

    return result;
  }
}