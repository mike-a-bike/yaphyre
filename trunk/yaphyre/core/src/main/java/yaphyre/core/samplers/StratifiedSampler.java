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
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import yaphyre.core.math.Point2D;

/**
 * Create stratified samples. The samples are based on the regular sampler which are jittered in a second step.
 *
 * @author axmbi03
 * @since 19.02.14
 */
public class StratifiedSampler extends AbstractSampler {

    private static final Random RANDOM = new Random();

    private static final Map<Integer, double[]> STRATIFIED_SAMPLES = new HashMap<>();
    private static final Map<Integer, List<Point2D>> STRATIFIED_POINT_SAMPLES = new HashMap<>();

    public StratifiedSampler(int numberOfSamples) {
        super(numberOfSamples);
    }

    @Nonnull
    @Override
    protected Stream<Point2D> createUnitSquareSamples(int numberOfSamples) {
        return STRATIFIED_POINT_SAMPLES.computeIfAbsent(numberOfSamples,
            n -> {
                double[] uSamples = createStratifiedSamples(n);
                double[] vSamples = createStratifiedSamples(n);
                return IntStream.range(0, n)
                    .mapToObj(i -> new Point2D(uSamples[i], vSamples[i]))
                    .collect(Collectors.toList());
            }
        ).stream();
    }

    private double[] createStratifiedSamples(int numberOfSamples) {
        return STRATIFIED_SAMPLES.computeIfAbsent(numberOfSamples, n -> {
            double stepSize = 1d / n;
            double stepSizeHalf = stepSize / 2d;

            double[] result = new double[n];
            for (int index = 0; index < n; index++) {
                result[index] = stepSizeHalf + index * stepSize + (RANDOM.nextDouble() * stepSize - stepSizeHalf);
            }

            return result;
        });
    }
}
