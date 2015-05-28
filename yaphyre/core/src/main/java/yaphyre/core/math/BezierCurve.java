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

package yaphyre.core.math;

import com.google.common.collect.Range;

import static com.google.common.base.Preconditions.checkArgument;
import static yaphyre.core.math.MathUtils.calculateBernsteinPolynomialFactor;

/**
 * An implementation for calculating points on a bezier curve. Each instance takes a different number of control
 * points
 */
public enum BezierCurve {

    LINEAR {
        @Override
        public Point3D calculatePoint(final double t, final Point3D... controlPoints) {
            checkArgument(controlPoints.length == 2, "Wrong number of control points. Expected: 2");
            checkArgument(validTRange.contains(t), "t is out of range: 0 <= t <= 1");

            final Point3D p0 = controlPoints[0];
            final Point3D p1 = controlPoints[1];

            final double s = 1d - t;

            return p0.scale(s).add(p1.scale(t));

			/* geometric interpretation
            return p0.add(p1.sub(p0).scale(t));
			*/
        }
    },
    QUADRATIC {
        @Override
        public Point3D calculatePoint(final double t, final Point3D... controlPoints) {
            checkArgument(controlPoints.length == 3, "Wrong number of control points. Expected: 3");
            checkArgument(validTRange.contains(t), "t is out of range: 0 <= t <= 1");

            final Point3D p0 = controlPoints[0];
            final Point3D p1 = controlPoints[1];
            final Point3D p2 = controlPoints[2];
            final double s = 1d - t;

            return p0.scale(s * s)
                    .add(p1.scale(2d * s * t))
                    .add(p2.scale(t * t));

			/* geometric interpretation
			final Point3D pa = LINEAR.calculatePoint(t, p0, p1);
			final Point3D pb = LINEAR.calculatePoint(t, p1, p2);

			return LINEAR.calculatePoint(t, pa, pb);
			*/
        }
    },
    CUBIC {
        @Override
        public Point3D calculatePoint(final double t, final Point3D... controlPoints) {
            checkArgument(controlPoints.length == 4, "Wrong number of control points. Expected: 4");
            checkArgument(validTRange.contains(t), "t is out of range: 0 <= t <= 1");

            final Point3D p0 = controlPoints[0];
            final Point3D p1 = controlPoints[1];
            final Point3D p2 = controlPoints[2];
            final Point3D p3 = controlPoints[3];

            final double s = 1d - t;

            return p0.scale(s * s * s)
                    .add(p1.scale(3d * s * s * t))
                    .add(p2.scale(3d * s * t * t))
                    .add(p3.scale(t * t * t));

			/* geometric interpretation
			final Point3D pa = LINEAR.calculatePoint(t, p0, p1);
			final Point3D pb = LINEAR.calculatePoint(t, p1, p2);
			final Point3D pc = LINEAR.calculatePoint(t, p2, p3);

			return QUADRATIC.calculatePoint(t, pa, pb, pc);
			*/
        }
    },
    QUARTIC {
        @Override
        public Point3D calculatePoint(final double t, final Point3D... controlPoints) {
            checkArgument(controlPoints.length == 5, "Wrong number of control points. Expected: 5");
            checkArgument(validTRange.contains(t), "t is out of range: 0 <= t <= 1");

            final Point3D p0 = controlPoints[0];
            final Point3D p1 = controlPoints[1];
            final Point3D p2 = controlPoints[2];
            final Point3D p3 = controlPoints[3];
            final Point3D p4 = controlPoints[4];

            final double s = 1d - t;

            return p0.scale(s * s * s * s)
                    .add(p1.scale(4d * s * s * s * t))
                    .add(p2.scale(6d * s * s * t * t))
                    .add(p3.scale(4d * s * t * t * t))
                    .add(p4.scale(t * t * t * t));

        }
    },
    GENERIC {
        @Override
        public Point3D calculatePoint(final double t, final Point3D... controlPoints) {
            checkArgument(controlPoints.length >= 2, "Too few control points. At least two control points are needed");
            checkArgument(validTRange.contains(t), "t is out of range: 0 <= t <= 1");

            if (t == 0d) {
                return controlPoints[0];
            } else if (t == 1d) {
                return controlPoints[controlPoints.length - 1];
            }

            final int order = controlPoints.length - 1;
            switch (order) {
                case 1:
                    return LINEAR.calculatePoint(t, controlPoints);
                case 2:
                    return QUADRATIC.calculatePoint(t, controlPoints);
                case 3:
                    return CUBIC.calculatePoint(t, controlPoints);
                case 4:
                    return QUARTIC.calculatePoint(t, controlPoints);
            }

            Point3D result = Point3D.ORIGIN;

            for (int k = 0; k <= order; k++) {
                final double scale = calculateBernsteinPolynomialFactor(t, order, k);
                result = result.add(controlPoints[k].scale(scale));
            }

            return result;

        }

    };

    private static final Range<Double> validTRange = Range.closed(0d, 1d);

    public abstract Point3D calculatePoint(double t, final Point3D... controlPoints);

}
