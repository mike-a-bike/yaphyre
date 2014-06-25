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

package yaphyre.core.tracers;

import yaphyre.core.api.Scene;
import yaphyre.core.api.Tracer;
import yaphyre.core.math.Color;
import yaphyre.core.math.Ray;
import yaphyre.core.shapes.SimpleSphere;

/**
 * Straightforward implementation of a {@link yaphyre.core.api.Tracer}. Just some basic functionality. Use this for
 * debugging purposes only.
 *
 * @author Michael Bieri
 * @since 05.07.13
 */
public class DebuggingRayCaster implements Tracer {

	private static final Color BLUE = new Color(0, 0, .9d);
	private static final Color RED = new Color(.9d, 0, 0);
	private static final Color GREEN = new Color(0, .9d, 0);

	@Override
	public Color traceRay(Ray ray, Scene scene) {
        return scene.hitObject(ray).map((c) -> (c.getShape() instanceof SimpleSphere) ? RED : GREEN).orElse(BLUE);
	}

}
