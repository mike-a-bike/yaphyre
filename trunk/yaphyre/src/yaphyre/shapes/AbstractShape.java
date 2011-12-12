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

import yaphyre.core.CollisionInformation;
import yaphyre.core.Shader;
import yaphyre.core.Shape;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;

/**
 * Implementation of common methods for most {@link Shape}.
 *
 * @version $Revision$
 *
 * @author Michael Bieri
 * @author $LastChangedBy$
 */
public abstract class AbstractShape implements Shape {

  private static final long serialVersionUID = 6078311087267053881L;

  private final Shader shader;

  private final boolean throwsShadow;

  protected AbstractShape(Shader shader, boolean throwsShadow) {
    this.shader = shader;
    this.throwsShadow = throwsShadow;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((shader == null) ? 0 : shader.hashCode());
    result = prime * result + (throwsShadow ? 1231 : 1237);
    return result;
  }




  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AbstractShape other = (AbstractShape)obj;
    if (shader == null) {
      if (other.shader != null)
        return false;
    } else if (!shader.equals(other.shader))
      return false;
    if (throwsShadow != other.throwsShadow)
      return false;
    return true;
  }




  @Override
  public Shader getShader() {
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
  public CollisionInformation intersect(Ray ray) {
    throw new RuntimeException("Not implemented yet");
  }

  @Override
  public Point3D getIntersectionPoint(Ray ray) {
    double intersectionDistance = getIntersectDistance(ray);
    if (intersectionDistance == Shape.NO_INTERSECTION) {
      return null;
    }

    return ray.getPoint(intersectionDistance);
  }

}
