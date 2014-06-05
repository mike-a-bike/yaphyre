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

import com.google.common.primitives.Doubles;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.cbrt;
import static java.lang.Math.cos;
import static java.lang.Math.sqrt;
import static yaphyre.math.MathUtils.div;
import static yaphyre.math.MathUtils.isZero;

public enum Solver {

	Linear {
		/**
		 * Solve a linear equation for:<br/>
		 * c<sub>1</sub>*x + c<sub>0</sub> = 0
		 */
		@Override
		public double[] solve(double... c) throws IllegalArgumentException {

			checkArgument(c.length == 2, ORDER_ERROR_MESSAGE);

			if (c[1] == 0) {
				return EMPTY_RESULT;
			}
			return new double[]{div(-c[0], c[1])};
		}
	},
	Quadratic {
		/**
		 * Solve a quadratic equation for:<br/>
		 * c<sub>2</sub>*x<sup>2</sup> + c<sub>1</sub>*x + c<sub>0</sub> = 0
		 */
		@Override
		public double[] solve(double... c) throws IllegalArgumentException {

			checkArgument(c.length == 3, ORDER_ERROR_MESSAGE);

			if (c[2] == 0) {
				return Solver.Linear.solve(c[0], c[1]);
			}

			double det = c[1] * c[1] - 4 * c[2] * c[0];
			if (det == 0) {
				return new double[]{div(-c[1], 2 * c[2])};
			}

			if (det > 0) {
				double sqrtDet = sqrt(det);
				double[] result = new double[2];
				result[0] = div(-c[1] - sqrtDet, 2 * c[2]);
				result[1] = div(-c[1] + sqrtDet, 2 * c[2]);
                orderResults(result);
                return result;
			} else {
				return EMPTY_RESULT;
			}
		}

        private void orderResults(double[] result) {
            if (result[0] > result[1]) {
                double tmp = result[0];
                result[0] = result[1];
                result[1] = tmp;
            }
        }
    },
	Cubic {
		/**
		 * Solve a cubic equation for:<br/>
		 * c<sub>3</sub>*x<sup>3</sup> + c<sub>2</sub>*x<sup>2</sup> + c<sub>1</sub>*x + c<sub>0</sub> = 0 <br/>
		 * http://en.wikipedia.org/wiki/Cubic_function under Trigonometric (and
		 * hyperbolic) method
		 */
		@Override
		public double[] solve(double... c) throws IllegalArgumentException {

			checkArgument(c.length == 4, ORDER_ERROR_MESSAGE);

			if (c[3] == 0) {
				return Solver.Quadratic.solve(c[0], c[1], c[2]);
			}

			if (c[2] == 0 && c[1] == 0) {
				return new double[]{cbrt(div(-c[0], c[3]))};
			}

			if (c[0] == 0) {
				double[] quadResults = Solver.Quadratic.solve(c[1], c[2], c[3]);
				Set<Double> results = new HashSet<Double>();
				results.add(0d);
				for (double value : quadResults) {
					results.add(value);
				}

				return Doubles.toArray(results);
			}

			double k, p, q;

			if (c[2] != 0) {
				k = div(-c[2], 3 * c[3]);
				p = div(3 * c[3] * c[1] - c[2] * c[2], -3 * c[3] * c[3]);
				q = div(2 * c[2] * c[2] * c[2] - 9 * c[3] * c[2] * c[1] + 27 * c[3] * c[3] * c[0],
						27 * c[3] * c[3] * c[3]);
			} else {
				k = 0;
				p = div(-c[1], c[3]);
				q = div(-c[0], c[3]);
			}

			double p_d3_e3 = p * p / 27 * p;
			double w = q / 4 * q - p_d3_e3;

			if (w < 0) {
				double cos3a = div(q, 2 * sqrt(p_d3_e3));
				double a = acos(cos3a) / 3;
				double t = sqrt(p * (4 / 3));
				double[] results = new double[3];
				results[0] = t * cos(a) + k;
				results[1] = t * cos(a + 2 * PI / 3) + k;
				results[2] = t * cos(a + 2 * (2 * PI / 3)) + k;
				return results;
			}

			if (w == 0) {
				return new double[]{2 * cbrt(q / 2) + k};
			}

			return new double[]{cbrt(q / 2 + sqrt(w)) + cbrt(q / 2 - sqrt(w)) + k};

		}
	},
	Quartic {
		/**
		 * Solve a quartic equation for:<br/>
		 * c<sub>4</sub>*x<sub>4</sub> + c<sub>3</sub>*x<sup>3</sup> + c<sub>2</sub>*x<sup>2</sup> + c<sub>1</sub>*x + c<sub>0</sub> = 0<br/>
		 * Lodovico Ferraria (1522 - 1565): http://www.sosmath.com/algebra/factor/fac12/fac12.html
		 */
		@Override
		public double[] solve(double... c) throws IllegalArgumentException {
			checkArgument(c.length == 5, ORDER_ERROR_MESSAGE);

			if (c[4] == 0) {
				return Solver.Cubic.solve(c[0], c[1], c[2], c[3]);
			}

			double[] results;

			// normalize to x^4 + Ax^3 + Bx^2 + Cx + D = 0
			double A = c[3] / c[4];
			double B = c[2] / c[4];
			double C = c[1] / c[4];
			double D = c[0] / c[4];

			// substitute x = y - A/4 to eliminate the cubic term: y^4 + py^2 + qy + r = 0 (depressed quartic)
			double sqA = A * A;
			double p = -3d / 8d * sqA + B;
			double q = 1d / 8d * sqA * A - 1d / 2d * A * B + C;
			double r = -3d / 256d * sqA * sqA + 1d / 16d * sqA * B - 1d / 4d * A * C + D;

			// check if the constant term is zero: if so, a further simplification can be used

			if (isZero(r)) {

				double[] coefficients = new double[]{q, p, 0, 1};
				// the equation reads now: y(y^3 + py + q) = 0 --> solve the cubic equation
				double[] cubicResults = Solver.Cubic.solve(coefficients);
				// add zero as a solution
				results = new double[cubicResults.length + 1];
				System.arraycopy(cubicResults, 0, results, 0, cubicResults.length);
				results[cubicResults.length + 1] = 0;

			} else {

				Set<Double> resultSet = new HashSet<Double>();

				// solve the resolvent qubic
				double[] coefficients = new double[4];

				coefficients[0] = 1d / 2d * r * p - 1d / 8d * q * q;
				coefficients[1] = -r;
				coefficients[2] = -1d / 2d * p;
				coefficients[3] = 1;

				double[] cubicResults = Solver.Cubic.solve(coefficients);

				// take the only real solution
				double z = cubicResults[0];

				// build two quadratic equations
				double u = z * z - r;
				double v = 2 * z - p;

				if (isZero(u)) {
					u = 0;
				} else if (u > 0) {
					u = sqrt(u);
				} else {
					return EMPTY_RESULT;
				}

				if (isZero(v)) {
					v = 0;
				} else if (v > 0) {
					v = sqrt(v);
				} else {
					return EMPTY_RESULT;
				}

				coefficients = new double[3];
				coefficients[0] = z - u;
				coefficients[1] = q < 0 ? -v : v;
				coefficients[2] = 1;

				double[] quadraticResults = Solver.Quadratic.solve(coefficients);
				resultSet.addAll(Doubles.asList(quadraticResults));

				coefficients[0] = z + u;
				coefficients[1] = q < 0 ? v : -v;
				coefficients[2] = 1;

				quadraticResults = Solver.Quadratic.solve(coefficients);
				resultSet.addAll(Doubles.asList(quadraticResults));

				results = Doubles.toArray(resultSet);

			}

			// re-substitute
			double sub = 1d / 4d * A;

			for (int rootIndex = 0; rootIndex < results.length; rootIndex++) {
				results[rootIndex] -= sub;
			}

			return results;
		}
	};

	protected static final double[] EMPTY_RESULT = new double[0];

	private static final String ORDER_ERROR_MESSAGE = "Number of coefficients do not match the order of the equation to solve";

	/**
	 * Solve an equation for the given equation type.
	 *
	 * @param c A list of coefficients.
	 *
	 * @return A list of solutions. This list may be empty if there are no real solutions.
	 *
	 * @throws IllegalArgumentException If the number of the coefficients in <code>c</code> does not match the necessary number of values, an {@link
	 *                                  IllegalArgumentException} is thrown. Please notice, even if coefficients may be zero, they must be provided.
	 */
	public abstract double[] solve(double... c) throws IllegalArgumentException;

}
