/*
 * Copyright 2013 Michael Bieri
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

import yaphyre.geometry.Vector3D;
import yaphyre.util.Color;

/**
 * Collects all the information needed for a specific point in space concerning a lightsource. Like the incident
 * direction of the light, its arriving energy and a closure used to calculate whether the light is unobstructed seen
 * from the associated point or not.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 96 $
 */
public class LightSample {

	private final Color energy;

	private final Vector3D wi;

	private final VisibilityTester visibilityTester;

	public LightSample(Color energy, Vector3D wi, VisibilityTester visibilityTester) {
		this.energy = energy;
		this.wi = wi;
		this.visibilityTester = visibilityTester;
	}

	/**
	 * Gets the energy of the light arriving at the sampling point.
	 *
	 * @return The light color scaled by the arriving intensity.
	 */
	public Color getEnergy() {
		return energy;
	}

	/**
	 * The direction of the incident ray from where the light arrives.
	 *
	 * @return A {@link Vector3D} indicating the direction from where the light arrives.
	 */
	public Vector3D getIncidentDirection() {
		return wi;
	}

	/**
	 * A Clojure containing the yet to be evaluated information about whether or not the sampling point is visible from
	 * the lightsource or not.
	 *
	 * @return A {@link VisibilityTester} instance which can evaluate whether the light is obstructed from the sampling
	 *         point or not.
	 */
	public VisibilityTester getVisibilityTester() {
		return visibilityTester;
	}

}
