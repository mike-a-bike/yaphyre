/*
 * Copyright 2012 Michael Bieri
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package yaphyre.core;

import yaphyre.geometry.MathUtils;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector3D;
import yaphyre.raytracer.Scene;

import org.jetbrains.annotations.NotNull;

/**
 * If Java had closures, this would be one. It contains the check whether a sampling point is visible from a given
 * second point in space. The idea is that this calculation and using this construct may be postponed until is is
 * necessary.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 96 $
 */
public class VisibilityTester {

	private final Ray ray;

	public VisibilityTester(@NotNull final Point3D p1, @NotNull final Point3D p2) {
		Vector3D connectingVector = p2.sub(p1);
		ray = new Ray(p1, connectingVector.normalize(), MathUtils.EPSILON,
				connectingVector.length() - MathUtils.EPSILON);
	}

	public VisibilityTester(@NotNull final Point3D p, @NotNull final Vector3D w) {
		ray = new Ray(p, w.normalize());
	}

	public boolean isUnobstructed(@NotNull Scene scene) {
		return scene.getCollidingShape(ray, ray.getMaxt(), true) == null;
	}

}
