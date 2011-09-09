package yaphyre.util;

public class RenderStatistics {
  private static int eyeRays = 0;

  private static int shadowRays = 0;

  private static int secondaryRays = 0;

  private static int cancelledRays = 0;

  public static int getEyeRays() {
    return eyeRays;
  }

  public static int incEyeRays() {
    return ++eyeRays;
  }

  public static int getShadowRays() {
    return shadowRays;
  }

  public static int incShadowRays() {
    return ++shadowRays;
  }

  public static int getSecondaryRays() {
    return secondaryRays;
  }

  public static int incSecondaryRays() {
    return ++secondaryRays;
  }

  public static int getCancelledRays() {
    return cancelledRays;
  }

  public static int incCancelledRays() {
    return ++cancelledRays;
  }
}
