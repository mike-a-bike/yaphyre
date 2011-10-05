package test.yaphyre.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.MessageFormat;

import org.junit.Test;

import yaphyre.geometry.Matrix;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Transformation;
import yaphyre.geometry.Vector3D;

public class VectorTest {

  @Test
  public void testNewVector() {
    Vector3D v = new Vector3D(0d, 1d, 2d);
    assertNotNull(v);
    assertEquals(0d, v.getX(), 0);
    assertEquals(1d, v.getY(), 0);
    assertEquals(2d, v.getZ(), 0);
  }

  public void testVectorWithPoints() {
    Point3D startPoint = new Point3D(1d, 1d, 1d);
    Point3D endPoint = new Point3D(2d, 2d, 2d);
    Vector3D v = new Vector3D(startPoint, endPoint);
    assertNotNull(v);
    assertEquals(new Vector3D(1, 1, 1), v);
  }

  @Test
  public void testEqualsObject() {
    Vector3D v1 = new Vector3D(0d, 0d, 0d);
    Vector3D v2 = new Vector3D(1d, 2d, 3d);
    assertTrue(v1.equals(v1));
    assertFalse(v1.equals(null));
    assertFalse(v1.equals("Hello, World"));
    assertTrue(v1.equals(Vector3D.NULL));
    assertFalse(v1.equals(v2));
    assertFalse(v2.equals(Normal3D.NORMAL_X.asVector()));
  }

  @Test
  public void testEqualsWithTolerance() {
    Vector3D v1 = new Vector3D(0, 0, 0);
    Vector3D v2 = new Vector3D(1, 1, 1);
    Vector3D v3 = new Vector3D(0, 0, 0.00001d);

    assertTrue(v1.equals(v1, 0d));
    assertFalse(v1.equals(null, 0d));
    assertFalse(v1.equals(v2, 0d));
    assertTrue(v1.equals(v3, 1e-4));

  }

  @Test
  public void testAdd() {
    Vector3D result;
    Vector3D v1 = new Vector3D(1d, 1d, 1d);
    Vector3D v2 = new Vector3D(2d, 2d, 2d);

    result = v1.add(v2);
    assertEquals(3d, result.getX(), 0);
    assertEquals(3d, result.getY(), 0);
    assertEquals(3d, result.getZ(), 0);

    result = Normal3D.NORMAL_X.asVector().add(Normal3D.NORMAL_Y.asVector()).add(Normal3D.NORMAL_Z.asVector());
    assertEquals(1d, result.getX(), 0);
    assertEquals(1d, result.getY(), 0);
    assertEquals(1d, result.getZ(), 0);
  }

  @Test
  public void testSub() {
    Vector3D result;
    Vector3D v1 = new Vector3D(1d, 1d, 1d);
    Vector3D v2 = new Vector3D(2d, 2d, 2d);

    result = v1.sub(v2);
    assertEquals(-1, result.getX(), 0);
    assertEquals(-1, result.getY(), 0);
    assertEquals(-1, result.getZ(), 0);

    result = v2.sub(v1);
    assertEquals(1, result.getX(), 0);
    assertEquals(1, result.getY(), 0);
    assertEquals(1, result.getZ(), 0);

    result = v2.sub(v2);
    assertTrue(Vector3D.NULL.equals(result));
  }

  @Test
  public void testLength() {
    Vector3D v;
    double length;

    length = Vector3D.NULL.length();
    System.out.println("Length of " + Vector3D.NULL + " is: " + length);
    assertEquals(0d, length, 0);

    length = Normal3D.NORMAL_X.asVector().length();
    System.out.println("Length of " + Normal3D.NORMAL_X.asVector() + " is: " + length);
    assertEquals(1d, length, 0);

    length = Normal3D.NORMAL_Y.asVector().length();
    System.out.println("Length of " + Normal3D.NORMAL_Y.asVector() + " is: " + length);
    assertEquals(1d, length, 0);

    length = Normal3D.NORMAL_Z.asVector().length();
    System.out.println("Length of " + Normal3D.NORMAL_Z.asVector() + " is: " + length);
    assertEquals(1d, length, 0);

    v = new Vector3D(1d, 1d, 1d);
    length = v.length();
    System.out.println("Length of " + v + " is: " + length);
    assertEquals(Math.sqrt(3), length, 0);

    v = new Vector3D(1d, 2d, 3d);
    length = v.length();
    System.out.println("Length of " + v + " is: " + length);
    assertEquals(Math.sqrt(1 + 4 + 9), length, 0);

  }

  @Test
  public void testUnitVector() {
    Vector3D v;
    Vector3D result;

    v = Normal3D.NORMAL_X.asVector();
    result = v.normalize();
    System.out.println("Unit vector of " + v + " is: " + result);
    assertEquals(Normal3D.NORMAL_X.asVector(), result);

    v = new Vector3D(1d, 1d, 1d);
    result = v.normalize();
    System.out.println("Unit vector of " + v + " is: " + result);
    assertEquals(1d, result.length(), 0);

    v = new Vector3D(100d, 100d, 100d);
    result = v.normalize();
    System.out.println("Unit vector of " + v + " is: " + result);
    assertEquals(1d, result.length(), 0);

    v = new Vector3D(1d, 2d, 3d);
    result = v.normalize();
    System.out.println("Unit vector of " + v + " is: " + result);
    assertEquals(1d, result.length(), 0);

    v = new Vector3D(12.323d, 88.831d, -45.1235d);
    result = v.normalize();
    System.out.println("Unit vector of " + v + " is: " + result);
    assertEquals(1d, result.length(), 0);
  }

  @Test(expected = ArithmeticException.class)
  public void testUnitVectorZeroLength() {
    Vector3D.NULL.normalize();
  }

  @Test
  public void testScale() {
    Vector3D v;
    double scalar;
    Vector3D result;

    v = Vector3D.NULL;
    scalar = 100d;
    result = v.scale(scalar);
    System.out.println("Scale " + v + " by " + scalar + ": " + result);
    assertEquals(result, Vector3D.NULL);

    v = Normal3D.NORMAL_X.asVector();
    scalar = 100d;
    result = v.scale(scalar);
    System.out.println("Scale " + v + " by " + scalar + ": " + result);
    assertEquals(new Vector3D(100d, 0d, 0d), result);

    v = new Vector3D(1, 2, 3);
    scalar = 10d;
    result = v.scale(scalar);
    System.out.println("Scale " + v + " by " + scalar + ": " + result);
    assertEquals(new Vector3D(10d, 20d, 30d), result);
  }

  @Test
  public void testDotProduct() {
    Vector3D v1;
    Vector3D v2;
    double result;

    v1 = Normal3D.NORMAL_X.asVector();
    v2 = Normal3D.NORMAL_Y.asVector();
    result = v1.dot(v2);
    System.out.println(v1 + " dot " + v2 + " = " + result);
    assertEquals(0d, result, 0);

    v1 = new Vector3D(1, 1, 1);
    v2 = new Vector3D(1, 1, 1);
    // 1*1 + 1*1 + 1*1 = 3
    result = v1.dot(v2);
    System.out.println(v1 + " dot " + v2 + " = " + result);
    assertEquals(3d, result, 0);

    v1 = new Vector3D(1, 1, 1);
    v2 = new Vector3D(2, 2, 2);
    // 1*2 + 1*2 + 1*2 = 6
    result = v1.dot(v2);
    System.out.println(v1 + " dot " + v2 + " = " + result);
    assertEquals(6d, result, 0);

    v1 = new Vector3D(1, 2, 3);
    v2 = new Vector3D(2, 3, 4);
    // 1*2 + 2*3 + 3*4 = 20
    result = v1.dot(v2);
    System.out.println(v1 + " dot " + v2 + " = " + result);
    assertEquals(20d, result, 0);
  }

  @Test
  public void testCrossProduct() {
    Vector3D v1;
    Vector3D v2;
    Vector3D result;

    // result is the z axis normal since the cross product of the x-y plane lies
    // on the z-axis.
    v1 = Normal3D.NORMAL_X.asVector();
    v2 = Normal3D.NORMAL_Y.asVector();
    result = v1.cross(v2);
    System.out.println(v1 + " x " + v2 + " = " + result);
    assertEquals(Normal3D.NORMAL_Z.asVector(), result);

    v1 = Normal3D.NORMAL_Y.asVector();
    v2 = Normal3D.NORMAL_X.asVector();
    result = v1.cross(v2);
    System.out.println(v1 + " x " + v2 + " = " + result);
    assertEquals(Normal3D.NORMAL_Z.asVector().neg(), result);

    v1 = Normal3D.NORMAL_Y.asVector();
    v2 = Normal3D.NORMAL_Z.asVector();
    result = v1.cross(v2);
    System.out.println(v1 + " x " + v2 + " = " + result);
    assertEquals(Normal3D.NORMAL_X.asVector(), result);

    v1 = Normal3D.NORMAL_X.asVector();
    v2 = Normal3D.NORMAL_Z.asVector();
    result = v1.cross(v2);
    System.out.println(v1 + " x " + v2 + " = " + result);
    assertEquals(Normal3D.NORMAL_Y.asVector().neg(), result);

    v1 = new Vector3D(2, 0, 0);
    v2 = new Vector3D(0, 10, 0);
    result = v1.cross(v2);
    System.out.println(v1 + " x " + v2 + " = " + result);
    // The new vector should have a length of 2 * 10
    // and a direction along the z-axis
    assertEquals(20d, result.length(), 0);
    assertEquals(Normal3D.NORMAL_Z.asVector(), result.normalize());

  }

  @Test
  public void testTransform() {
    Vector3D vector = new Vector3D(10, 10, 10);
    Matrix transform;
    Vector3D result;
    Vector3D expected;

    transform = Transformation.translate(10, 0, 0).getTransformation();
    expected = new Vector3D(10, 10, 10);
    result = vector.transform(transform);
    System.out.println(vector + " * " + transform + " = " + result);
    assertEquals(expected, result);

    transform = Transformation.translate(0, 10, 0).getTransformation();
    expected = new Vector3D(10, 10, 10);
    result = vector.transform(transform);
    System.out.println(vector + " * " + transform + " = " + result);
    assertEquals(expected, result);

    transform = Transformation.translate(0, 0, 10).getTransformation();
    expected = new Vector3D(10, 10, 10);
    result = vector.transform(transform);
    System.out.println(vector + " * " + transform + " = " + result);
    assertEquals(expected, result);

  }

  @Test
  public void testToString() {
    String stringRep;

    stringRep = Vector3D.NULL.toString();

    assertEquals("<0.000, 0.000, 0.000>", stringRep);
  }

  @Test
  public void testCreateCoordinateSystem() {
    // the base vectors to start from (they do not have to be perpendicular to
    // each other)
    Vector3D up = new Vector3D(0, 1, 1);
    Vector3D right = new Vector3D(1, 0, 1);

    // up vector is preserved in the new coordinate system as y vector, the
    // other axes are re-calculated.

    // normalize up: y = |up|
    Vector3D y = up.normalize();
    // calculate the z direction: z = |right x y|
    Vector3D z = right.cross(y).normalize();
    // recalculate the x direction: x = |y x z|
    Vector3D x = y.cross(z).normalize();

    System.out.println(MessageFormat.format("New coordinate system from up={0} and right={1}", up, right));
    System.out.println(MessageFormat.format("x={0}, y={1}, z={2}", x, y, z));

  }

}
