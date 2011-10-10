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
package yaphyre.math;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

/**
 * Some useful mathematical helper functions.
 * 
 * @version $Revision$
 * 
 * @author Michael Bieri
 * @author $LastChangedBy$
 */
public class MathUtils {

  public static final double EPSILON = 1e-10;

  public static final double INV_PI = 1d / PI;

  public static final double TWO_PI = 2d * PI;

  private static final double LARGE_VAL = Double.MAX_VALUE;

  public static double div(double a, double b) {
    if (a == 0) {
      return 0;
    }
    if (b == 0) {
      return LARGE_VAL * sign(a);
    } else {
      if ((a + b) == a) {
        return LARGE_VAL * sign(a) * sign(b);
      }
      return a / b;
    }
  }

  public static double sign(double x) {
    return (x == 0) ? 0 : (x > 0) ? 1 : -1;
  }

  public static boolean equalsWithTolerance(double v1, double v2, double tolerance) {
    double diff = abs(v1 - v2);
    return diff <= tolerance;
  }

  public static boolean equalsWithTolerance(double v1, double v2) {
    return equalsWithTolerance(v1, v2, EPSILON);
  }

  public static double calcLength(double... values) {
    double lengthSquared = calculateLengthSquared(values);
    return sqrt(lengthSquared);
  }

  public static double calculateLengthSquared(double... values) {
    double result = 0d;
    for (double value : values) {
      result += value * value;
    }
    return result;
  }

}
