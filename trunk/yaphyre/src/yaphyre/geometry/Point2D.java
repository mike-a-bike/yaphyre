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
package yaphyre.geometry;

import java.text.MessageFormat;

/**
 * Abstraction of a point in a 2 dimensional space. This uses u and v as
 * coordinates since its major usage will be the mapping of shader and texture
 * informations.
 * 
 * @TODO implement the camera, so that it uses the 2d coordinates in order to
 *       create the seeing rays.
 * 
 * @version $Revision: 47 $
 * 
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
public class Point2D {

  protected final double u, v;

  public Point2D(double u, double v) {
    this.u = u;
    this.v = v;
  }

  @Override
  public String toString() {
    return MessageFormat.format("<{0,number,0.000}, {1,number,0.000}>", this.u, this.v);
  }

}
