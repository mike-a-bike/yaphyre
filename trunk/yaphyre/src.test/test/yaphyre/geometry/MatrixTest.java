package test.yaphyre.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import yaphyre.geometry.Matrix;
import yaphyre.math.MathUtils;

public class MatrixTest {

  private Matrix createTestMatrix() {
    return new Matrix(1, 2, 3, 4,
                      5, 6, 7, 8,
                      9, 10, 11, 12,
                      13, 14, 15, 16);
  }

  @Test
  public void testMatrix() {
    Matrix M = createTestMatrix();
    assertNotNull(M);
    assertEquals(1d, M.get(0, 0), 0d);
    assertEquals(16d, M.get(3, 3), 0d);
  }

  @Test
  public void testMatrixDoubleArrays() {
    Matrix M = createTestMatrix();
    assertNotNull(M);
    assertEquals(1d, M.get(0, 0), 0d);
    assertEquals(16d, M.get(3, 3), 0d);
  }

  @Test
  public void testMatrixAdd() {
    Matrix M1 = createTestMatrix();
    Matrix M2 = createTestMatrix();
    Matrix Mr = M1.add(M2);
    Matrix R = new Matrix(2, 4, 6, 8,
                          10, 12, 14, 16,
                          18, 20, 22, 24,
                          26, 28, 30, 32);

    assertEquals(R, Mr);

  }

  @Test
  public void testMatricMulScalar() {
    Matrix M = createTestMatrix();
    Matrix Mr = M.mul(2);
    Matrix R = new Matrix(2, 4, 6, 8,
                          10, 12, 14, 16,
                          18, 20, 22, 24,
                          26, 28, 30, 32);

    assertEquals(R, Mr);

    M = createTestMatrix();
    Mr = M.mul(1d / 2d);
    R = new Matrix(0.5, 1, 1.5, 2,
                   2.5, 3, 3.5, 4,
                   4.5, 5, 5.5, 6,
                   6.5, 7, 7.5, 8);

    assertEquals(R, Mr);

  }

  @Test
  public void testTransposed() {
    Matrix M = createTestMatrix();
    Matrix Mt = M.transpose();
    Matrix R = new Matrix(1, 5, 9, 13,
                          2, 6, 10, 14,
                          3, 7, 11, 15,
                          4, 8, 12, 16);

    assertEquals(R, Mt);
  }

  @Test
  public void testIsInvertible() {
    Matrix M;

    M = new Matrix(1, 7, 4, 2, 0, 9, 2, 5, 2, 2, 3, 2, 9, 9, 9, 9);
    assertTrue(M.isInvertible());
    assertNotNull(M.inverse());
    assertFalse(Math.abs(M.getDeterminat()) < MathUtils.EPSILON);

    M = createTestMatrix();
    assertFalse(M.isInvertible());
    assertTrue(M.inverse() == null);
    assertTrue(Math.abs(M.getDeterminat()) < MathUtils.EPSILON);
  }

  @Test
  public void testInverse() {
    Matrix M = new Matrix(1, 7, 4, 2, 0, 9, 2, 5, 2, 2, 3, 2, 9, 9, 9, 9);
    Matrix Mi = M.inverse();
    Matrix Mr = M.mul(Mi);

    assertEquals(Matrix.IDENTITY, Mr);
  }
}
