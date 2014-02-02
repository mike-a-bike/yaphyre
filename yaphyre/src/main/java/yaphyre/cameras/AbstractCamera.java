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

import yaphyre.core.Camera;
import yaphyre.core.Sampler;
import yaphyre.core.Tracer;
import yaphyre.math.Point2D;
import yaphyre.math.Ray;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 08.09.13
 */
public abstract class AbstractCamera implements Camera {

	private Tracer tracer;
	private Sampler sampler;

	public Sampler getSampler() {
		return sampler;
	}

	@Inject
	public void setSampler(@CameraSampler @Nonnull Sampler sampler) {
		this.sampler = sampler;
	}

	public Tracer getTracer() {
		return tracer;
	}

	@Inject
	public void setTracer(@Nonnull Tracer tracer) {
		this.tracer = tracer;
	}

	protected abstract Ray createCameraRay(@Nonnull Point2D samplePoint);
}
