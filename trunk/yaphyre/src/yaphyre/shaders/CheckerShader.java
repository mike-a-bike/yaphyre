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
package yaphyre.shaders;

import yaphyre.geometry.Point2D;
import yaphyre.util.Color;

/**
 * A simple checker pattern. No ray tracer is complete without one ;-) This
 * implementation uses two shader to create the color of the pixel to show. So
 * it is for example possible to create a checked pattern within the checker
 * pattern itself.
 * 
 * @version $Revision: 40 $
 * 
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
public class CheckerShader implements Shaders {

  private final Shaders shader1, shader2;

  private final double uSize, vSize, uSizeInv, vSizeInv;

  /**
   * Creates a simple checker with the given two shader. The frequency of the
   * change is 1.
   * 
   * @param id
   *          The id of the shader
   * @param shader1
   *          The first shader
   * @param shader2
   *          The second shader
   */
  public CheckerShader(String id, Shaders shader1, Shaders shader2) {
    this(shader1, shader2, 1d, 1d);
  }

  /**
   * Create a new checker shader defined by its id, the two shader used for its
   * tiles and the frequency with which the pattern changes.
   * 
   * @param id
   *          The id of the shader
   * @param shader1
   *          The first shader
   * @param shader2
   *          The second shader
   * @param frequency
   *          The frequency with which the pattern changes.
   */
  public CheckerShader(String id, Shaders shader1, Shaders shader2, double frequency) {
    this(shader1, shader2, frequency, frequency);
  }

  /**
   * Create a new checker shader defined by its id, the two shader used for its
   * tiles and the frequency with which the pattern changes.
   * 
   * @param id
   *          The id of the shader
   * @param shader1
   *          The first shader
   * @param shader2
   *          The second shader
   * @param uFrequency
   *          The frequency with which the pattern changes in the u direction.
   * @param vFrequency
   *          The frequency with which the pattern changes in the v direction.
   */
  public CheckerShader(Shaders shader1, Shaders shader2, double uFrequency, double vFrequency) {
    this.shader1 = shader1;
    this.shader2 = shader2;
    this.uSizeInv = uFrequency;
    this.uSize = 1d / uFrequency;
    this.vSizeInv = vFrequency;
    this.vSize = 1d / vFrequency;
  }

  @Override
  public Color getColor(Point2D uvCoordinate) {
    return getShaderAtCoordinate(uvCoordinate).getColor(uvCoordinate);
  }

  @Override
  public Material getMaterial(Point2D uvCoordinate) {
    return getShaderAtCoordinate(uvCoordinate).getMaterial(uvCoordinate);
  }

  private Shaders getShaderAtCoordinate(Point2D uvCoordinate) {
    double u = uvCoordinate.getU();
    double v = uvCoordinate.getV();
    u = (u >= 0) ? u : u - this.uSize;
    v = (v > +0) ? v : v - this.vSize;
    boolean xEven = ((int)(u * this.uSizeInv)) % 2 == 0;
    boolean yEven = ((int)(v * this.vSizeInv)) % 2 == 0;

    if (xEven == yEven) {
      return this.shader1;
    }
    return this.shader2;
  }

}
