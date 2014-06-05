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

package yaphyre.tracers;

import org.apache.commons.lang3.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yaphyre.core.CollisionInformation;
import yaphyre.core.Scene;
import yaphyre.core.Tracer;
import yaphyre.math.Color;
import yaphyre.math.MathUtils;
import yaphyre.math.Ray;
import yaphyre.shapes.SimpleSphere;
import yaphyre.shapes.Sphere;

/**
 * Straightforward implementation of a {@link yaphyre.core.Tracer}. Just some basic functionality. Use this for
 * debugging purposes only.
 *
 * @author Michael Bieri
 * @since 05.07.13
 */
public class DebuggingRayCaster implements Tracer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DebuggingRayCaster.class);

    private static final Range<Double> rayRange = Range.between(MathUtils.EPSILON, 1d / MathUtils.EPSILON);

	private static final Color BLUE = new Color(0, 0, .9d);

	private static final Color RED = new Color(.9d, 0, 0);

	private static final Color GREEN = new Color(0, .9d, 0);

	@Override
	public Color traceRay(Ray ray, Scene scene) {

		Color result;

		final CollisionInformation collisionInformation = scene.hitObject(ray, rayRange);

		if (collisionInformation == null) {
			// Sky hit
			result = BLUE;
		} else if (collisionInformation.getShape() instanceof SimpleSphere) {
			// Sphere hit
			result = RED;
		} else {
			// Other hit
			result = GREEN;
		}

		return result;
	}

}
