package test.yaphyre.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector3D;

public class RayTest {

  @Test
  public void testRay() {
    Point3D origin = new Point3D(1, 1, 1);
    Vector3D direction = Normal3D.NORMAL_X.asVector();

    Ray r;

    r = new Ray(origin, direction);
    assertNotNull(r);
    assertNotNull(r.getOrigin());
    assertNotNull(r.getDirection());
    assertEquals(origin, r.getOrigin());
    assertEquals(Normal3D.NORMAL_X.asVector(), r.getDirection());

  }

  @Test
  public void testGetOrigin() {
    Point3D origin = new Point3D(1, 1, 1);
    Vector3D direction = Normal3D.NORMAL_X.asVector();

    Ray r = new Ray(origin, direction);

    assertNotNull(r.getOrigin());
    assertEquals(origin, r.getOrigin());
  }

  @Test
  public void testGetDirection() {
    Point3D origin = new Point3D(1, 1, 1);
    Vector3D direction = Normal3D.NORMAL_X.asVector();
    Ray r;

    r = new Ray(origin, direction);
    assertNotNull(r);
    assertNotNull(r.getDirection());
    assertEquals(Normal3D.NORMAL_X.asVector(), r.getDirection());

  }

}
