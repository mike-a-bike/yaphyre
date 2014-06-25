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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
     * An unmodifiable collection containing the one sample of this sampler.
     */
    private static final List<Double> SAMPLES = Collections.unmodifiableList(Arrays.asList(0.5d));

    /**
     * Single sample for a unit square (0.5, 0.5).
     */
    private static final List<Point2D> UNIT_SQUARE_SAMPLE = Collections.unmodifiableList(Arrays.asList(new Point2D(0.5d, 0.5d)));

    /**
     * Single sample for the unit circle (origin: 0, 0).
     */
    private static final List<Point2D> UNIT_CIRCLE_SAMPLE = Collections.unmodifiableList(Arrays.asList(Point2D.ZERO));

    /**
     * Single sample for a hemisphere (straight up along the y axis: 0, 1, 0).
     */
    private static final List<Point3D> UNIT_HEMISPHERE_SAMPLE = Collections.unmodifiableList(Arrays.asList(new Point3D(0d, 1d, 0d)));

    /**
     * Get an Iterable for the samples. It this case contains just one element (0.5).
     */
    @Nonnull
    @Override
    public Iterable<Double> getSamples() {
        return SAMPLES;
    }

    @Nonnull
    @Override
    public Iterable<Point2D> getUnitSquareSamples() {
        return UNIT_SQUARE_SAMPLE;
    }

    @Nonnull
    @Override
    public Iterable<Point2D> getUnitCircleSamples() {
        return UNIT_CIRCLE_SAMPLE;
    }

    @Nonnull
    @Override
    public Iterable<Point3D> getUnitSphereSamples() {
        throw new RuntimeException("Illegal action on single value sampler: No single value for unit sphere possible");
    }

    @Nonnull
    @Override
    public Iterable<Point3D> getUnitHemisphereSamples(@Nonnegative double cosinePower) {
        return UNIT_HEMISPHERE_SAMPLE;
    }

}
