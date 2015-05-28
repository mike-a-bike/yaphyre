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

package yaphyre.core.api;

import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import yaphyre.core.math.Point2D;
import yaphyre.core.math.Point3D;

/**
 * Common interface for all samplers. The idea is that it can deliver continuously values within the range (0,1). If
 * all samples would be taken together, a perfect distribution would be achieved. This is of course not possible,
 * so most implementation have a limited number of samples (which may be pre-calculated) which represent an
 * approximation of a continuous stream of values.<br/>
 * In addition this class also provides access to a set of specific representations like samples in a unit square, a
 * unit disc, a unit sphere and hemisphere. The hemisphere can be parametrised using a cosine distribution. The
 * necessary power factor is part of the call to get the samples.
 *
 * @author Michael Bieri
 * @since 27.07.13
 */
public interface Sampler {

    /**
     * @return An Iterable representing samples within the unit square.
     */
    @Nonnull
    public Stream<Point2D> getUnitSquareSamples();

    /**
     * Access a collection of samples within a unit circle.
     *
     * @return An Iterable of samples within the unit circle.
     */
    @Nonnull
    public Stream<Point2D> getUnitCircleSamples();

    /**
     * Get a collection of samples lying on the surface of the unit sphere.
     *
     * @return An Iterable of samples on the unit sphere.
     */
    @Nonnull
    public Stream<Point3D> getUnitSphereSamples();

    @Nonnull
    public Stream<Point3D> getUnitHemisphereSamples(@Nonnegative double cosinePower);

}
