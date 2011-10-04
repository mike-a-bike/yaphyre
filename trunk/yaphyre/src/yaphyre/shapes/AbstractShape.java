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
package yaphyre.shapes;

import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.shaders.Shaders;
import yaphyre.util.IdentifiableObject;

/**
 * Implementation of common methods for most {@link Shapes}.
 * 
 * @version $Revision$
 * 
 * @author Michael Bieri
 * @author $LastChangedBy$
 */
public abstract class AbstractShape extends IdentifiableObject implements Shapes {

  private final Shaders shader;

  private final boolean throwsShadow;

  protected AbstractShape(String id, Shaders shader, boolean throwsShadow) {
    super(id);
    this.shader = shader;
    this.throwsShadow = throwsShadow;
  }

  @Override
  public Shaders getShader() {
    return this.shader;
  }

  @Override
  public boolean throwsShadow() {
    return this.throwsShadow;
  }

  @Override
  public Point3D getIntersectionPoint(Ray ray) {
    double intersectionDistance = getIntersectDistance(ray);
    if (intersectionDistance == Shapes.NO_INTERSECTION) {
      return null;
    }

    return ray.getPoint(intersectionDistance);
  }

}