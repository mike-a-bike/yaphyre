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

package yaphyre.math;

import com.google.common.base.Objects;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

import static com.google.common.primitives.Doubles.max;
import static com.google.common.primitives.Doubles.min;
import static java.lang.Math.pow;

/**
 * Represents a color by its three components of red, green and blue with a value between zero and one. Each instance
 * of {@link yaphyre.math.Color} is immutable. Some simple operations are provided as for example adding two color, multiplying
 * colors with each other and with a scalar value.<br/>
 * Each component is represented by a double value. So it is
 * possible that the values may become bigger than 1 or smaller than 0. To use such a color in another context it is
 * necessary to make sure that the values are within the allowed range. To do this, this class provides a helper method
 * called {@link yaphyre.math.Color#clip()}.
 *
 * @author Michael Bieri
 */
@Immutable
public class Color implements Serializable {

	private static final long serialVersionUID = 6104986207165257901L;

	/**
	 * Represent the value of black.
	 */
	public static final Color BLACK = new Color(0d, 0d, 0d);

	/**
	 * White
	 */
	public static final Color WHITE = new Color(1d, 1d, 1d);

	private final double red;

	private final double green;

	private final double blue;

	public static double fromByteValue(int byteValue) {
		return byteValue / 255d;
	}

	public static int toByteValue(double doubleValue) {
		return (int) (doubleValue * 255d);
	}

	public Color(java.awt.Color awtColor) throws NullPointerException {
		this(fromByteValue(awtColor.getRed()), fromByteValue(awtColor.getGreen()), fromByteValue(awtColor.getBlue()));
	}

	public Color(double r, double g, double b) {
		red = r;
		green = g;
		blue = b;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("r", red).add("g", green).add("b", blue).toString();
	}

	/**
	 * Compares two instances. If the instance to compare to is a {@link yaphyre.math.Color}, non-null and each its three color
	 * components has the same value as this instance, the two {@link yaphyre.math.Color}s are equal.
	 *
	 * @return <code>true</code> if both {@link yaphyre.math.Color} instances represent the same red, green and blue values.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Color) {
			final Color color = (Color) obj;
			return color.red == red && color.green == green && color.blue == blue;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(red, green, blue);
	}

	public double getRed() {
		return red;
	}

	public double getGreen() {
		return green;
	}

	public double getBlue() {
		return blue;
	}

	/**
	 * Multiply this color with another color. It does this by multiplying the separate components with each other.
	 *
	 * @param other The {@link yaphyre.math.Color} to multiply the values with.
	 *
	 * @return A new instance with the multiplied values.
	 */
	public Color multiply(Color other) {
		return new Color(red * other.getRed(), green * other.getGreen(), blue * other.getBlue());
	}

	/**
	 * Multiply the values of this color with a scalar. Thus 'scaling' the color value.
	 *
	 * @param factor The value to multiply each color value with.
	 *
	 * @return A new instance with the multiplied values.
	 */
	public Color multiply(double factor) {
		return new Color(red * factor, green * factor, blue * factor);
	}

	/**
	 * Add another {@link yaphyre.math.Color} to this {@link yaphyre.math.Color}. Adds each component separately and creates a new color with these
	 * values.
	 *
	 * @param other The {@link yaphyre.math.Color} to add to this instance.
	 *
	 * @return A new instance of {@link yaphyre.math.Color} with the added values.
	 */
	public Color add(Color other) {
		return new Color(red + other.red, green + other.green, blue + other.blue);
	}

	/**
	 * When doing calculations with a color it can happen that the range between zero and one is exceeded. To use such a
	 * color in another context it is necessary to make sure that the value for the color components are within the
	 * allowed boundaries. Otherwise clipping can occur.<br/> By clipping the color values it ensures that values smaller
	 * than zero are set to zero and values bigger than one are set to one. Any other values are left as they are.
	 *
	 * @return A new instance of {@link yaphyre.math.Color} with each component value between zero and one.
	 */
	public Color clip() {
		double r = clipValue(red);
		double g = clipValue(green);
		double b = clipValue(blue);
		return new Color(r, g, b);
	}

	/**
	 * If a value for one of the three color components exceeds the range of zero to one all the components are scaled so
	 * that the biggest value is one and all the others are scaled accordingly.
	 *
	 * @return A new instance of {@link yaphyre.math.Color} with the scaled values.
	 */
	public Color rescale() {
		double maxValue = max(red, green, blue);
		if (maxValue > Double.MIN_VALUE) {
			return multiply(1 / maxValue);
		}
		return this;
	}

	private static double clipValue(double value) {
		return max(0d, min(1d, value));
	}

	/**
	 * Creates a new color instance with each component raised to the power of <code>pow</code>. This can be used for
	 * gamma correction.
	 *
	 * @param pow The power to which each component is raised.
	 *
	 * @return A new {@link yaphyre.math.Color} with the scaled values.
	 */
	public Color powc(double pow) {
		return new Color(pow(red, pow), pow(green, pow), pow(blue, pow));
	}

}
