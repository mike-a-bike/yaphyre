package test.yaphyre.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import yaphyre.math.MathUtils;

public class MathUtilsTest {

  @Test
  public void testMax() {
    double maxVal;

    maxVal = MathUtils.max(1, 2, 3);
    assertEquals(3, maxVal, 0);

    maxVal = MathUtils.max(1, 1, 1);
    assertEquals(1, maxVal, 0);

    maxVal = MathUtils.max(Double.MAX_VALUE, Double.MIN_VALUE, 0);
    assertEquals(Double.MAX_VALUE, maxVal, 0);

    maxVal = MathUtils.max(Double.MAX_VALUE, Double.POSITIVE_INFINITY, 0);
    assertEquals(Double.POSITIVE_INFINITY, maxVal, 0);
  }

  @Test
  public void testMin() {
    double minVal;

    minVal = MathUtils.min(1, 2, 3);
    assertEquals(1, minVal, 0);

    minVal = MathUtils.min(0, 0, 0);
    assertEquals(0, minVal, 0);

    minVal = MathUtils.min(Double.MAX_VALUE, Double.MIN_VALUE, 0);
    assertEquals(0, minVal, 0);

    minVal = MathUtils.min(Double.MIN_NORMAL, Double.MIN_VALUE, Double.NEGATIVE_INFINITY);
    assertEquals(Double.NEGATIVE_INFINITY, minVal, 0);
  }

}
