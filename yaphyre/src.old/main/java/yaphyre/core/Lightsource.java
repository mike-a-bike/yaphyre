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

import com.google.common.base.Objects;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Transformation;
import yaphyre.util.Color;

import java.io.Serializable;

/**
 * Common interface for all light sources in the rendering system.
 *
 * @author Michael Bieri
 * @author $LastChangedBy$
 * @version $Revision$
 */
public abstract class Lightsource implements Serializable {

	private static final long serialVersionUID = -6028978583820615734L;

	private final Transformation l2w, w2l;

	private final double intensity;

	private final Color color;

	private final int numberOfSamples;

	public Lightsource(Transformation l2w, Color color, double intensity, int numberOfSamples) {
		this.l2w = l2w;
		w2l = l2w.inverse();
		this.color = color;
		this.intensity = intensity;
		this.numberOfSamples = numberOfSamples;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(getClass()).add("intensity", color.multiply(intensity)).add("l2w", l2w).add(
				"samples", numberOfSamples).add("delta light", isDeltaLight()).toString();
	}

	/**
	 * Samples the light properties for the given {@link Point3D}. The resulting {@link LightSample} contains the
	 * information about the incident light ray direction, the intensity of the light and the yet to solve visibility of
	 * the lightsource from the given points view.
	 *
	 * @param point The {@link Point3D} to sample the light from.
	 *
	 * @return A {@link LightSample} instance containing all the necessary information about this light seen from the
	 *         given point.
	 */
	public abstract LightSample sample(Point3D point);

	/**
	 * Gets the total emitted light energy.
	 *
	 * @return A {@link Color} instance scaled to the extent of the lights intensity.
	 */
	public abstract Color getTotalEnergy();

	protected Transformation getLight2World() {
		return l2w;
	}

	protected Transformation getWorld2Light() {
		return w2l;
	}

	protected double getIntensity() {
		return intensity;
	}

	public Color getColor() {
		return color;
	}

	public boolean isDeltaLight() {
		return false;
	}

	public int getNumberOfSamples() {
		return numberOfSamples;
	}

}
