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

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import com.google.common.collect.Lists;
import org.apache.commons.math3.random.HaltonSequenceGenerator;
import org.apache.commons.math3.random.RandomVectorGenerator;
import yaphyre.core.math.Point2D;

/**
 * A sampler using the halton algorithm. This creates random samples which are evenly distributed over the range.
 *
 * @author axmbi03
 * @since 30.06.2014
 */
public class HaltonSampler extends AbstractSampler {

    private List<Double> linearSamples;
    private List<Point2D> unitSquareSamples;

    public HaltonSampler(int numberOfSamples) {
        super(numberOfSamples);
        setupSampler(numberOfSamples);
    }

    private void setupSampler(int numberOfSamples) {
        RandomVectorGenerator sampleGenerator = new HaltonSequenceGenerator(1);
        RandomVectorGenerator vectorGenerator = new HaltonSequenceGenerator(2);
        linearSamples = Lists.newArrayListWithCapacity(numberOfSamples);
        unitSquareSamples = Lists.newArrayListWithCapacity(numberOfSamples);
        for (int i = 0; i < numberOfSamples; i++) {
            linearSamples.add(sampleGenerator.nextVector()[0]);
            unitSquareSamples.add(Point2D.of(vectorGenerator.nextVector()));
        }
        linearSamples = Collections.unmodifiableList(linearSamples);
        unitSquareSamples = Collections.unmodifiableList(unitSquareSamples);
    }

    @Nonnull
    @Override
    protected Stream<Point2D> createUnitSquareSamples(int numberOfSamples) {
        return unitSquareSamples.stream();
    }

}
