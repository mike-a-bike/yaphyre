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

import java.util.ArrayList;
import java.util.List;
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

    public RegularSampler(int numberOfSamples) {
        super(numberOfSamples);
    }

    @Nonnull
    @Override
    protected List<Double> createLinearSamples(int numberOfSamples) {
        List<Double> backingSamples = new ArrayList<>();
        final double stepSize = 1d / numberOfSamples;
        final double start = stepSize / 2d;
        for (int sampleCount = 0; sampleCount < numberOfSamples; sampleCount++) {
            final double sample = start + sampleCount * stepSize;
            backingSamples.add(sample);
        }
        return backingSamples;
    }

    @Nonnull
    @Override
    protected List<Point2D> createUnitSquareSamples(int numberOfSamples) {

        return createLinearSamples(numberOfSamples).stream()
                                                   .map((d) -> new Point2D(d, d))
                                                   .collect(toList());

    }

}
