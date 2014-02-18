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

package yaphyre.samplers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import yaphyre.math.Point2D;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 18.02.14
 */
public class RegularSampler extends AbstractSampler {

    private final List<Double> samples;

    private final List<Point2D> unitSquareSamples;

    public RegularSampler(int numberOfSamples) {
        samples = Collections.unmodifiableList(createValueSamples(numberOfSamples));
        unitSquareSamples = Collections.unmodifiableList(createUnitSquareSamples(samples));
    }

    @Nonnull
    @Override
    public Iterable<Double> getSamples() {
        return samples;
    }

    @Nonnull
    @Override
    public Iterable<Point2D> getUnitSquareSamples() {
        return unitSquareSamples;
    }

    @Override
    protected int getSampleCount() {
        return samples.size();
    }

    //region Private Helpers

    private List<Point2D> createUnitSquareSamples(List<Double> samples) {
        List<Point2D> backingPointSamples = new ArrayList<>();
        for (Double sample : samples) {
            backingPointSamples.add(new Point2D(sample, sample));
        }
        return backingPointSamples;
    }

    private List<Double> createValueSamples(int numberOfSamples) {
        List<Double> backingSamples = new ArrayList<>();
        final double stepSize = 1d / numberOfSamples;
        final double start = stepSize / 2d;
        for (int sampleCount = 0; sampleCount < numberOfSamples; sampleCount++) {
            final double sample = start + sampleCount * stepSize;
            backingSamples.add(sample);
        }
        return backingSamples;
    }

    //endregion
}
