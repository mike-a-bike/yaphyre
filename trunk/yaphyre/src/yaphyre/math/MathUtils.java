package yaphyre.math;

import static java.lang.Math.PI;

public class MathUtils {

  public static final double EPSILON = 1e-10;

  public static final double DEG_TO_RAD = 1d / 360d * 2d * PI;

  public static final double RAD_TO_DEG = 1d / 2d / PI * 360d;

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

  public static double toRad(double deg) {
    return deg * DEG_TO_RAD;
  }

  public static double toDeg(double rad) {
    return rad * RAD_TO_DEG;
  }

}