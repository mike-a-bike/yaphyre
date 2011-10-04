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
import yaphyre.util.IdentifiableObject;

/**
 * A simple checker pattern. No ray tracer is complete without one ;-)
 * 
 * @version $Revision: 40 $
 * 
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
public class CheckerShader extends IdentifiableObject implements Shaders {

  private final Shaders shader1, shader2;

  private final double uSize, vSize, uSizeInv, vSizeInv;

  public CheckerShader(String id, Shaders shader1, Shaders shader2, double uSize, double vSize) {
    super(id);
    this.shader1 = shader1;
    this.shader2 = shader2;
    this.uSize = uSize;
    this.uSizeInv = 1d / this.uSize;
    this.vSize = vSize;
    this.vSizeInv = 1d / this.vSize;
  }

  @Override
  public Color getColor(Point2D uvCoordinate) {
    return this.getShaderAtCoordinate(uvCoordinate).getColor(uvCoordinate);
  }

  @Override
  public Material getMaterial(Point2D uvCoordinate) {
    return this.getShaderAtCoordinate(uvCoordinate).getMaterial(uvCoordinate);
  }

  private Shaders getShaderAtCoordinate(Point2D uvCoordinate) {
    double u = uvCoordinate.getU();
    double v = uvCoordinate.getV();
    u = (u >= 0) ? u : u - this.uSize;
    v = (v > +0) ? v : v - this.vSize;
    boolean xEven = ((int) (u * this.uSizeInv)) % 2 == 0;
    boolean yEven = ((int) (v * this.vSizeInv)) % 2 == 0;

    if (xEven == yEven) {
      return this.shader1;
    }
    return this.shader2;
  }

}
