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
import java.util.Random;
import javax.annotation.Nonnull;
import yaphyre.math.Point2D;

/**
 * Create stratified samples. The samples are based on the regular sampler which are jittered in a second step.
 *
 * @author axmbi03
 * @since 19.02.14
 */
public class StratifiedSampler extends AbstractSampler {

    private static final Random RANDOM = new Random();

    public StratifiedSampler(int numberOfSamples) {
        super(numberOfSamples);
    }

    @Nonnull
    @Override
    protected List<Double> createLinearSamples(int numberOfSamples) {
        List<Double> result = createStratifiedSamples(numberOfSamples);
        Collections.shuffle(result, RANDOM);
        return result;
    }

    @Nonnull
    @Override
    protected List<Point2D> createUnitSquareSamples(int numberOfSamples) {
        List<Point2D> pointSamples = new ArrayList<>();
        List<Double> uSamples = createStratifiedSamples(numberOfSamples);
        List<Double> vSamples = createStratifiedSamples(numberOfSamples);
        for (int sampleIndex = 0; sampleIndex < uSamples.size(); sampleIndex++) {
            pointSamples.add(new Point2D(uSamples.get(sampleIndex), vSamples.get(sampleIndex)));
        }
        Collections.shuffle(pointSamples, RANDOM);
        return pointSamples;
    }

    private List<Double> createStratifiedSamples(int numberOfSamples) {
        List<Double> result = new ArrayList<>();
        double stepSize = 1d / numberOfSamples;
        double stepSizeHalf = stepSize / 2d;
        for (int sampleIndex = 0; sampleIndex < numberOfSamples; sampleIndex++) {
            double sample = stepSizeHalf + sampleIndex * stepSize;
            double offset = RANDOM.nextDouble() * stepSize - stepSizeHalf;
            result.add(sample + offset);
        }
        return result;
    }
}
