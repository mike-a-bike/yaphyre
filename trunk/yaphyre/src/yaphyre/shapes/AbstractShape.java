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

import yaphyre.core.CollisionInformations;
import yaphyre.core.Shaders;
import yaphyre.core.Shapes;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;

/**
 * Implementation of common methods for most {@link Shapes}.
 * 
 * @version $Revision$
 * 
 * @author Michael Bieri
 * @author $LastChangedBy$
 */
public abstract class AbstractShape implements Shapes {

  private final Shaders shader;

  private final boolean throwsShadow;

  protected AbstractShape(Shaders shader, boolean throwsShadow) {
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
  public boolean isHitBy(Ray ray) {
    return (getIntersectDistance(ray) > 0d);
  }

  @Override
  public CollisionInformations intersect(Ray ray) {
    throw new RuntimeException("Not implemented yet");
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
