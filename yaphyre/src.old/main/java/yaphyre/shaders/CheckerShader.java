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
package yaphyre.shaders;

import yaphyre.core.Shader;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Transformation;
import yaphyre.util.Color;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple checker pattern. No ray tracer is complete without one ;-) This implementation uses two shader to create
 * the color of the pixel to show. So it is for example possible to create a checked pattern within the checker pattern
 * itself.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 40 $
 */
public class CheckerShader extends AbstractShader {

	private static final long serialVersionUID = -4840381886169680635L;

	private final Shader shader1, shader2;

	private final double uSize, vSize, uSizeInv, vSizeInv;

	/**
	 * Creates a simple checker with the given two shader. The frequency of the change is 1.
	 *
	 * @param shaderToObject An eventual {@link Transformation} used to map the shader coordinates into the object coordinate space.
	 * @param shader1        The first shader
	 * @param shader2        The second shader
	 */
	public CheckerShader(Transformation shaderToObject, Shader shader1, Shader shader2) {
		this(shaderToObject, shader1, shader2, 1d, 1d);
	}

	/**
	 * Create a new checker shader defined by its id, the two shader used for its tiles and the frequency with which the
	 * pattern changes.
	 *
	 * @param shaderToObject An eventual {@link Transformation} used to map the shader coordinates into the object coordinate space.
	 * @param shader1        The first shader
	 * @param shader2        The second shader
	 * @param frequency      The frequency with which the pattern changes.
	 */
	public CheckerShader(Transformation shaderToObject, Shader shader1, Shader shader2, double frequency) {
		this(shaderToObject, shader1, shader2, frequency, frequency);
	}

	/**
	 * Create a new checker shader defined by its id, the two shader used for its tiles and the frequency with which the
	 * pattern changes.
	 *
	 * @param shaderToObject An eventual {@link Transformation} used to map the shader coordinates into the object coordinate space.
	 * @param shader1        The first shader
	 * @param shader2        The second shader
	 * @param uFrequency     The frequency with which the pattern changes in the u direction.
	 * @param vFrequency     The frequency with which the pattern changes in the v direction.
	 */
	public CheckerShader(Transformation shaderToObject, Shader shader1, Shader shader2, double uFrequency,
	                     double vFrequency) {
		super(shaderToObject);
		checkArgument(uFrequency > 0d && vFrequency > 0d);
		this.shader1 = checkNotNull(shader1);
		this.shader2 = checkNotNull(shader2);
		uSizeInv = uFrequency;
		uSize = 1d / uFrequency;
		vSizeInv = vFrequency;
		vSize = 1d / vFrequency;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((shader1 == null) ? 0 : shader1.hashCode());
		result = prime * result + ((shader2 == null) ? 0 : shader2.hashCode());
		long temp = Double.doubleToLongBits(uSize);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(uSizeInv);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(vSize);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(vSizeInv);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CheckerShader)) {
			return false;
		}
		CheckerShader other = (CheckerShader) obj;
		if (shader1 == null) {
			if (other.shader1 != null) {
				return false;
			}
		} else if (!shader1.equals(other.shader1)) {
			return false;
		}
		if (shader2 == null) {
			if (other.shader2 != null) {
				return false;
			}
		} else if (!shader2.equals(other.shader2)) {
			return false;
		}
		if (Double.doubleToLongBits(uSize) != Double.doubleToLongBits(other.uSize)) {
			return false;
		}
		if (Double.doubleToLongBits(uSizeInv) != Double.doubleToLongBits(other.uSizeInv)) {
			return false;
		}
		if (Double.doubleToLongBits(vSize) != Double.doubleToLongBits(other.vSize)) {
			return false;
		}
		if (Double.doubleToLongBits(vSizeInv) != Double.doubleToLongBits(other.vSizeInv)) {
			return false;
		}
		return true;
	}

	@Override
	public Color getColor(Point2D uvCoordinate) {
		return getShaderAtCoordinate(uvCoordinate).getColor(uvCoordinate);
	}

	@Override
	public Material getMaterial(Point2D uvCoordinate) {
		return getShaderAtCoordinate(uvCoordinate).getMaterial(uvCoordinate);
	}

	private Shader getShaderAtCoordinate(Point2D uvCoordinate) {
		Point2D shaderCoordinate = super.toShaderCoordinate(uvCoordinate);
		double u = shaderCoordinate.getU();
		double v = shaderCoordinate.getV();
		u = (u >= 0) ? u : u - uSize;
		v = (v > +0) ? v : v - vSize;
		boolean xEven = ((int) (u * uSizeInv)) % 2 == 0;
		boolean yEven = ((int) (v * vSizeInv)) % 2 == 0;

		if (xEven == yEven) {
			return shader1;
		}
		return shader2;
	}

}
