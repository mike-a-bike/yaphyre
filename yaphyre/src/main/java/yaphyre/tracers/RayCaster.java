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
import yaphyre.core.CollisionInformation;
import yaphyre.core.Scene;
import yaphyre.core.Tracer;
import yaphyre.math.Color;
import yaphyre.math.MathUtils;
import yaphyre.math.Ray;
import yaphyre.shapes.Sphere;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 05.07.13
 */
public class RayCaster implements Tracer {

	private static final Range<Double> rayRange = Range.between(MathUtils.EPSILON, 1d / MathUtils.EPSILON);

	private static final Color BLUE = new Color(0, 0, 1);

	private static final Color RED = new Color(1, 0, 0);

	private static final Color GREEN = new Color(0, 1, 0);

	@Override
	public Color traceRay(Ray ray, Scene scene) {

		Color result;

		final CollisionInformation collisionInformation = scene.hitObject(ray, rayRange);

		if (collisionInformation == null) {
			// Sky hit
			result = BLUE;
		} else if (collisionInformation.getShape() instanceof Sphere) {
			// Sphere hit
			result = RED;
		} else {
			// Plane hit
			result = GREEN;
		}

		return result;
	}

}
