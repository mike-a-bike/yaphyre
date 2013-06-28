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
package yaphyre.lights;

import yaphyre.core.LightSample;
import yaphyre.core.Lightsource;
import yaphyre.core.VisibilityTester;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Transformation;
import yaphyre.geometry.Vector3D;
import yaphyre.util.Color;

import static java.lang.Math.PI;

/**
 * A simple point light. The characteristics of this {@link Lightsource} is, that is has no physical size and radiates
 * uniformly in all directions. Since it is infinitesimally small, it is a so called delta light source, which means,
 * that the energy distribution is a delta function. It cannot be sampled by using a purely random approach, since it
 * cannot be hit by any ray.
 *
 * @author Michael Bieri
 * @author $LastChangedBy$
 * @version $Revision$
 */
public class Pointlight extends Lightsource {

	private static final long serialVersionUID = -1976888619913693137L;

	/**
	 * The position of the point light. This is pre-calculated for convenience reasons.
	 */
	private final Point3D position;

	public Pointlight(Transformation l2w, Color color, double intensity) {
		super(l2w, color, intensity, 1);
		position = super.getLight2World().transform(Point3D.ORIGIN);
	}

	@Override
	public LightSample sample(Point3D point) {
		double distanceSquared = point.sub(position).lengthSquared();
		Color energy = getColor().multiply(getIntensity() / distanceSquared);
		Vector3D wi = position.sub(point).normalize();
		VisibilityTester visibilityTester = new VisibilityTester(point, position);
		return new LightSample(energy, wi, visibilityTester);
	}

	/**
	 * The pointlight emits the same amount of energy in all directions. So the total energy is integrated over a spheres
	 * surface.
	 */
	@Override
	public Color getTotalEnergy() {
		return super.getColor().multiply(super.getIntensity() * 4d * PI);
	}

	@Override
	public boolean isDeltaLight() {
		return true;
	}
}
