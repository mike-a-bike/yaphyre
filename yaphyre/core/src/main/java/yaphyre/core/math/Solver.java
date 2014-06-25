/*
 * Copyright 2014 Michael Bieri
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

package yaphyre.core.math;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import com.google.common.primitives.Doubles;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.math3.util.FastMath.acos;
import static org.apache.commons.math3.util.FastMath.cbrt;
import static org.apache.commons.math3.util.FastMath.cos;
import static org.apache.commons.math3.util.FastMath.sin;
import static org.apache.commons.math3.util.FastMath.sqrt;
import static yaphyre.core.math.MathUtils.div;
import static yaphyre.core.math.MathUtils.isZero;

public enum Solver {

    Linear {
        /**
         * Solve a linear equation for:<br/>
         * ax + b = 0
         */
        @Override
        public double[] solve(double... coefficients) throws IllegalArgumentException {

            checkArgument(coefficients.length == 2, ORDER_ERROR_MESSAGE);

            final double a = coefficients[1];
            final double b = coefficients[0];

            if (isZero(a)) {
                return EMPTY_RESULT;
            }

            return new double[]{div(-b, a)};
        }
    },

    Quadratic {
        /**
         * Solve a quadratic equation for:<br/>
         * ax<sup>2</sup> + bx + c = 0
         */
        @Override
        public double[] solve(double... coefficients) throws IllegalArgumentException {

            checkArgument(coefficients.length == 3, ORDER_ERROR_MESSAGE);

            final double a = coefficients[2];
            final double b = coefficients[1];
            final double c = coefficients[0];

            if (isZero(a)) {
                return Solver.Linear.solve(c, b);
            }

            double det = b * b - 4 * a * c;
            if (isZero(det)) {
                return new double[]{div(-b, 2 * a)};
            }

            if (det > 0) {
                double sqrtDet = sqrt(det);
                double[] result = new double[2];
                result[0] = div(-b - sqrtDet, 2 * a);
                result[1] = div(-b + sqrtDet, 2 * a);
                Arrays.sort(result);
                return result;
            } else {
                return EMPTY_RESULT;
            }
        }
    },

    Cubic {
        /**
         * Solve a cubic equation for:<br/>
         * ax<sup>3</sup> + bx<sup>2</sup> + cx + d = 0 <br/>
         * http://en.wikipedia.org/wiki/Cubic_function under Trigonometric (and
         * hyperbolic) method or http://www.1728.org/cubic.htm
         */
        @Override
        public double[] solve(double... coefficients) throws IllegalArgumentException {

            checkArgument(coefficients.length == 4, ORDER_ERROR_MESSAGE);

            final double a = coefficients[3];
            final double b = coefficients[2];
            final double c = coefficients[1];
            final double d = coefficients[0];

            if (isZero(a)) {
                return Solver.Quadratic.solve(d, c, b);
            }

            if (isZero(b) && isZero(c)) {
                return new double[]{cbrt(div(-d, a))};
            }

            if (isZero(d)) {
                double[] quadResults = Solver.Quadratic.solve(c, b, a);
                Set<Double> results = new HashSet<>();
                results.add(0d);
                for (double value : quadResults) {
                    results.add(value);
                }

                final double[] solutions = Doubles.toArray(results);
                if (solutions.length > 1) {
                    Arrays.sort(solutions);
                }
                return solutions;
            }

            final double f = (((3d * c) / a) - ((b * b) / (a * a))) / 3d;
            final double g = ((((2d * (b * b * b)) / (a * a * a)) - ((9d * b * c) / (a * a))) + ((27d * d) / a)) / 27d;
            final double h = (g * g) / 4 + (f * f * f) / 27;

            if (h <= 0d) {
                // all three roots are real

                if (isZero(f) && isZero(g) && isZero(h)) {
                    // all three roots are real AND equal
                    final double solution = cbrt(d / a) * -1d;
                    return new double[]{solution, solution, solution};
                }

                final double i = sqrt(g * g / 4d - h);
                final double j = cbrt(i);
                final double K = acos(-g / (2d * i));
                final double M = cos(K / 3d);
                final double N = sqrt(3d) * sin(K / 3d);
                final double P = -(b / (3d * a));

                final double x1 = 2d * j * cos(K / 3d) - (b / (a * 3d));
                final double x2 = -j * (M + N) + P;
                final double x3 = -j * (M - N) + P;

                final double[] solutions = new double[]{x1, x2, x3};
                if (solutions.length > 1) {
                    Arrays.sort(solutions);
                }
                return solutions;

            }

            final double R = -(g / 2d) + sqrt(h);
            final double S = cbrt(R);
            final double T = -(g / 2d) - sqrt(h);
            final double U = cbrt(T);

            return new double[]{(S + U) - (b / (a * 3d))};

        }
    },

    Quartic {
        /**
         * Solve a quartic equation for:<br/>
         * ax<sup>4</sup> + bx<sup>3</sup> + cx<sup>2</sup> + dx + e = 0<br/>
         * Lodovico Ferraria (1522 - 1565): http://www.sosmath.com/algebra/factor/fac12/fac12.html
         * also http://www.1728.org/quartic.htm
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

                Set<Double> resultSet = new HashSet<>();

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

            if (results.length > 1) {
                Arrays.sort(results);
            }
            return results;
        }
    };

    protected static final double[] EMPTY_RESULT = new double[0];

    private static final String ORDER_ERROR_MESSAGE = "Number of coefficients do not match the order of the equation to solve";

    /**
     * Solve an equation for the given equation type.
     *
     * @param c A list of coefficients. c[0] -> a, c[1] -> b, c[2] -> c, c[3] -> d, ...
     * @return A list of solutions. This list may be empty if there are no real solutions.
     * @throws IllegalArgumentException If the number of the coefficients in <code>c</code> does not match the necessary number of values, an {@link
     * IllegalArgumentException} is thrown. Please notice, even if coefficients may be zero, they must be provided.
     */
    public abstract double[] solve(double... c) throws IllegalArgumentException;

}
