/*
 * Copyright 2012 Michael Bieri
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

import yaphyre.core.BoundingBox;
import yaphyre.core.Shader;
import yaphyre.core.Shape;
import yaphyre.geometry.*;

import java.text.MessageFormat;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Math.*;
import static yaphyre.geometry.MathUtils.*;

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

	private static final long serialVersionUID = -8353927614531728405L;

	/**
	 * The radius of the unit sphere is 1.
	 */
	private static final int RADIUS = 1;

	/**
	 * The squared radius of the unit sphere is 1: 1^2 = 1
	 */
	private static final int RADIUS_SQUARED = 1;

	private final BoundingBox boundingBox;

	/**
	 * Helper method for creating a sphere where the center and its radius are
	 * known. This creates a transformation which scales the unit sphere by the
	 * size of the given radius and translates it to the given coordinates. So,
	 * the resulting matrix looks like this: <br/>
	 * [[r 0 0 c<sub>x</sub>] [0 r 0 c<sub>y</sub>] [0 0 r c<sub>z</sub>] [0 0 0
	 * 1]]
	 *
	 * @param center       The center of the sphere (c<sub>x, y, z</sub>)
	 * @param radius       Its radius (r)
	 * @param shader       The {@link Shader} to use for rendering
	 * @param throwsShadow Flag whether this {@link Shape} throws a shadow or not
	 * @return A new instance of {@link Sphere}.
	 * @throws NullPointerException     If either <code>center</code> or <code>shader</code> are
	 *                                  <code>null</code>, a {@link NullPointerException} is thrown.
	 * @throws IllegalArgumentException If <code>radius</code> is too small, an
	 *                                  {@link IllegalArgumentException} is thrown.
	 */
	public static Sphere createSphere(Point3D center, double radius, Shader shader, boolean throwsShadow) throws NullPointerException, IllegalArgumentException {
		checkArgument(radius > EPSILON);
		checkNotNull(center);
		checkNotNull(shader);
		Transformation scaling = Transformation.scale(radius, radius, radius);
		Transformation translation = Transformation.translate(center.getX(), center.getY(), center.getZ());
		Transformation objectToWorld = translation.mul(scaling);
		return new Sphere(objectToWorld, shader, throwsShadow);
	}

	/**
	 * Creates a unit sphere at the origin of the world coordinates. Changes in
	 * the position of the objects and the radius are made by providing a
	 * transformation matrix.
	 * <ul>
	 * <li>Change the radius -> Use a scaling transformation.</li>
	 * <li>Change the center -> Use a translation transformation.</li>
	 * <li>Make an ellipsoid -> Use a non uniform scaling transformation</li>
	 * </ul>
	 *
	 * @param objectToWorld The {@link Transformation} to translate from object space into
	 *                      world space.
	 * @param shader        The {@link Shader} to use for this instance.
	 * @param throwsShadow  Flag whether this shape throws a shadow or not.
	 * @throws NullPointerException If either <code>objectToWorld</code> or <code>shader</code> is
	 *                              <code>null</code>, a {@link NullPointerException} is thrown.
	 */
	public Sphere(Transformation objectToWorld, Shader shader, boolean throwsShadow) throws NullPointerException {
		super(objectToWorld, shader, throwsShadow);
		this.boundingBox = new BoundingBox(super.getObjectToWorld().transform(new Point3D(-1, -1, -1)), super.getObjectToWorld().transform(new Point3D(1, 1, 1)));
	}

	@Override
	public String toString() {
		return MessageFormat.format("Sphere[{0}]", super.getObjectToWorld().toString());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Sphere)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingBox getBoundingBox() {
		return this.boundingBox;
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
	 * We have to solve a quadratic equation: c<sub>2</sub>*<em>t</em><sup>2</sup>
	 * + c<sub>1</sub>* <em>t</em> + c<sub>0</sub> = 0<br/>
	 * Solutions:<br/>
	 * <em>t</em><sub>0</sub> = (-c<sub>1</sub> - SQRT( c<sub>1</sub><sup>2</sup>
	 * - 4c<sub>2</sub>c<sub>0</sub>)) / 2c<sub>2</sub><br/>
	 * <em>t</em><sub>1</sub> = (-c<sub>1</sub> + SQRT( c<sub>1</sub><sup>2</sup>
	 * - 4c<sub>2</sub>c<sub>0</sub>)) / 2c<sub>2</sub><br/>
	 *
	 * @param ray The {@link Ray} to intersect with this sphere.
	 * @return The distance in which the ray intersects this sphere, or if they do
	 *         not intersect {@link Shape#NO_INTERSECTION}.
	 */
	@Override
	public double getIntersectDistance(Ray ray) {

		// Transform the incoming ray from the world space into the object space.
		ray = super.getWorldToObject().transform(ray);

		// Transform the origin of the ray into the object space of the sphere.
		Vector3D oc = ray.getOrigin().asVector();

		final double c2 = ray.getDirection().dot(ray.getDirection());
		final double c1 = 2 * oc.dot(ray.getDirection());
		final double c0 = oc.dot(oc) - RADIUS_SQUARED;

		final double[] solutions = Solver.Quadratic.solve(c0, c1, c2);

		double result = Shape.NO_INTERSECTION;

		for (double solution : solutions) {
			if (solution < result && (solution >= ray.getMint() && solution <= ray.getMaxt())) {
				result = solution;
			}
		}

		return result;
	}

	/**
	 * Calculate the normal for the given point. For a {@link Sphere} this is
	 * pretty simple:
	 * <ol>
	 * <li>Transform the surface point into the object space</li>
	 * <li>Construct the vector connecting the origin of the sphere and the
	 * surface point</li>
	 * <li>Transform the resulting normal back to world space</li>
	 * </ol>
	 */
	@Override
	public Normal3D getNormal(Point3D surfacePoint) {
		surfacePoint = super.getWorldToObject().transform(surfacePoint);
		return super.getObjectToWorld().transform(surfacePoint.asNormal());
	}

	/**
	 * Map the given point onto the <em>u</em>/<em>v</em> coordinates of the
	 * sphere. This is done by converting the standard Cartesian coordinates into
	 * the polar representation and mapping the angles &theta; and &phi; to the
	 * standard <em>u</em>/<em>v</em> range {u, v &isin; [0, 1]}<br/>
	 * The definition is:
	 * <ul>
	 * <li><em>cos</em>(&theta;) = <em>z</em> / <em>r</em></li>
	 * <li><em>tan</em>(&phi;) = <em>y</em> / <em>x</em></li>
	 * </ul>
	 * With &theta; &isin; [0, &pi;) and &phi; &isin; [0, 2&pi;)
	 *
	 * @throws NullPointerException     If <code>surfacePoint</code> is <code>null</code> a
	 *                                  {@link NullPointerException} is thrown.
	 * @throws IllegalArgumentException If <code>surfacePoint</code> does not lie on the surface of the
	 *                                  sphere an {@link IllegalArgumentException} is thrown.
	 */
	@Override
	public Point2D getMappedSurfacePoint(Point3D surfacePoint) throws NullPointerException, IllegalArgumentException {
		surfacePoint = super.getWorldToObject().transform(surfacePoint);
		// Make sure, that the point lies on the surface.
		checkNotNull(surfacePoint, "surfacePoint must not be null");
		checkArgument(Math.abs(surfacePoint.asVector().length()) - RADIUS <= EPSILON, "the point % does not lie on the surface of %", surfacePoint, this);

		// Calculate the two angles of the spherical coordinates
		double theta = acos(surfacePoint.getZ());
		double phi = atan2(surfacePoint.getY(), surfacePoint.getX()) + PI;

		// Map u and v to [0, 1]
		double u = phi * INV_TWO_PI;
		double v = theta * INV_PI;

		return new Point2D(u, v);
	}

}
