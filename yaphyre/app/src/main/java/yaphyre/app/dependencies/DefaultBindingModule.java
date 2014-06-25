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

package yaphyre.app.dependencies;

import java.util.function.Supplier;
import com.google.inject.Exposed;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yaphyre.core.api.Sampler;
import yaphyre.core.api.Tracer;

import javax.annotation.Nonnull;

import static yaphyre.core.api.Camera.CameraSampler;
import static yaphyre.core.api.Light.LightSampler;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 14.09.13
 */
public class DefaultBindingModule extends PrivateModule {

    private final Logger LOGGER = LoggerFactory.getLogger(DefaultBindingModule.class);

    private final Supplier<Sampler> cameraSamplerSupplier;
    private final Supplier<Sampler> lightSamplerSupplier;
    private final Supplier<Sampler> defaultSamplerSupplier;
    private final Tracer tracer;

    public DefaultBindingModule(@Nonnull Supplier<Sampler> cameraSamplerSupplier,
                                @Nonnull Supplier<Sampler> lightSamplerSupplier,
                                @Nonnull Supplier<Sampler> defaultSamplerSupplier,
                                @Nonnull Tracer tracer) {
        this.cameraSamplerSupplier = cameraSamplerSupplier;
        this.lightSamplerSupplier = lightSamplerSupplier;
        this.defaultSamplerSupplier = defaultSamplerSupplier;
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
		return cameraSamplerSupplier.get();
	}

	@Nonnull
	@Exposed
	@Provides
	@LightSampler
	public Sampler providesLightSampler() {
		LOGGER.debug("Creating instance for Light Sampler");
		return lightSamplerSupplier.get();
	}

	@Nonnull
	@Exposed
	@Provides
	public Sampler providesDefaultSampler() {
		LOGGER.debug("Creating new general purpose Sampler");
		return defaultSamplerSupplier.get();
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
