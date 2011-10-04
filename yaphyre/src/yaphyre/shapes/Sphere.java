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

import java.text.MessageFormat;

import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector3D;
import yaphyre.math.Solver;
import yaphyre.shaders.Shaders;

/**
 * A sphere in the three dimensional space is defined as:<br/>
 * (p - p<sub>0</sub>) &sdot; (p - p<sub>0</sub>) = r<sup>2</sup><br/>
 * with:
 * <ul>
 * <li>p<sub>0</sub>: center of the sphere</li>
 * <li>p: point on the sphere</li>
 * <li>r: radius</li>
 * </ul>
 * 
 * @author Michael Bieri
 */
public class Sphere extends AbstractShape {

  private final Point3D center;

  private final double radius;

  public Sphere(String id, Point3D center, double radius, Shaders shader, boolean throwsShadow) {
    super(id, shader, throwsShadow);
    this.center = center;
    this.radius = radius;
  }

  @Override
  public String toString() {
    return MessageFormat.format("Sphere[{0}, {1}, {2}]", getId(), this.center, this.radius);
  }

  /**
   * Determine the distance on a half line where this line intersects with the
   * sphere. To do this, we use the parametric form of a line which is:<br/>
   * p(t) = p<sub>0</sub> + t * d<br/>
   * with
   * <ul>
   * <li>p(t): point on the line for the parameter value t</li>
   * <li>p<sub>0</sub>: line start point</li>
   * <li>d: direction</li>
   * <li>t: parameter value</li>
   * </ul>
   * We have to solve a quadratic equation: a*t<sup>2</sup> + b*t + c = 0<br/>
   * Solutions:<br/>
   * t<sub>0</sub> = (-b - SQRT( b<sup>2</sup> - 4ac)) / 2a<br/>
   * t<sub>1</sub> = (-b + SQRT( b<sup>2</sup> - 4ac)) / 2a<br/>
   * 
   * @param ray
   *          The {@link Ray} to intersect with this sphere.
   * 
   * @return The distance in which the ray intersects this sphere, or if they do
   *         not intersect {@link Shapes#NO_INTERSECTION}.
   */
  @Override
  public double getIntersectDistance(Ray ray) {
    // Transform the origin of the ray into the object space of the sphere.
    Vector3D oc = ray.getOrigin().sub(this.center);

    final double a = ray.getDirection().dot(ray.getDirection());
    final double b = 2 * oc.dot(ray.getDirection());
    final double c = oc.dot(oc) - this.radius * this.radius;

    final double[] solutions = Solver.Quadratic.solve(c, b, a);

    double result = Shapes.NO_INTERSECTION;

    for (double solution : solutions) {
      if (solution < result && solution >= 0d) {
        result = solution;
      }
    }

    return result;
  }

  @Override
  public Normal3D getNormal(Point3D surfacePoint) {
    return surfacePoint.sub(this.center).asNormal();
  }

  public Point3D getCenter() {
    return this.center;
  }

  public double getRadius() {
    return this.radius;
  }

  /**
   * Map the given point onto the u/v coordinates of the sphere.
   */
  @Override
  public Point2D getMappedSurfacePoint(Point3D surfacePoint) {
    // TODO implement the real u/v mapping
    return new Point2D(surfacePoint.getX(), surfacePoint.getY());
  }

}
