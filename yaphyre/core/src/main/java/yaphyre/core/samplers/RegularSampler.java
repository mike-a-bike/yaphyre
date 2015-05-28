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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

import yaphyre.core.math.Point2D;

import static java.util.stream.Collectors.toList;

/**
 * A sampler which created regularly spaced samples across the value range [0,1). This will most probably not be used
 * in production, but is very useful for debugging purposes.
 *
 * @author Michael Bieri
 * @since 18.02.14
 */
public class RegularSampler extends AbstractSampler {

    private static final Map<Integer, List<Point2D>> SAMPLES = new HashMap<>();

    List<Point2D> samples = null;

    public RegularSampler(int numberOfSamples) {
        super(numberOfSamples);
    }

    @Nonnull
    private DoubleStream createLinearSamples(int numberOfSamples) {
        final double stepSize = 1d / numberOfSamples;
        final double stepSizeHalf = stepSize / 2d;
        return IntStream.range(0, numberOfSamples).mapToDouble(step -> stepSizeHalf + step * stepSize);
    }

    @Nonnull
    @Override
    protected Stream<Point2D> createUnitSquareSamples(int numberOfSamples) {
        return SAMPLES.computeIfAbsent(numberOfSamples,
                sampleCount -> createLinearSamples(sampleCount).mapToObj(value -> new Point2D(value, value)).collect(toList())
        ).stream();
    }

}
