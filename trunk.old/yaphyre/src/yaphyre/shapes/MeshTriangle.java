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

import yaphyre.core.CollisionInformation;
import yaphyre.core.Primitive;
import yaphyre.geometry.MathUtils;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created with IntelliJ IDEA. User: michael Date: 17.02.13 Time: 12:50 To change this template use File | Settings | File
 * Templates.
 */
public abstract class MeshTriangle implements Primitive {

	protected final Point3D v0, v1, v2;

	protected final Point2D uv0, uv1, uv2;

	protected MeshTriangle(final Point3D v0, final Point3D v1, final Point3D v2,
			final Point2D uv0, final Point2D uv1, final Point2D uv2) {
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
		this.uv0 = uv0;
		this.uv1 = uv1;
		this.uv2 = uv2;
	}

	@NotNull
	protected Point2D calculateUVCoordinates(final double alpha, final double beta, final double gamma,
			@NotNull final Point2D uv0, @NotNull final Point2D uv1, @NotNull final Point2D uv2) {
		return uv0.mul(alpha).add(uv1.mul(beta)).add(uv2.mul(gamma));
	}

	@NotNull
	protected abstract Normal3D calculateNormal(final double alpha, final double beta, final double gamma);

	@Override
	@Nullable
	public CollisionInformation intersect(@NotNull final Ray ray) {
		CollisionInformation result = null;
		TriangleIntersectionInformation triangleIntersection = calculateTriangleIntersection(ray);

		if (triangleIntersection != null) {
			result = new CollisionInformation(ray, null,
					triangleIntersection.getIntersectionDistance(),
					ray.getPoint(triangleIntersection.getIntersectionDistance()),
					calculateNormal(triangleIntersection.getAlpha(), triangleIntersection.getBeta(), triangleIntersection.getGamma()),
					calculateUVCoordinates(triangleIntersection.getAlpha(), triangleIntersection.getBeta(), triangleIntersection.getGamma(), uv0, uv1, uv2));
		}
		return result;
	}

	@Nullable
	protected TriangleIntersectionInformation calculateTriangleIntersection(@NotNull final Ray ray) {
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
			// point of intersection behind the origin of the triangle
			return null;
		}

		// calculate gamma (second coordinate of the barycentric coordinate system)
		final double r = e * l - h * i;
		final double e2 = a * n + d * q + c * r;
		final double gamma = e2 * inv_denominator;

		if (gamma < 0d || beta + gamma > 1d) {
			// point of intersection behind the origin of the triangle or outside
			return null;
		}

		final double alpha = 1d - beta - gamma;

		// calculate the intersection distance
		final double e3 = a * p - b * r + d * s;
		final double t = e3 * inv_denominator;

		if (t < MathUtils.EPSILON) {
			// intersection behind the origin of the ray
			return null;
		}


		return new TriangleIntersectionInformation(alpha, beta, gamma, t);

	}

	protected final class TriangleIntersectionInformation {
		private final double alpha, beta, gamma;
		private final double intersectionDistance;

		public TriangleIntersectionInformation(final double alpha, final double beta, final double gamma,
											   final double intersectionDistance) {
			this.alpha = alpha;
			this.beta = beta;
			this.gamma = gamma;
			this.intersectionDistance = intersectionDistance;
		}

		public double getAlpha() {
			return alpha;
		}

		public double getBeta() {
			return beta;
		}

		public double getGamma() {
			return gamma;
		}

		public double getIntersectionDistance() {
			return intersectionDistance;
		}

	}

}
