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

package yaphyre.app;

import com.google.inject.Exposed;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yaphyre.core.Sampler;
import yaphyre.core.Tracer;
import yaphyre.tracers.SimpleRayCaster;

import javax.annotation.Nonnull;

import static yaphyre.core.Camera.CameraSampler;
import static yaphyre.core.Light.LightSampler;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 14.09.13
 */
public class DefaultBindingModule extends PrivateModule {

    private final Logger LOGGER = LoggerFactory.getLogger(DefaultBindingModule.class);

    private final Sampler cameraSampler;
    private final Sampler lightSampler;
    private final Sampler defaultSampler;
    private final Tracer tracer;

    public DefaultBindingModule(@Nonnull Sampler cameraSampler, @Nonnull Sampler lightSampler,
                                @Nonnull Sampler defaultSampler, @Nonnull Tracer tracer) {
        this.cameraSampler = cameraSampler;
        this.lightSampler = lightSampler;
        this.defaultSampler = defaultSampler;
        this.tracer = tracer;
    }

    @Override
	protected void configure() {
	}

	@Nonnull
	@Exposed
	@Provides
	@CameraSampler
	public Sampler providesCameraSampler() {
		LOGGER.debug("Creating instance for Camera Sampler");
		return cameraSampler;
	}

	@Nonnull
	@Exposed
	@Provides
	@LightSampler
	public Sampler providesLightSampler() {
		LOGGER.debug("Creating instance for Light Sampler");
		return lightSampler;
	}

	@Nonnull
	@Exposed
	@Provides
	public Sampler providesDefaultSampler() {
		LOGGER.debug("Creating new general purpose Sampler");
		return defaultSampler;
	}

	@Nonnull
	@Exposed
	@Provides
	@Singleton
	public Tracer providesTracer() {
		LOGGER.debug("Creating instance for Tracer");
		return tracer;
	}

}
