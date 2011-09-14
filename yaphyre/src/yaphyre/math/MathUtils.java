package yaphyre.math;

public class MathUtils {

  private static final double LARGE_VAL = 1e100;

  public static double max(double v1, double v2, double v3) {
    return Math.max(v1, Math.max(v2, v3));
  }

  public static double min(double v1, double v2, double v3) {
    return Math.min(v1, Math.min(v2, v3));
  }

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

}
