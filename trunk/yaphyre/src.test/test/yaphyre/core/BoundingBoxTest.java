package test.yaphyre.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import yaphyre.core.BoundingBox;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector3D;

public class BoundingBoxTest {

  @Test
  public void testBoundingBoxPoint3D() {
    System.out.println("testBoundingBoxPoint3D: ");

    BoundingBox box = new BoundingBox(Point3D.ORIGIN);

    assertNotNull(box);

    System.out.println(box.toString());

    assertTrue(box.isInside(Point3D.ORIGIN));
    assertFalse(box.isInside(new Point3D(1, 0, 0)));
    assertFalse(box.isInside(new Point3D(-1, 0, 0)));
  }

  @Test
  public void testBoundingBoxPoint3DPoint3D() {
    System.out.println("testBoundingBoxPoint3DPoint3D: ");

    BoundingBox b1 = new BoundingBox(new Point3D(0, 0, 0), new Point3D(1, 1, 1));
    BoundingBox b2 = new BoundingBox(new Point3D(1, 1, 1), new Point3D(0, 0, 0));
    System.out.println("b1 = " + b1.toString());
    System.out.println("b2 = " + b2.toString());
    assertTrue(b1.equals(b2));

    BoundingBox box = new BoundingBox(new Point3D(-1, -1, -1), new Point3D(1, 1, 1));

    assertNotNull(box);

    System.out.println("box = " + box.toString());

    assertTrue(box.isInside(new Point3D(0, 0, 0)));

    assertTrue(box.isInside(new Point3D(-1, -1, -1)));
    assertTrue(box.isInside(new Point3D(1, -1, -1)));
    assertTrue(box.isInside(new Point3D(-1, 1, -1)));
    assertTrue(box.isInside(new Point3D(1, 1, -1)));

    assertTrue(box.isInside(new Point3D(1, 1, 1)));
    assertTrue(box.isInside(new Point3D(-1, 1, 1)));
    assertTrue(box.isInside(new Point3D(1, -1, 1)));
    assertTrue(box.isInside(new Point3D(-1, -1, 1)));
  }

  @Test
  public void testUnionBoundingBoxPoint3D() {
    System.out.println("testUnionBoundingBoxPoint3D: ");

    BoundingBox box = new BoundingBox(new Point3D(-1, -1, -1), new Point3D(0, 0, 0));
    System.out.println("box = " + box.toString());

    box = BoundingBox.union(box, new Point3D(1, 1, 1));
    System.out.println("box = " + box.toString());
    assertTrue(new BoundingBox(new Point3D(-1, -1, -1), new Point3D(1, 1, 1)).equals(box));
  }

  @Test
  public void testUnionBoundingBoxBoundingBox() {
    System.out.println("testUnionBoundingBoxBoundingBox");

    BoundingBox b1 = new BoundingBox(new Point3D(0, 0, 0), new Point3D(1, 1, 1));
    BoundingBox b2 = new BoundingBox(new Point3D(0, 0, 0), new Point3D(-1, -1, -1));

    System.out.println("b1 = " + b1.toString());
    System.out.println("b2 = " + b2.toString());

    BoundingBox box = BoundingBox.union(b1, b2);

    System.out.println("box = " + box.toString());

    assertTrue(new BoundingBox(new Point3D(-1, -1, -1), new Point3D(1, 1, 1)).equals(box));
  }

  @Test
  public void testEquals() {
    System.out.println("testEquals");

    BoundingBox b1 = new BoundingBox(new Point3D(-1, -1, -1), new Point3D(1, 1, 1));
    BoundingBox b2 = new BoundingBox(new Point3D(-1, -1, -1), new Point3D(1, 1, 1));
    BoundingBox b3 = new BoundingBox(new Point3D(0, 0, 0), new Point3D(1, 1, 1));

    System.out.println("b1 = " + b1.toString());
    System.out.println("b2 = " + b2.toString());
    System.out.println("b3 = " + b3.toString());

    assertFalse(b1 == b2);
    assertTrue(b1.equals(b1));
    assertFalse(b1.equals(null));
    assertTrue(b1.equals(b2));
    assertTrue(b2.equals(b1));
    assertFalse(b1.equals(b3));
    assertFalse(b3.equals(b1));

  }

  @Test
  public void testHashcode() {
    System.out.println("testHashcode");

    BoundingBox b1 = new BoundingBox(new Point3D(-1, -1, -1), new Point3D(1, 1, 1));
    BoundingBox b2 = new BoundingBox(new Point3D(-1, -1, -1), new Point3D(1, 1, 1));
    BoundingBox b3 = new BoundingBox(new Point3D(0, 0, 0), new Point3D(1, 1, 1));

    System.out.println("b1 = " + b1.toString() + " ( hashcode = " + b1.hashCode() + " )");
    System.out.println("b2 = " + b2.toString() + " ( hashcode = " + b2.hashCode() + " )");
    System.out.println("b3 = " + b3.toString() + " ( hashcode = " + b3.hashCode() + " )");

    assertTrue(b1.hashCode() == b2.hashCode());
    assertFalse(b1.hashCode() == b3.hashCode());

  }

  @Test
  public void testIsInside() {
    System.out.println("testIsInside");

    BoundingBox box = new BoundingBox(new Point3D(-1, -1, -1), new Point3D(1, 1, 1));

    assertNotNull(box);

    System.out.println("box = " + box.toString());

    assertTrue(box.isInside(new Point3D(0, 0, 0)));

    assertTrue(box.isInside(new Point3D(-1, -1, -1)));
    assertTrue(box.isInside(new Point3D(1, -1, -1)));
    assertTrue(box.isInside(new Point3D(-1, 1, -1)));
    assertTrue(box.isInside(new Point3D(1, 1, -1)));

    assertTrue(box.isInside(new Point3D(1, 1, 1)));
    assertTrue(box.isInside(new Point3D(-1, 1, 1)));
    assertTrue(box.isInside(new Point3D(1, -1, 1)));
    assertTrue(box.isInside(new Point3D(-1, -1, 1)));
  }

  @Test
  public void testOverlaps() {
    System.out.println("testOverlaps");

    BoundingBox box = new BoundingBox(new Point3D(-1, -1, -1), new Point3D(1, 1, 1));

    BoundingBox b1 = new BoundingBox(new Point3D(0, 0, 0), new Point3D(1, 1, 1));
    BoundingBox b2 = new BoundingBox(new Point3D(0, 0, 0), new Point3D(2, 2, 2));

    System.out.println("box = " + box.toString());
    System.out.println("b1 = " + b1.toString());
    System.out.println("b2 = " + b2.toString());

    System.out.println("box overlaps with b1: " + box.overlaps(b1));
    System.out.println("box overlaps with b2: " + box.overlaps(b2));

    assertTrue(box.overlaps(b1));
    assertFalse(box.overlaps(b2));

  }

  @Test
  public void testIsHitBy() {
    System.out.println("testIsHitBy");

    BoundingBox box = new BoundingBox(new Point3D(-1, -1, -1), new Point3D(1, 1, 1));
    Ray ray = new Ray(new Point3D(0, 0, -10), Vector3D.Z);

    System.out.println("box = " + box.toString());
    System.out.println("ray = " + ray.toString());

    assertTrue(box.isHitBy(ray));

    ray = new Ray(new Point3D(0, 0, -10), Vector3D.X);

    assertFalse(box.isHitBy(ray));

  }

}
