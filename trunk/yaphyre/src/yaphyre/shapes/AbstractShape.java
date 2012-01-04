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

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import yaphyre.core.CollisionInformation;
import yaphyre.core.Shader;
import yaphyre.core.Shape;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Transformation;

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

  private final Transformation worldToObject;

  private final Transformation objectToWorld;

  /**
   * Initialize the common fields for all {@link Shape}s. Each {@link Shape}
   * defines a point of origin for its own, which is translated to the world
   * coordinate space using the given transformation. {@link Ray}s are
   * translated by the inverse of the {@link Transformation} to calculate an
   * eventual intersection.</br> Please remember, that the order of the
   * {@link Transformation} matters. It is not the same if the object is rotated
   * an then translated or first translated and then rotated.
   *
   * @param objectToWorld
   *          The {@link Transformation} used to map world coordinates to object
   *          coordinates.
   * @param shader
   *          The {@link Shader} instance to use when rendering this
   *          {@link Shape}.
   * @param throwsShadow
   *          Flag whether this {@link Shape} throws a shadow or not.
   *
   * @throws NullPointerException
   *           If either <code>objectToWorld</code> or <code>shader</code> is
   *           <code>null</code> a {@link NullPointerException} is thrown
   */
  protected AbstractShape(Transformation objectToWorld, Shader shader, boolean throwsShadow) throws NullPointerException {
    Preconditions.checkNotNull(objectToWorld);
    Preconditions.checkNotNull(shader);
    this.shader = shader;
    this.throwsShadow = throwsShadow;
    this.objectToWorld = objectToWorld;
    this.worldToObject = this.objectToWorld.inverse();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.getClass(), objectToWorld, shader, throwsShadow);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final AbstractShape other = (AbstractShape)obj;
    return Objects.equal(shader, other.shader) && Objects.equal(objectToWorld, other.objectToWorld) && Objects.equal(throwsShadow, other.throwsShadow);
  }

  @Override
  public Shader getShader() {
    return this.shader;
  }

  @Override
  public boolean throwsShadow() {
    return this.throwsShadow;
  }

  protected Transformation getWorldToObject() {
    return this.worldToObject;
  }

  protected Transformation getObjectToWorld() {
    return this.objectToWorld;
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

  protected Ray transformToObjectSpace(Ray ray) {
    return this.worldToObject.transform(ray);
  }

}
