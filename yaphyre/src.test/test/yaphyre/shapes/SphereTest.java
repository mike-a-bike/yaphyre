package test.yaphyre.shapes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector3D;
import yaphyre.shaders.Material;
import yaphyre.shaders.MaterialBuilder;
import yaphyre.shaders.Shaders;
import yaphyre.shapes.Shapes;
import yaphyre.shapes.Sphere;
import yaphyre.util.Color;

public class SphereTest {

  private static final String SHPERE_ID = "sphere1";

  private static final String UNIT_SPHERE_ID = "unitSphere";

  private static final String SHADER_ID = "shader1";

  private Shapes createTestSphere() {
    return new Sphere(new Point3D(2d, 0d, 0d), 1d, new TestShader(), true);
  }

  /**
   * Creates a sphere at the coordinate origin with a radius of 1.
   * 
   * @return The unit sphere.
   */
  private Shapes createUnitSphere() {
    return new Sphere(Point3D.ORIGIN, 1d, new TestShader(), true);
  }

  @Test
  public void testSphere() {
    Sphere s = new Sphere(new Point3D(1d, 1d, 1d), 1d, null, true);
    System.out.println("New sphere created: " + s);
    assertNotNull(s);
  }

  @Test
  public void testGetIntersectDistance() {
    Ray intersectingRay = new Ray(Point3D.ORIGIN, Normal3D.NORMAL_X.asVector());
    Ray nonIntersectingRay = new Ray(Point3D.ORIGIN, Normal3D.NORMAL_Y.asVector());
    Ray crookedRay = new Ray(Point3D.ORIGIN, new Vector3D(1, 0.25, 0.25).normalize());
    Shapes testSphere = createTestSphere();

    double distance;

    distance = testSphere.getIntersectDistance(nonIntersectingRay);
    System.out.println(testSphere + " intersects with " + nonIntersectingRay + " at a distance of " + distance);
    assertEquals(Shapes.NO_INTERSECTION, distance, 0);

    distance = testSphere.getIntersectDistance(intersectingRay);
    System.out.println(testSphere + " intersects with " + intersectingRay + " at a distance of " + distance);
    assertEquals(1d, distance, 0);

    distance = testSphere.getIntersectDistance(crookedRay);
    System.out.println(testSphere + " intersects with " + crookedRay + " at a distance of " + distance);

  }

  @Test
  public void testGetIntersectionPoint() {
    Ray intersectingRay = new Ray(Point3D.ORIGIN, Normal3D.NORMAL_X.asVector());
    Ray nonIntersectingRay = new Ray(Point3D.ORIGIN, Normal3D.NORMAL_Y.asVector());
    Ray crookedRay = new Ray(Point3D.ORIGIN, new Vector3D(1, 0.25, 0.25).normalize());

    Shapes testSphere = createTestSphere();
    Shapes unitSphere = createUnitSphere();

    Point3D intersectionPoint;

    intersectionPoint = testSphere.getIntersectionPoint(nonIntersectingRay);
    System.out.println(testSphere + " intersects with " + nonIntersectingRay + " at " + intersectionPoint);
    assertNull(intersectionPoint);

    intersectionPoint = testSphere.getIntersectionPoint(intersectingRay);
    System.out.println(testSphere + " intersects with " + intersectingRay + " at " + intersectionPoint);
    assertNotNull(intersectionPoint);
    assertEquals(new Point3D(1d, 0d, 0d), intersectionPoint);

    intersectionPoint = testSphere.getIntersectionPoint(crookedRay);
    System.out.println(testSphere + " intersects with " + crookedRay + " at " + intersectionPoint);
    assertNotNull(intersectionPoint);

  }

  @Test
  public void testGetNormal() {
    Shapes testSphere = createTestSphere();
    Shapes unitSphere = createUnitSphere();

    Point3D p1 = new Point3D(1, 0, 0);
    Point3D p2 = new Point3D(3, 0, 0);
    Point3D p3 = new Point3D(1, 2, 3);
    Point3D p;
    Normal3D n;

    n = testSphere.getNormal(p1);
    System.out.println("Normal of " + testSphere + " at " + p1 + " = " + n);
    assertNotNull(n);
    assertEquals(new Normal3D(-1, 0, 0), n);

    n = testSphere.getNormal(p2);
    System.out.println("Normal of " + testSphere + " at " + p2 + " = " + n);
    assertNotNull(n);
    assertEquals(new Normal3D(1, 0, 0), n);

    n = testSphere.getNormal(p3);
    System.out.println("Normal of " + testSphere + " at " + p3 + " = " + n);
    assertNotNull(n);

    p = new Point3D(0, 0, -1);
    n = unitSphere.getNormal(p);
    System.out.println("Normal of " + unitSphere + " at " + p + " = " + n);
    assertNotNull(n);
    assertEquals(new Normal3D(0, 0, -1), n);

    p = new Point3D(0, 0, 1);
    n = unitSphere.getNormal(p);
    System.out.println("Normal of " + unitSphere + " at " + p + " = " + n);
    assertNotNull(n);
    assertEquals(new Normal3D(0, 0, 1), n);

    p = new Point3D(1, 0, 0);
    n = unitSphere.getNormal(p);
    System.out.println("Normal of " + unitSphere + " at " + p + " = " + n);
    assertNotNull(n);
    assertEquals(new Normal3D(1, 0, 0), n);

    p = new Point3D(-1, 0, 0);
    n = unitSphere.getNormal(p);
    System.out.println("Normal of " + unitSphere + " at " + p + " = " + n);
    assertNotNull(n);
    assertEquals(new Normal3D(-1, 0, 0), n);

    p = new Point3D(0, 1, 0);
    n = unitSphere.getNormal(p);
    System.out.println("Normal of " + unitSphere + " at " + p + " = " + n);
    assertNotNull(n);
    assertEquals(new Normal3D(0, 1, 0), n);

    p = new Point3D(0, -1, 0);
    n = unitSphere.getNormal(p);
    System.out.println("Normal of " + unitSphere + " at " + p + " = " + n);
    assertNotNull(n);
    assertEquals(new Normal3D(0, -1, 0), n);

    p = new Point3D(0, 1, 1);
    n = unitSphere.getNormal(p);
    System.out.println("Normal of " + unitSphere + " at " + p + " = " + n);
    assertNotNull(n);
    // assertEquals(new Vector(0, -1, 0), n);

  }

  @Test
  public void testGetShader() {
    Shapes s = createTestSphere();
    assertNotNull(s.getShader());
  }

  private static class TestShader implements Shaders {
    private static final Color TEST_COLOR = new Color(java.awt.Color.BLACK);

    @Override
    public Color getColor(Point2D uvPoint) {
      return TEST_COLOR;
    }

    private static final Material TEST_MATERIAL = MaterialBuilder.start().ambient(0.1).diffuse(0.8).build();

    @Override
    public Material getMaterial(Point2D uvCoordinate) {
      return TEST_MATERIAL;
    }
  }

}
