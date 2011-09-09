package yaphyre.math;

public class MathUtils {

  public static double max(double v1, double v2, double v3) {
    return Math.max(v1, Math.max(v2, v3));
  }

  public static double min(double v1, double v2, double v3) {
    return Math.min(v1, Math.min(v2, v3));
  }

}
