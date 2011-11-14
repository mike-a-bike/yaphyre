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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static yaphyre.math.MathUtils.EPSILON;
import static yaphyre.math.MathUtils.INV_PI;
import static yaphyre.math.MathUtils.INV_TWO_PI;
import static yaphyre.math.MathUtils.div;

import java.text.MessageFormat;

import yaphyre.core.Shader;
import yaphyre.core.Shape;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector3D;
import yaphyre.math.MathUtils;
import yaphyre.math.Solver;

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

  /**
   * Creates a new instance of a sphere. It is defined by its center (as
   * {@link Point3D}) and its radius. Since it extends {@link AbstractShape},
   * all the mandatory fields from there are also needed.
   * 
   * @param id
   *          A string which identifies this instance.
   * @param center
   *          A {@link Point3D} which describes where the center of the sphere
   *          lies. This cannot be <code>null</code>.
   * @param radius
   *          The radius of the sphere. This value must be bigger than
   *          {@link MathUtils#EPSILON}.
   * @param shader
   *          The {@link Shader} instance used to render this sphere.
   * @param throwsShadow
   *          Flag whether this object throws a show or not.
   * 
   * @throws NullPointerException
   *           If <code>center</code> is <code>null</code>, a
   *           <code>NullPointerException</code> is thrown.
   * @throws IllegalArgumentException
   *           If the <code>radius</code> is smaller than
   *           {@link MathUtils#EPSILON}, an
   *           <code>IllegalArgumentException</code> is thrown.
   */
  public Sphere(Point3D center, double radius, Shader shader, boolean throwsShadow) throws NullPointerException, IllegalArgumentException {
    super(shader, throwsShadow);
    checkNotNull(center);
    checkArgument(radius > EPSILON, "the radius [%] is smaller than the allowed minimal size [%]", radius, EPSILON);
    this.center = center;
    this.radius = radius;
  }

  @Override
  public String toString() {
    return MessageFormat.format("Sphere[{0}, {1}]", this.center, this.radius);
  }

  /**
   * Determine the distance on a half line where this line intersects with the
   * sphere. To do this, we use the parametric form of a line which is:<br/>
   * p(<em>t</em>) = p<sub>0</sub> + <em>t</em> * d<br/>
   * with
   * <ul>
   * <li>p(<em>t</em>): point on the line for the parameter value <em>t</em></li>
   * <li>p<sub>0</sub>: line start point</li>
   * <li>d: direction</li>
   * <li><em>t</em>: parameter value</li>
   * </ul>
   * We have to solve a quadratic equation: a*<em>t</em><sup>2</sup> + b*
   * <em>t</em> + c = 0<br/>
   * Solutions:<br/>
   * <em>t</em><sub>0</sub> = (-b - SQRT( b<sup>2</sup> - 4ac)) / 2a<br/>
   * <em>t</em><sub>1</sub> = (-b + SQRT( b<sup>2</sup> - 4ac)) / 2a<br/>
   * 
   * @param ray
   *          The {@link Ray} to intersect with this sphere.
   * 
   * @return The distance in which the ray intersects this sphere, or if they do
   *         not intersect {@link Shape#NO_INTERSECTION}.
   */
  @Override
  public double getIntersectDistance(Ray ray) {
    // Transform the origin of the ray into the object space of the sphere.
    Vector3D oc = ray.getOrigin().sub(this.center);

    final double a = ray.getDirection().dot(ray.getDirection());
    final double b = 2 * oc.dot(ray.getDirection());
    final double c = oc.dot(oc) - this.radius * this.radius;

    final double[] solutions = Solver.Quadratic.solve(c, b, a);

    double result = Shape.NO_INTERSECTION;

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
   * Map the given point onto the <em>u</em>/<em>v</em> coordinates of the
   * sphere. This is done by converting the standard Cartesian coordinates into
   * the polar representation and mapping the angles &theta; and &phi; to the
   * standard <em>u</em>/<em>v</em> range [0, 1]<br/>
   * The definition is:
   * <ul>
   * <li><em>cos</em>(&theta;) = <em>z</em> / <em>r</em></li>
   * <li><em>tan</em>(&phi;) = <em>y</em> / <em>x</em></li>
   * </ul>
   * With &theta; &isin; [0, &pi;) and &phi; &isin; [0, 2&pi;)
   * 
   * @throws NullPointerException
   *           If <code>surfacePoint</code> is <code>null</code> a
   *           {@link NullPointerException} is thrown.
   * 
   * @throws IllegalArgumentException
   *           If <code>surfacePoint</code> does not lie on the surface of the
   *           sphere an {@link IllegalArgumentException} is thrown.
   */
  @Override
  public Point2D getMappedSurfacePoint(Point3D surfacePoint) throws NullPointerException, IllegalArgumentException {
    // Make sure, that the point lies on the surface.
    checkNotNull(surfacePoint, "surfacePoint must not be null");
    Vector3D surfacePointVector = new Vector3D(this.center, surfacePoint);
    checkArgument(Math.abs(surfacePointVector.length() - this.radius) <= EPSILON, "the point % does not lie on the surface of %", surfacePoint, this);

    // Calculate the two angles of the spherical coordinates
    double theta = acos(div(surfacePointVector.getZ(), this.radius));
    double phi = atan2(surfacePointVector.getY(), surfacePointVector.getX()) + PI;

    // Map to [0, 1] for u and v
    double u = phi * INV_TWO_PI;
    double v = theta * INV_PI;

    return new Point2D(u, v);
  }

}
