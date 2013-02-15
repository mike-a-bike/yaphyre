/*
 * Copyright 2013 Michael Bieri
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

import yaphyre.core.Shader;
import yaphyre.geometry.MathUtils;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Transformation;

public class Triangle extends AbstractShape {

	private final Point3D v0, v1, v2;

	private final Normal3D normal;

	public static Triangle create(final Point3D v0, final Point3D v1, final Point3D v2, final Shader shader) {
		return new Triangle(v0, v1, v2, shader, Transformation.IDENTITY, true);
	}

	public static Triangle create(final Point3D v0, final Point3D v1, final Point3D v2, final Shader shader, final Transformation transformation) {
		return new Triangle(v0, v1, v2, shader, transformation, true);
	}

	public static Triangle create(final Point3D v0, final Point3D v1, final Point3D v2, final Shader shader, final Transformation transformation, final boolean throwsShadow) {
		return new Triangle(v0, v1, v2, shader, transformation, throwsShadow);
	}

	public Triangle(final Point3D v0, final Point3D v1, final Point3D v2, final Shader shader, final Transformation transformation, final boolean throwsShadow) {
		super(transformation, shader, throwsShadow);
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
		normal = v1.sub(v0).cross(v2.sub(v0)).normalize().asNormal();
	}

	@Override
	public double getIntersectDistance(final Ray ray) {

		// prepare system of equations
		final double a = v0.getX() - v1.getX(), b = v0.getX() - v2.getX(), c = ray.getDirection().getX(), d = v0.getX() - ray.getOrigin().getX();
		final double e = v0.getY() - v1.getY(), f = v0.getY() - v2.getY(), g = ray.getDirection().getY(), h = v0.getY() - ray.getOrigin().getY();
		final double i = v0.getZ() - v1.getZ(), j = v0.getZ() - v2.getZ(), k = ray.getDirection().getZ(), l = v0.getZ() - ray.getOrigin().getZ();

		final double m = f*k - g*j, n = h*k - g*l, p = f*l - h*j, q = g*i-e*k, s = e*j - f*i;

		final double inv_denominator = 1d / (a*m + b*q + c*s);

		// calculate beta (first coordinate of the barycentric coordinate system)
		final double e1 = d*m - b*n - c*p;
		final double beta = e1 * inv_denominator;

		if (beta < 0d) {
			// point of intersection behind the origin
			return NO_INTERSECTION;
		}

		// calculate gamma (second coordinate of the barycentric coordinate system)
		final double r = e * l - h * i;
		final double e2 = a * n + d * q + c * r;
		final double gamma = e2 * inv_denominator;

		if (gamma < 0d || beta + gamma > 1d) {
			// point of intersection behind the origin or outside the triangle
			return NO_INTERSECTION;
		}

		// calculate the intersection distance
		final double e3 = a * p - b * r + d * s;
		final double t = e3 * inv_denominator;

		if (t < MathUtils.EPSILON) {
			// intersection behind the origin of the ray
			return NO_INTERSECTION;
		}

		return t;
	}

	@Override
	public Normal3D getNormal(final Point3D surfacePoint) {
		return normal;
	}

	@Override
	public Point2D getMappedSurfacePoint(final Point3D surfacePoint) {
		return null;
	}

	@Override
	public boolean isInside(final Point3D point) {
		return false;
	}

}
