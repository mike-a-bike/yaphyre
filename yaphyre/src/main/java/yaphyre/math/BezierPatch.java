/*
 * Copyright 2013 Michael Bieri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yaphyre.math;

/**
 * Calculates a single point on a bezier patch. The format of the data is a linear array to make the usage simpler.
 * From these u bezier-functions, the corresponding v control points are calculated by evaluating each set of control
 * points for the given u-value. After that, the calculated control points are used as control points for a bezier
 * curve in the v-direction which is evaluated for the given v-value. The result is the point in question.
 */
public enum BezierPatch {
	GENERIC {
		@Override
		public Point3D calculateMeshPoint(final double u, final double v, final int uOrder, final int vOrder,
		                                  final int startIndex, final Point3D... controlPoints) {

			final Point3D[] vControlPoints = new Point3D[vOrder];

			// calculate the new v-control points by evaluating the u bezier curves for the u value
			for (int vIndex = 0; vIndex < vOrder; vIndex++) {
				final Point3D[] uControlPoints = new Point3D[uOrder];
				for (int uIndex = 0; uIndex < uOrder; uIndex++) {
					uControlPoints[uIndex] = controlPoints[MathUtils.calculateIndex(startIndex, uIndex, vIndex)];
				}
				vControlPoints[vIndex] = BezierCurve.GENERIC.calculatePoint(u, uControlPoints);
			}

			// evaluate the new bezier curve for the calculated control points for v
			return BezierCurve.GENERIC.calculatePoint(v, vControlPoints);
		}

	};

	public abstract Point3D calculateMeshPoint(final double u, final double v, final int uOrder, final int vOrder,
	                                           final int startIndex, final Point3D... controlPoints);

}
