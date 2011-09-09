package test.yaphyre.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import yaphyre.geometry.Vector;

public class VectorTest {

  @Test
  public void testNewVector() {
    Vector v = new Vector(0d, 1d, 2d);
    assertNotNull(v);
    assertEquals(0d, v.getX(), 0);
    assertEquals(1d, v.getY(), 0);
    assertEquals(2d, v.getZ(), 0);
  }

  public void testVectorWithVectors() {
    Vector startPoint = new Vector(1d, 1d, 1d);
    Vector endPoint = new Vector(2d, 2d, 2d);
    Vector v = new Vector(startPoint, endPoint);
    assertNotNull(v);
    assertEquals(new Vector(1, 1, 1), v);
  }

  @Test
  public void testEqualsObject() {
    Vector v1 = new Vector(0d, 0d, 0d);
    Vector v2 = new Vector(1d, 2d, 3d);
    assertTrue(v1.equals(v1));
    assertFalse(v1.equals(null));
    assertFalse(v1.equals("Hello, World"));
    assertTrue(v1.equals(Vector.NULL));
    assertFalse(v1.equals(v2));
    assertFalse(v2.equals(Vector.NORMAL_X));
  }

  @Test
  public void testEqualsWithTolerance() {
    Vector v1 = new Vector(0, 0, 0);
    Vector v2 = new Vector(1, 1, 1);
    Vector v3 = new Vector(0, 0, 0.00001d);

    assertTrue(v1.equals(v1, 0d));
    assertFalse(v1.equals(null, 0d));
    assertFalse(v1.equals(v2, 0d));
    assertTrue(v1.equals(v3, 1e-4));

  }

  @Test
  public void testAdd() {
    Vector result;
    Vector v1 = new Vector(1d, 1d, 1d);
    Vector v2 = new Vector(2d, 2d, 2d);

    result = v1.add(v2);
    assertEquals(3d, result.getX(), 0);
    assertEquals(3d, result.getY(), 0);
    assertEquals(3d, result.getZ(), 0);

    result = Vector.NORMAL_X.add(Vector.NORMAL_Y).add(Vector.NORMAL_Z);
    assertEquals(1d, result.getX(), 0);
    assertEquals(1d, result.getY(), 0);
    assertEquals(1d, result.getZ(), 0);
  }

  @Test
  public void testSub() {
    Vector result;
    Vector v1 = new Vector(1d, 1d, 1d);
    Vector v2 = new Vector(2d, 2d, 2d);

    result = v1.sub(v2);
    assertEquals(-1, result.getX(), 0);
    assertEquals(-1, result.getY(), 0);
    assertEquals(-1, result.getZ(), 0);

    result = v2.sub(v1);
    assertEquals(1, result.getX(), 0);
    assertEquals(1, result.getY(), 0);
    assertEquals(1, result.getZ(), 0);

    result = v2.sub(v2);
    assertTrue(Vector.NULL.equals(result));
  }

  @Test
  public void testLength() {
    Vector v;
    double length;

    length = Vector.NULL.length();
    System.out.println("Length of " + Vector.NULL + " is: " + length);
    assertEquals(0d, length, 0);

    length = Vector.NORMAL_X.length();
    System.out.println("Length of " + Vector.NORMAL_X + " is: " + length);
    assertEquals(1d, length, 0);

    length = Vector.NORMAL_Y.length();
    System.out.println("Length of " + Vector.NORMAL_Y + " is: " + length);
    assertEquals(1d, length, 0);

    length = Vector.NORMAL_Z.length();
    System.out.println("Length of " + Vector.NORMAL_Z + " is: " + length);
    assertEquals(1d, length, 0);

    v = new Vector(1d, 1d, 1d);
    length = v.length();
    System.out.println("Length of " + v + " is: " + length);
    assertEquals(Math.sqrt(3), length, 0);

    v = new Vector(1d, 2d, 3d);
    length = v.length();
    System.out.println("Length of " + v + " is: " + length);
    assertEquals(Math.sqrt(1 + 4 + 9), length, 0);

  }

  @Test
  public void testUnitVector() {
    Vector v;
    Vector result;

    v = Vector.NORMAL_X;
    result = v.unitVector();
    System.out.println("Unit vector of " + v + " is: " + result);
    assertEquals(Vector.NORMAL_X, result);

    v = new Vector(1d, 1d, 1d);
    result = v.unitVector();
    System.out.println("Unit vector of " + v + " is: " + result);
    assertEquals(1d, result.length(), 0);

    v = new Vector(100d, 100d, 100d);
    result = v.unitVector();
    System.out.println("Unit vector of " + v + " is: " + result);
    assertEquals(1d, result.length(), 0);

    v = new Vector(1d, 2d, 3d);
    result = v.unitVector();
    System.out.println("Unit vector of " + v + " is: " + result);
    assertEquals(1d, result.length(), 0);

    v = new Vector(12.323d, 88.831d, -45.1235d);
    result = v.unitVector();
    System.out.println("Unit vector of " + v + " is: " + result);
    assertEquals(1d, result.length(), 0);
  }

  @Test(expected = ArithmeticException.class)
  public void testUnitVectorZeroLength() {
    Vector.NULL.unitVector();
  }

  @Test
  public void testScale() {
    Vector v;
    double scalar;
    Vector result;

    v = Vector.NULL;
    scalar = 100d;
    result = v.scale(scalar);
    System.out.println("Scale " + v + " by " + scalar + ": " + result);
    assertEquals(result, Vector.NULL);

    v = Vector.NORMAL_X;
    scalar = 100d;
    result = v.scale(scalar);
    System.out.println("Scale " + v + " by " + scalar + ": " + result);
    assertEquals(new Vector(100d, 0d, 0d), result);

    v = new Vector(1, 2, 3);
    scalar = 10d;
    result = v.scale(scalar);
    System.out.println("Scale " + v + " by " + scalar + ": " + result);
    assertEquals(new Vector(10d, 20d, 30d), result);
  }

  @Test
  public void testDotProduct() {
    Vector v1;
    Vector v2;
    double result;

    v1 = Vector.NORMAL_X;
    v2 = Vector.NORMAL_Y;
    result = v1.dot(v2);
    System.out.println(v1 + " dot " + v2 + " = " + result);
    assertEquals(0d, result, 0);

    v1 = new Vector(1, 1, 1);
    v2 = new Vector(1, 1, 1);
    // 1*1 + 1*1 + 1*1 = 3
    result = v1.dot(v2);
    System.out.println(v1 + " dot " + v2 + " = " + result);
    assertEquals(3d, result, 0);

    v1 = new Vector(1, 1, 1);
    v2 = new Vector(2, 2, 2);
    // 1*2 + 1*2 + 1*2 = 6
    result = v1.dot(v2);
    System.out.println(v1 + " dot " + v2 + " = " + result);
    assertEquals(6d, result, 0);

    v1 = new Vector(1, 2, 3);
    v2 = new Vector(2, 3, 4);
    // 1*2 + 2*3 + 3*4 = 20
    result = v1.dot(v2);
    System.out.println(v1 + " dot " + v2 + " = " + result);
    assertEquals(20d, result, 0);
  }

  @Test
  public void testCrossProduct() {
    Vector v1;
    Vector v2;
    Vector result;

    // result is the z axis normal since the cross product of the x-y plane lies
    // on the z-axis.
    v1 = Vector.NORMAL_X;
    v2 = Vector.NORMAL_Y;
    result = v1.cross(v2);
    System.out.println(v1 + " x " + v2 + " = " + result);
    assertEquals(Vector.NORMAL_Z, result);

    v1 = Vector.NORMAL_Y;
    v2 = Vector.NORMAL_X;
    result = v1.cross(v2);
    System.out.println(v1 + " x " + v2 + " = " + result);
    assertEquals(Vector.NORMAL_Z.scale(-1), result);

    v1 = Vector.NORMAL_Y;
    v2 = Vector.NORMAL_Z;
    result = v1.cross(v2);
    System.out.println(v1 + " x " + v2 + " = " + result);
    assertEquals(Vector.NORMAL_X, result);

    v1 = Vector.NORMAL_X;
    v2 = Vector.NORMAL_Z;
    result = v1.cross(v2);
    System.out.println(v1 + " x " + v2 + " = " + result);
    assertEquals(Vector.NORMAL_Y.scale(-1), result);

    v1 = new Vector(2, 0, 0);
    v2 = new Vector(0, 10, 0);
    result = v1.cross(v2);
    System.out.println(v1 + " x " + v2 + " = " + result);
    // The new vector should have a length of 2 * 10
    // and a direction along the z-axis
    assertEquals(20d, result.length(), 0);
    assertEquals(Vector.NORMAL_Z, result.unitVector());

  }

  @Test
  public void testToString() {
    String stringRep;

    stringRep = Vector.NULL.toString();

    assertEquals("<0, 0, 0>", stringRep);
  }

}
