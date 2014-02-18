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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import yaphyre.core.Sampler;
import yaphyre.math.Point2D;
import yaphyre.math.Point3D;

/**
 * Base implementation for all Samplers.
 *
 * @author Michael Bieri
 * @since 18.02.14
 */
public abstract class AbstractSampler implements Sampler {

    @Nonnull
    @Override
    public Iterable<Point2D> getUnitCircleSamples() {
        return null;
    }

    @Nonnull
    @Override
    public Iterable<Point3D> getUnitSphereSamples() {
        return null;
    }

    @Nonnull
    @Override
    public Iterable<Point3D> getUnitHemisphereSamples(@Nonnegative double cosinePower) {
        return null;
    }

    protected abstract int getSampleCount();

}
