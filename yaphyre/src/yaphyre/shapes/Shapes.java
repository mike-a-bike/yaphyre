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

import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.raytracer.CollisionInformations;
import yaphyre.shaders.Shaders;

/**
 * Interface implemented by all {@link Shapes} of the rendering system.
 * 
 * @version $Revision$
 * 
 * @author Michael Bieri
 * @author $LastChangedBy$
 */
public interface Shapes {

  /** Constant for signaling that there is no intersection. */
  public static final double NO_INTERSECTION = Double.POSITIVE_INFINITY;

  /**
   * Gets the {@link Shaders} instance associated with this shape instance.
   * 
   * @return The {@link Shaders} for this shape.
   */
  public Shaders getShader();

  /**
   * Simple intersection test. This does not produce exact intersection
   * informations, but just informations about whether the given {@link Ray}
   * hits this {@link Shapes} or not.
   * 
   * @param ray
   *          The {@link Ray} to check for intersection.
   * 
   * @return <code>true</code> if the {@link Ray} hits this shape,
   *         <code>false</code> otherwise.
   */
  public boolean isHitBy(Ray ray);

  /**
   * Create the intersection informations for the given {@link Ray} and this
   * shape. If the {@link Ray} does not intersect this shape at all, then
   * <code>null</code> is returned. Otherwise the collision informations are
   * returned. The results created by this method are as accurate as possible.
   * 
   * @param ray
   *          The {@link Ray} to check for intersection.
   * 
   * @return The {@link CollisionInformations} instance describing the
   *         intersection between the {@link Ray} and this {@link Shapes}
   *         instance. <code>null</code> if no intersection happens.
   */
  public CollisionInformations intersect(Ray ray);

  /**
   * Calculate the distance in which the given {@link Ray} and this shape
   * intersect. If no intersection occurs, {@link #NO_INTERSECTION} is returned.
   * 
   * @param ray
   *          The {@link Ray} to calculate the intersection distance for.
   * 
   * @return The distance in which the ray intersects this shape.
   *         {@link #NO_INTERSECTION} if there is no intersection.
   */
  public double getIntersectDistance(Ray ray);

  /**
   * Returns the {@link Point3D} at which the given {@link Ray} intersects this
   * {@link Shapes} instance. If the ray misses this shape, <code>null</code> is
   * returned.
   * 
   * @param ray
   *          The {@link Ray} to calculate the intersection for.
   * 
   * @return The {@link Point3D} where the given {@link Ray} meets this shape.
   *         <code>null</code> if the ray misses the shape.
   */
  public Point3D getIntersectionPoint(Ray ray);

  /**
   * Gets the {@link Normal3D} information at a given point on the surface of
   * this shape.
   * 
   * @param surfacePoint
   *          The {@link Point3D} on the surface of this shape.
   * 
   * @return The {@link Normal3D} information at the location of the
   *         <code>surfacePoint</code>.
   */
  public Normal3D getNormal(Point3D surfacePoint);

  /**
   * Gets the mapping information of the given surface point. Each 3D object has
   * a 2D surface with each point described by its u- and v- coordinates. The
   * method calculates these coordinates for the given point on the surface.
   * 
   * @param surfacePoint
   *          The point on the surface of this shape.
   * 
   * @return A {@link Point2D} instance with the u- and v- coordinates.
   */
  public Point2D getMappedSurfacePoint(Point3D surfacePoint);

  /**
   * Flag which determines whether this shape actually throws a shadow or not.
   * This is only used when lights are places within a shape. If the surrounding
   * shape throws shadows, then no light can get to the outside of it.
   * 
   * @return <code>true</code> if the shape throws shadows, <code>false</code>
   *         otherwise. Default is <code>true</code>.
   */
  public boolean throwsShadow();

}
