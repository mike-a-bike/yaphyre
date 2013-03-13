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

package yaphyre.geometry;

import static java.lang.Math.pow;
import static org.apache.commons.math.util.MathUtils.binomialCoefficientDouble;

import org.jetbrains.annotations.NotNull;

/**
 * Calculates a single point on a bezier patch. The format of the data is a two dimensional array.
 * Example:
 * <pre>
 *     p[0][2] p[1][2] p[2][2] p[3][2]
 *     p[0][1] p[1][1] p[2][1] p[3][1]
 *     p[0][0] p[1][0] p[2][0] p[3][0]
 * </pre>
 * The u bezier-functions contain the following control points:
 * <pre>
 *     B0 = p[0][0], p[1][0], p[2][0], p[3][0]
 *     B1 = p[0][1], p[1][1], p[2][1], p[3][1]
 *     B2 = p[0][2], p[1][2], p[2][2], p[3][2]
 * </pre>
 * From these u bezier-functions, the corresponding v control points are calculated by evaluating each
 * set of control points for the given u-value. After that, the calculated control points are used as control
 * points for a bezier curve in the v-direction which is evaluated for the given v-value. The result is the
 * point in question.
 */
public enum BezierPatch {
	GENERIC {
		@NotNull
		@Override
		public Point3D calculateMeshPoint(final double u, final double v, @NotNull final Point3D[][] controlPoints) {
			return null;
		}
	};

	@NotNull
	public abstract Point3D calculateMeshPoint(double u, double v, @NotNull Point3D[][] controlPoints);

	private static double calculateBernsteinPolynomalFactor(final double t, final int order, final int k) {
		return binomialCoefficientDouble(order, k) * pow(t, k) * pow((1-t), (order - k));
	}

}
