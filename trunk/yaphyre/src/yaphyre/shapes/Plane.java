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

import static java.lang.Math.signum;

import java.text.MessageFormat;

import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector3D;
import yaphyre.shaders.Shaders;

/**
 * Plane represented by a point on the plane and the normal. Since on a plane
 * the normal does not change, it does not matter where on the plane the origin
 * lies.<br/>
 * The mathematical representation of a point on the plane is:<br/>
 * (p - p<sub>0</sub>) &sdot; n = 0<br/>
 * with
 * <ul>
 * <li>p: the point on the plane</li>
 * <li>p<sub>0</sub>: the origin</li>
 * <li>n: the normal of the plane</li>
 * </ul>
 * 
 * @version $Revision$
 * 
 * @author Michael Bieri
 * @author $LastChangedBy$
 */
public class Plane extends AbstractShape {

  private final Point3D origin;

  private final Normal3D normal;

  public Plane(String id, Point3D origin, Normal3D normal, Shaders shader, boolean throwsShadow) {
    super(id, shader, throwsShadow);
    this.origin = origin;
    this.normal = normal;
  }

  @Override
  public String toString() {
    return MessageFormat.format("Plane[{0}, {1}, {2}]", getId(), this.origin, this.normal);
  }

  /**
   * Intersect the plane with a ray. We use the parametric form of the line
   * equation to determine the distance in which the line intersects this plane.<br/>
   * Using the two equations:<br/>
   * Plane: (p - p<sub>0</sub>) &sdot; n = 0<br/>
   * Line: p(t) = l<sub>0</sub> + (t * d)<br/>
   * we get:<br/>
   * t = ((p<sub>0</sub> - l<sub>0</sub>) &sdot; n) / (d &sdot; n)<br/>
   * If the line starts outside the plane and is parallel to it there is no
   * intersection, then the denominator is zero and the numerator is non-zero.<br/>
   * If the line starts on the plane and is parallel to it so every point on the
   * line intersects with the plane, the denominator and the numerator are zero<br/>
   * If the result is negative, the line intersects with the plane behind the
   * origin of the ray, so there is no visible intersection.
   * 
   * @param ray
   *          The {@link Ray} to intersect with this plane.
   * 
   * @return The distance in which the ray intersects this plane or
   *         {@link Shapes#NO_INTERSECTION} if there is no intersection.
   */
  @Override
  public double getIntersectDistance(Ray ray) {
    double numerator = this.origin.sub(ray.getOrigin()).dot(this.normal);
    double denominator = ray.getDirection().dot(this.normal);

    if (numerator == 0 && denominator == 0) {
      // The ray starts on the plane and is parallel to the plane, so it
      // intersects everywhere.
      return ray.getMint();
    } else if (numerator != 0 && denominator == 0) {
      // The ray starts outside the plane and is parallel to the plane, so no
      // intersection, ever...
      return Shapes.NO_INTERSECTION;
    }

    double distance = numerator / denominator;

    return (distance >= ray.getMint() && distance <= ray.getMaxt()) ? distance : Shapes.NO_INTERSECTION;

  }

  @Override
  public boolean isHitBy(Ray ray) {
    double numerator = this.origin.sub(ray.getOrigin()).dot(this.normal);
    double denominator = ray.getDirection().dot(this.normal);

    if (numerator == 0 && denominator == 0) {
      return true;
    } else if (numerator != 0 && denominator == 0) {
      return false;
    }

    return signum(numerator) == signum(denominator);
  }

  /**
   * The normal of a plane is independent from the position on the plane, so
   * always the defining normal is returned.
   * 
   * @param surfacePoint
   *          The surface point (as {@link Vector3D}) for which the normal is
   *          asked.
   * 
   * @return The normal of the plane (position independent)
   */
  @Override
  public Normal3D getNormal(Point3D surfacePoint) {
    return this.normal;
  }

  /**
   * Maps the given point to the planes u/v coordinates
   */
  @Override
  public Point2D getMappedSurfacePoint(Point3D surfacePoint) {
    // TODO implement real mapping to the surface
    return new Point2D(surfacePoint.getX(), surfacePoint.getZ());
  }

}
