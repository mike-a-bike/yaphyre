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

package yaphyre.core.samplers;

import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import yaphyre.core.api.Sampler;
import yaphyre.core.math.Point2D;
import yaphyre.core.math.Point3D;

/**
 * Simple sampler. It only returns one value (0.5) which represents the mean value of the range (0-1).
 *
 * @author Michael Bieri
 * @since 04.02.14
 */
public class SingleValueSampler implements Sampler {

    /**
     * Single sample for a unit square (0.5, 0.5).
     */
    private static final Point2D UNIT_SQUARE_SAMPLE = new Point2D(0.5d, 0.5d);

    /**
     * Single sample for the unit circle (origin: 0, 0).
     */
    private static final Point2D UNIT_CIRCLE_SAMPLE = Point2D.ZERO;

    /**
     * Single sample for a hemisphere (straight up along the y axis: 0, 1, 0).
     */
    private static final Point3D UNIT_HEMISPHERE_SAMPLE = new Point3D(0d, 1d, 0d);

    @Nonnull
    @Override
    public Stream<Point2D> getUnitSquareSamples() {
        return Stream.of(UNIT_SQUARE_SAMPLE);
    }

    @Nonnull
    @Override
    public Stream<Point2D> getUnitCircleSamples() {
        return Stream.of(UNIT_CIRCLE_SAMPLE);
    }

    @Nonnull
    @Override
    public Stream<Point3D> getUnitSphereSamples() {
        throw new RuntimeException("Illegal action on single value sampler: No single value for unit sphere possible");
    }

    @Nonnull
    @Override
    public Stream<Point3D> getUnitHemisphereSamples(@Nonnegative double cosinePower) {
        return Stream.of(UNIT_HEMISPHERE_SAMPLE);
    }

}
