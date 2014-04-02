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

import com.google.common.collect.Lists;
import yaphyre.core.Sampler;
import yaphyre.math.Point2D;
import yaphyre.math.Point3D;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Base implementation for all Samplers.
 *
 * @author Michael Bieri
 * @since 18.02.14
 */
public abstract class AbstractSampler implements Sampler {

    private List<Double> linearSamples;

    private List<Point2D> pointSamples;

    public AbstractSampler(int numberOfSamples) {
        linearSamples = Collections.unmodifiableList(createLinearSamples(numberOfSamples));
        pointSamples = Collections.unmodifiableList(createUnitSquareSamples(numberOfSamples));
    }

    @Nonnull
    @Override
    public Iterable<Double> getSamples() {
        return linearSamples;
    }

    @Nonnull
    @Override
    public Iterable<Point2D> getUnitSquareSamples() {
        return pointSamples;
    }

    @Nonnull
    @Override
    public Iterable<Point2D> getUnitCircleSamples() {
        return Collections.unmodifiableList(
            Lists.newArrayList(
                pointSamples.stream().map(
                    (p) -> {
                        final double radius = p.getU();
                        final double phi = p.getV() * 2d * PI;
                        return new Point2D(radius * sin(phi), radius * cos(phi));
                    }
                ).iterator()
            )
        );
    }

    @Nonnull
    @Override
    public Iterable<Point3D> getUnitSphereSamples() {
        return Collections.unmodifiableList(
            Lists.newArrayList(
                pointSamples.stream().map(
                    (p) -> {
                        final double phi = p.getU() * 2d * PI;
                        final double theta = p.getV() * PI;
                        return new Point3D(cos(phi) * cos(theta), cos(phi) * sin(theta), sin(phi));
                    }
                ).iterator()
            )
        );
    }

    @Nonnull
    @Override
    public Iterable<Point3D> getUnitHemisphereSamples(@Nonnegative double cosinePower) {
        return null;
    }

    @Override
    public void shuffle() {
        linearSamples = shuffleCollection(linearSamples);
        pointSamples = shuffleCollection(pointSamples);
    }

    private <T> List<T> shuffleCollection(List<T> collection) {
        List<T> shuffledCollection = new ArrayList<>(collection);
        Collections.shuffle(shuffledCollection);
        return Collections.unmodifiableList(shuffledCollection);
    }

    @Nonnull
    protected abstract List<Double> createLinearSamples(int numberOfSamples);

    @Nonnull
    protected abstract List<Point2D> createUnitSquareSamples(int numberOfSamples);

}
