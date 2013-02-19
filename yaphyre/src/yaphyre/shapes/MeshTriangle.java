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

import yaphyre.core.Primitive;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;

import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA. User: michael Date: 17.02.13 Time: 12:50 To change this template use File | Settings | File
 * Templates.
 */
public abstract class MeshTriangle implements Primitive {

	@Override
	public abstract double getIntersectDistance(@NotNull final Ray ray);

	@NotNull
	@Override
	public abstract Normal3D getNormal(@NotNull final Point3D surfacePoint);

	@NotNull
	@Override
	public abstract Point2D getMappedSurfacePoint(@NotNull final Point3D surfacePoint);

	protected abstract TriangleIntersectionInformation calculateTriangleIntersection(@NotNull final Ray ray);

	protected final class TriangleIntersectionInformation {
		private final double alpha, beta, gamma;
		private final double intersectionDistance;
		private final Point3D intersectionPoint;
		private final Point2D intersectionUVCoordinates;
		private final Normal3D intersectionNormal;

		public TriangleIntersectionInformation(final double alpha, final double beta, final double gamma,
											   final double intersectionDistance,
											   final Point3D intersectionPoint,
											   final Point2D intersectionUVCoordinates,
											   final Normal3D intersectionNormal) {
			this.alpha = alpha;
			this.beta = beta;
			this.gamma = gamma;
			this.intersectionDistance = intersectionDistance;
			this.intersectionPoint = intersectionPoint;
			this.intersectionUVCoordinates = intersectionUVCoordinates;
			this.intersectionNormal = intersectionNormal;
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

		public Point3D getIntersectionPoint() {
			return intersectionPoint;
		}

		public Point2D getIntersectionUVCoordinates() {
			return intersectionUVCoordinates;
		}

		public Normal3D getIntersectionNormal() {
			return intersectionNormal;
		}
	}

}
