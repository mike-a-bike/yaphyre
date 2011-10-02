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

import java.awt.image.BufferedImage;

import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector3D;
import yaphyre.util.Color;

/**
 * see {@link http://www.cubic.org/docs/camera.htm} for some pretty cool
 * documentation about cameras
 * 
 * @version $Revision$
 * 
 * @author Michael Bieri
 * @author $LastChangedBy$
 */
public class Camera {

  public static final int RED = 0;

  public static final int GREEN = 1;

  public static final int BLUE = 2;

  public static final int ALPHA = 3;

  public Point3D position;

  public Vector3D direction;

  public int width;

  public int height;

  public double minX;

  public double maxX;

  public double stepX;
  
  private double stepX_half;

  public double minY;

  public double maxY;

  public double stepY;
  
  private double stepY_half;
  
  public int oversampling;

  public short[] depthChannel;

  public double[][] colorChannel;

  public void setColor(int x, int y, Color color) {
    int pixelIndex = y * this.width + x;
    this.colorChannel[pixelIndex][RED] = color.getRed();
    this.colorChannel[pixelIndex][GREEN] = color.getGreen();
    this.colorChannel[pixelIndex][BLUE] = color.getBlue();
    this.colorChannel[pixelIndex][ALPHA] = 1d;
  }

  public void setDepth(int x, int y, double depth) {
    int pixelIndex = y * this.width + x;
    this.depthChannel[pixelIndex] = (short)depth;
  }

  public Ray createEyeRay(int x, int y) {
    double yCoordinate = this.minY + y * this.stepY + this.stepX_half;
    double xCoordinate = this.minX + x * this.stepX + this.stepY_half;
    return new Ray(new Point3D(xCoordinate, yCoordinate, this.position.getZ()), this.direction);
  }

  public BufferedImage createDepthImage() {
    BufferedImage result = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);

    int pixelIndex = 0;
    for (int y = this.height - 1; y >= 0; y--) {
      for (int x = 0; x < this.width; x++) {
        int depth = this.depthChannel[pixelIndex];
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
        int red = Color.toByteValue(this.colorChannel[pixelIndex][RED]) & 0xff;
        int green = Color.toByteValue(this.colorChannel[pixelIndex][GREEN]) & 0xff;
        int blue = Color.toByteValue(this.colorChannel[pixelIndex][BLUE]) & 0xff;
        int alpha = Color.toByteValue(this.colorChannel[pixelIndex][ALPHA]) & 0xff;
        int argb = ((alpha << 24) | (red << 16) | (green << 8) | blue);
        result.setRGB(x, y, argb);
        pixelIndex++;
      }
    }

    return result;
  }
}