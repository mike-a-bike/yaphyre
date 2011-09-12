package test.yaphyre.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import yaphyre.geometry.Matrix;

public class MatrixTest {

  @Test
  public void testMatrix() {
    Matrix M = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
    assertNotNull(M);
    assertEquals(1d, M.get(0, 0), 0d);
  }

  @Test
  public void testEquals() {
    Matrix M = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
    Matrix N = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);

    assertFalse(M.equals(null));
    assertTrue(M.equals(M));
    assertTrue(M.equals(N));
  }

  @Test
  public void testAdd() {
    Matrix M = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
    Matrix N = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
    Matrix E = new Matrix(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32);

    Matrix R = M.add(N);

    System.out.println("M + N = " + R);

    assertEquals(E, R);
  }

  @Test
  public void testSub() {
    Matrix M = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
    Matrix N = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
    Matrix E = new Matrix(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

    Matrix R = M.sub(N);

    System.out.println("M - N = " + R);

    assertEquals(E, R);
  }

  @Test
  public void testDiv() {
    Matrix M = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
    Matrix E = new Matrix(1. / 2, 2. / 2, 3. / 2, 4. / 2, 5. / 2, 6. / 2, 7. / 2, 8. / 2, 9. / 2, 10. / 2, 11. / 2, 12. / 2, 13. / 2, 14. / 2, 15. / 2, 16. / 2);

    Matrix R = M.div(2d);

    System.out.println("M / 2 = " + R);

    assertEquals(E, R);
  }

  @Test
  public void testMulScalar() {
    Matrix M = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
    Matrix E = new Matrix(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32);

    Matrix R = M.mul(2);

    System.out.println("M * 2 = " + R);

    assertEquals(E, R);
  }

  @Test
  public void testMulMatrix() {
    Matrix M = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
    Matrix N = new Matrix(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
    Matrix E = new Matrix(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

    Matrix R = M.mul(N);

    System.out.println("M * N = " + R);

    assertEquals(E, R);
  }

  @Test
  public void testMulVector() {
    fail("Not yet implemented");
  }

}
