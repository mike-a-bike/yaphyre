package test.yaphyre.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import yaphyre.geometry.Matrix;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Transformation;
import yaphyre.geometry.TransformationBuilder;

public class TransformationBuilderTest {

  @Test
  public void testTranslate() {
    Transformation transformation = TransformationBuilder.translate(1, 2, 3);

    Matrix identity = transformation.getTransformation().mul(transformation.getInverseTransformation());
    assertEquals(Matrix.IDENTITY, identity);

    Point3D p = new Point3D(1, 0, 0);
    Point3D r = p.transform(transformation.getTransformation());
    assertEquals(new Point3D(2, 2, 3), r);

    r = r.transform(transformation.getInverseTransformation());
    assertEquals(p, r);
  }

  @Test
  public void testScale() {
    Transformation trans = TransformationBuilder.scale(2, 3, 4);

    Matrix identity = trans.getTransformation().mul(trans.getInverseTransformation());
    assertEquals(Matrix.IDENTITY, identity);

    Point3D p = new Point3D(1, 0, 0);
    Point3D r = p.transform(trans.getTransformation());
    assertEquals(new Point3D(2, 0, 0), r);

    p = new Point3D(1, 2, 3);
    r = p.transform(trans.getTransformation());
    assertEquals(new Point3D(2, 6, 12), r);

    r = r.transform(trans.getInverseTransformation());
    assertEquals(p, r);
  }

  @Test
  public void testRotateX() {
    fail("Not yet implemented");
  }

  @Test
  public void testRotateY() {
    fail("Not yet implemented");
  }

  @Test
  public void testRotateZ() {
    fail("Not yet implemented");
  }

  @Test
  public void testLookAt() {
    fail("Not yet implemented");
  }

}
