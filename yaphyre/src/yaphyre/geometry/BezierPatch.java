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
 *
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
