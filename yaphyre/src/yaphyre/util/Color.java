/*
 * Copyright 2011 Michael Bieri
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
package yaphyre.util;

import static com.google.common.primitives.Doubles.max;
import static com.google.common.primitives.Doubles.min;

import com.google.common.base.Objects;

/**
 * Represents a color by its three components of red, green and blue with a
 * value between zero and one. Each instance of {@link Color} is immutable. Some
 * simple operations are provided as for example adding two color, multiplying
 * colors with each other and with a scalar value.<br/>
 * Each component is represented by a double value. So it is possible that the
 * values may become bigger than 1 or smaller than 0. To use such a color in
 * another context it is necessary to make sure that the values are within the
 * allowed range. To do this, this class provides a helper method called
 * {@link Color#clip()}.
 * 
 * @author Michael Bieri
 */
public class Color {

  /** Represent the value of black. */
  public static final Color BLACK = new Color(0d, 0d, 0d);

  private final double red;

  private final double green;

  private final double blue;

  public static double fromByteValue(int byteValue) {
    return byteValue / 255d;
  }

  public static int toByteValue(double doubleValue) {
    return (int)(doubleValue * 255d);
  }

  public Color(java.awt.Color awtColor) throws NullPointerException {
    this(fromByteValue(awtColor.getRed()), fromByteValue(awtColor.getGreen()), fromByteValue(awtColor.getBlue()));
  }

  public Color(double r, double g, double b) {
    this.red = r;
    this.green = g;
    this.blue = b;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this).add("r", this.red).add("g", this.green).add("b", this.blue).toString();
  }

  /**
   * Compares two instances. If the instance to compare to is a {@link Color},
   * non-null and each its three color components has the same value as this
   * instance, the two {@link Color}s are equal.
   * 
   * @return <code>true</code> if both {@link Color} instances represent the
   *         same red, green and blue values.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof Color) {
      Color color = (Color)obj;
      return color.red == this.red && color.green == this.green && color.blue == this.blue;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.red, this.green, this.blue);
  }

  public double getRed() {
    return this.red;
  }

  public double getGreen() {
    return this.green;
  }

  public double getBlue() {
    return this.blue;
  }

  /**
   * Multiply this color with another color. It does this by multiplying the
   * separate components with each other.
   * 
   * @param other
   *          The {@link Color} to multiply the values with.
   * 
   * @return A new instance with the multiplied values.
   */
  public Color multiply(Color other) {
    return new Color(this.red * other.getRed(), this.green * other.getGreen(), this.blue * other.getBlue());
  }

  /**
   * Multiply the values of this color with a scalar. Thus 'scaling' the color
   * value.
   * 
   * @param factor
   *          The value to multiply each color value with.
   * 
   * @return A new instance with the multiplied values.
   */
  public Color multiply(double factor) {
    return new Color(this.red * factor, this.green * factor, this.blue * factor);
  }

  /**
   * Add another {@link Color} to this {@link Color}. Adds each component
   * separately and creates a new color with these values.
   * 
   * @param other
   *          The {@link Color} to add to this instance.
   * 
   * @return A new instance of {@link Color} with the added values.
   */
  public Color add(Color other) {
    return new Color(this.red + other.red, this.green + other.green, this.blue + other.blue);
  }

  /**
   * When doing calculations with a color it can happen that the range between
   * zero and one is exceeded. To use such a color in another context it is
   * necessary to make sure that the value for the color components are within
   * the allowed boundaries. Otherwise clipping can occur.<br/>
   * By clipping the color values it ensures that values smaller than zero are
   * set to zero and values bigger than one are set to one. Any other values are
   * left as they are.
   * 
   * @return A new instance of {@link Color} with each component value between
   *         zero and one.
   */
  public Color clip() {
    double r = Color.clipValue(this.red);
    double g = Color.clipValue(this.green);
    double b = Color.clipValue(this.blue);
    return new Color(r, g, b);
  }

  /**
   * If a value for one of the three color components exceeds the range of zero
   * to one all the components are scaled so that the biggest value is one and
   * all the others are scaled accordingly.
   * 
   * @return A new instance of {@link Color} with the scaled values.
   */
  public Color rescale() {
    double maxValue = max(this.red, this.green, this.blue);
    if (maxValue > Double.MIN_VALUE) {
      return this.multiply(1 / maxValue);
    }
    return this;
  }

  private static double clipValue(double value) {
    return max(0d, min(1d, value));
  }

}
