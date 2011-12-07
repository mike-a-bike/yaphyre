package test.yaphyre.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import yaphyre.geometry.Matrix;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Transformation;
import yaphyre.geometry.Vector3D;

public class TransformationTest {

  @Test
  public void testLookAtTransformation() {
    Point3D eye;
    Point3D lookAt;
    Vector3D up;

    eye = Point3D.ORIGIN;
    lookAt = new Point3D(0, 0, 1);
    up = Vector3D.Y;
    Transformation result;

    result = Transformation.lookAt(eye, lookAt, up);

    assertEquals(Matrix.IDENTITY, result.getMatrix());
  }

  @Test
  public void testRasterTransformation() {
    Transformation rasterTransformation;
    Point2D p;
    Point2D pt;

    rasterTransformation = Transformation.rasterToUnitSquare(640, 480);
    assertNotNull(rasterTransformation);

    p = new Point2D(0d, 0d);
    pt = rasterTransformation.transform(p);

    assertEquals(new Point2D(0d, 0d), pt);

    p = new Point2D(640d, 480d);
    pt = rasterTransformation.transform(p);

    assertEquals(new Point2D(1d, 1d), pt);

    p = new Point2D(320d, 240d);
    pt = rasterTransformation.transform(p);

    assertEquals(new Point2D(0.5d, 0.5d), pt);

    p = new Point2D(0d, 0d);
    pt = rasterTransformation.inverse().transform(p);

    assertEquals(p, pt);

    p = new Point2D(1d, 1d);
    pt = rasterTransformation.inverse().transform(p);

    assertEquals(new Point2D(640d, 480d), pt);

    p = new Point2D(0.5d, 0.5d);
    pt = rasterTransformation.inverse().transform(p);

    assertEquals(new Point2D(320d, 240d), pt);

  }

  @Test
  public void testMul() {

    Transformation trans1 = Transformation.rasterToUnitSquare(640, 480);
    Transformation trans2 = Transformation.translate(-0.5d, -0.5d, 0);
    Transformation transCombined = trans2.mul(trans1);

    Point2D p;
    Point2D pt1;
    Point2D pt2;

    p = new Point2D(0d, 0d);
    pt1 = transCombined.transform(p);
    pt2 = trans2.transform(trans1.transform(p));

    assertEquals(pt1, pt2);

    p = new Point2D(640d, 480d);
    pt1 = transCombined.transform(p);
    pt2 = trans2.transform(trans1.transform(p));

    assertEquals(pt1, pt2);

    p = new Point2D(320d, 240d);
    pt1 = transCombined.transform(p);
    pt2 = trans2.transform(trans1.transform(p));

    assertEquals(pt1, pt2);
  }

  @Test
  public void testTransformVector() {
    Vector3D v;
    Vector3D r;
    Transformation t;

    v = Vector3D.X;
    t = Transformation.rotateZ(90);
    r = t.transform(v);
    assertEquals(Vector3D.Y, r);
    r = t.inverse().transform(r);
    assertEquals(v, r);

    t = Transformation.translate(10, 20, 30);
    r = t.transform(v);
    assertEquals(v, r);
    r = t.inverse().transform(r);
    assertEquals(v, r);

    t = Transformation.scale(100, 100, 100);
    r = t.transform(v);
    assertEquals(new Vector3D(100, 0, 0), r);
    r = t.inverse().transform(r);
    assertEquals(v, r);

  }

  @Test
  public void testRotate() {

    Vector3D v;
    Vector3D r;
    Transformation t;

    t = Transformation.rotate(120, new Vector3D(1, 1, 1));

    v = Vector3D.X;
    r = t.transform(v);
    assertEquals(Vector3D.Y, r);

    r = t.transform(r);
    assertEquals(Vector3D.Z, r);

    r = t.transform(r);
    assertEquals(Vector3D.X, r);

    fail("Not implemented yet");
    // Vector3D v;
    // Vector3D r;
    // Transformation t;
    //
    // v = Vector3D.Y;
    // t = Transformation.rotateX(90);
    // r = t.transform(v);
    // assertEquals(Vector3D.Z, r);
    // r = t.transformInverse(r);
    // assertEquals(v, r);
    //
    // v = Vector3D.Z;
    // t = Transformation.rotateY(90);
    // r = t.transform(v);
    // assertEquals(Vector3D.X, r);
    // r = t.transformInverse(r);
    // assertEquals(v, r);
  }
}
