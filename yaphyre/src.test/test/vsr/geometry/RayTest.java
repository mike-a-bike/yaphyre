package test.vsr.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector;

public class RayTest {

  @Test
  public void testRay() {
    Vector origin = new Vector(1, 1, 1);
    Vector direction = Vector.NORMAL_X;

    Ray r;

    r = new Ray(origin, direction);
    assertNotNull(r);
    assertNotNull(r.getOrigin());
    assertNotNull(r.getDirection());
    assertEquals(origin, r.getOrigin());
    assertEquals(Vector.NORMAL_X, r.getDirection());

  }

  @Test
  public void testGetOrigin() {
    Vector origin = new Vector(1, 1, 1);
    Vector direction = Vector.NORMAL_X;

    Ray r = new Ray(origin, direction);

    assertNotNull(r.getOrigin());
    assertEquals(origin, r.getOrigin());
  }

  @Test
  public void testGetDirection() {
    Vector origin = new Vector(1, 1, 1);
    Vector direction = Vector.NORMAL_X;
    Ray r;

    r = new Ray(origin, direction);
    assertNotNull(r);
    assertNotNull(r.getDirection());
    assertEquals(Vector.NORMAL_X, r.getDirection());

  }

}
