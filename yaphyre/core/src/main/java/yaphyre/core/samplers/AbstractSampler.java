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

import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yaphyre.core.api.Sampler;
import yaphyre.core.math.Point2D;
import yaphyre.core.math.Point3D;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.util.stream.Collectors.toList;

/**
 * Base implementation for all Samplers.
 *
 * @author Michael Bieri
 * @since 18.02.14
 */
public abstract class AbstractSampler implements Sampler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSampler.class);

    private final int numberOfSamples;

    private List<Point2D> discSamples;
    private List<Point3D> sphereSamples;

    public AbstractSampler(int numberOfSamples) {
        this.numberOfSamples = numberOfSamples;
    }

    @Nonnull
    @Override
    public Stream<Point2D> getUnitSquareSamples() {
        return createUnitSquareSamples(numberOfSamples);
    }

    /**
     * {@inheritDoc}<br/>
     * Maps the unit square coordinates onto a disc. It uses an algorithm which produces an even distribution from
     * the samples.
     */
    @Nonnull
    @Override
    public Stream<Point2D> getUnitCircleSamples() {
        if (discSamples == null) {
            discSamples = createUnitSquareSamples(numberOfSamples)
                    .peek(p -> LOGGER.trace("converting: {}", p))
                    .map(AbstractSampler::mapUnitSquarePointToUnitDisc)
                    .collect(toList());
        }
        return discSamples.stream();
    }

    private static Point2D mapUnitSquarePointToUnitDisc(Point2D p) {
        final double r1 = p.getU() * 2d - 1d;
        final double r2 = p.getV() * 2d - 1d;
        final double PI4th = PI / 4d;
        final double radius;
        final double phi;
        if (r1 > -r2 && r1 > r2) {
            radius = r1;
            phi = PI4th * (r2 / r1);
        } else if (r1 < r2 && r1 > -r2) {
            radius = r2;
            phi = PI4th * (2d - r1 / r2);
        } else if (r1 < -r2 && r1 > r2) {
            radius = -r1;
            phi = PI4th * (4d + r2 / r1);
        } else {
            radius = -r2;
            phi = PI4th * (6d - r1 / r2);
        }
        return new Point2D(radius * sin(phi), radius * cos(phi));
    }

    @Nonnull
    @Override
    public Stream<Point3D> getUnitSphereSamples() {
        if (sphereSamples == null) {
            sphereSamples = createUnitSquareSamples(numberOfSamples)
                    .peek(p -> LOGGER.trace("converting: {}", p))
                    .map(AbstractSampler::mapUnitSquarePointToUnitSphere)
                    .collect(toList());
        }
        return sphereSamples.stream();
    }

    private static Point3D mapUnitSquarePointToUnitSphere(Point2D p) {
        // TODO REVIEW IMPLEMENTATION: IS MOST CERTAINLY WRONG...
        final double phi = p.getU() * 2d * PI;
        final double theta = p.getV() * PI;
        final double cosPhi = cos(phi);
        return new Point3D(cosPhi * cos(theta), cosPhi * sin(theta), sin(phi));
    }

    @Nonnull
    @Override
    public Stream<Point3D> getUnitHemisphereSamples(@Nonnegative double cosinePower) {
        return createUnitSquareSamples(numberOfSamples)
                .map((p) -> Point3D.ORIGIN);
    }

    @Nonnull
    protected abstract Stream<Point2D> createUnitSquareSamples(int numberOfSamples);

}
