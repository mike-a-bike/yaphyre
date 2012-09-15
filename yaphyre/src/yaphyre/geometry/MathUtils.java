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
package yaphyre.geometry;

import static java.lang.Math.*;

/**
 * Some useful mathematical helper functions.
 *
 * @author Michael Bieri
 * @author $LastChangedBy$
 * @version $Revision$
 */
public class MathUtils {

	/**
	 * Small value. Used for tolerance calculations.
	 */
	public static final double EPSILON = 1e-10;

	/**
	 * Inverse of &pi;. If division by &pi; is needed, use this and multiply.
	 */
	public static final double INV_PI = 1d / PI;

	/**
	 * The numerical value of 2&pi;.
	 */
	public static final double TWO_PI = 2d * PI;

	/**
	 * Inverse of 2&pi;. If division by 2&pi; is needed, use this and multiply.
	 */
	public static final double INV_TWO_PI = 1d / TWO_PI;

	/**
	 * Use this (large value indeed) to handle division by zero.
	 */
	private static final double LARGE_VAL = Double.MAX_VALUE;

	/**
	 * Safe division of two numerical values. If b is zero, than
	 * {@link #LARGE_VAL} is returned. The signs are maintained. So this method
	 * never throws an error for dividing by zero. Since this application only
	 * solves numerical problems, this is the preferred behavior.
	 *
	 * @param a The numerator.
	 * @param b The denominator.
	 * @return a / b if b is not zero, {@link #LARGE_VAL} otherwise.
	 */
	public static double div(double a, double b) {
		if (a == 0) {
			return 0;
		}
		if (b == 0) {
			return LARGE_VAL * signum(a);
		}
		if ((a + b) == a) {
			return LARGE_VAL * signum(a) * signum(b);
		}
		return a / b;
	}

	public static boolean equalsWithTolerance(double v1, double v2, double tolerance) {
		double diff = abs(v1 - v2);
		return diff <= tolerance;
	}

	public static boolean equalsWithTolerance(double v1, double v2) {
		return equalsWithTolerance(v1, v2, EPSILON);
	}

	public static double calcLength(double... values) {
		double lengthSquared = calculateLengthSquared(values);
		return sqrt(lengthSquared);
	}

	public static double calculateLengthSquared(double... values) {
		double result = 0d;
		for (double value : values) {
			result += value * value;
		}
		return result;
	}

}
