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

package yaphyre.cameras;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import yaphyre.core.Camera;
import yaphyre.core.Sampler;
import yaphyre.core.Tracer;
import yaphyre.math.Point2D;
import yaphyre.math.Ray;

/**
 * A generic camera base class. Most of the implemented cameras will use this base class. It contains a
 * {@link yaphyre.core.Sampler} for use in Montecarlo sampling as well as a {@link yaphyre.core.Tracer} for integrating
 * a camera ray.
 * Both instance cannot be null.
 *
 * @author Michael Bieri
 * @since 08.09.13
 */
public abstract class AbstractCamera implements Camera {

    /**
     * Tracer used for integrating a camera ray.
     */
    private Tracer tracer;
    /**
     * Camera sampler
     */
    private Sampler sampler;

    @Nonnull
    public Sampler getSampler() {
        return sampler;
    }

    @Inject
    public void setSampler(@CameraSampler @Nonnull Sampler sampler) {
        this.sampler = sampler;
    }

    @Nonnull
    public Tracer getTracer() {
        return tracer;
    }

    @Inject
    public void setTracer(@Nonnull Tracer tracer) {
        this.tracer = tracer;
    }

    /**
     * Create a sample ray representing a 'looking' ray for the given sample point. The range of the samples is [0,1).
     * @param samplePoint The point to create the sample ray for. Must not be null
     * @return A corresponding ray starting at the camera.
     */
    @Nonnull
    protected abstract Ray createCameraRay(@Nonnull Point2D samplePoint);
}
