/*
 * Copyright 2012 Michael Bieri
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package test.yaphyre.shapes;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Test;

import yaphyre.core.Shader;
import yaphyre.core.Shape;
import yaphyre.geometry.Matrix;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Transformation;
import yaphyre.geometry.Vector3D;
import yaphyre.shaders.Material;
import yaphyre.shaders.MaterialBuilder;
import yaphyre.shapes.Sphere;
import yaphyre.util.Color;

public class SphereTest {

	private Shape createTestSphere() {
		Shader testShader = mock(Shader.class);
		return Sphere.createSphere(new Point3D(2d, 0d, 0d), 1d, true, testShader);
	}

	/**
	 * Creates a sphere at the coordinate origin with a radius of 1.
	 *
	 * @return The unit sphere.
	 */
	private Shape createUnitSphere() {
		Shader shader = mock(Shader.class);
		return Sphere.createSphere(Point3D.ORIGIN, 1d, true, shader);
	}

	@Test
	public void testSphere() {
		Shader shader = mock(Shader.class);
		Point3D origin = mock(Point3D.class);
		when(origin.getX()).thenReturn(1d);
		when(origin.getY()).thenReturn(1d);
		when(origin.getZ()).thenReturn(1d);
		Sphere s = Sphere.createSphere(origin, 1d, true, shader);
		System.out.println("New sphere created: " + s);
		assertNotNull(s);
	}

	@Test
	public void testStaticSphereConstructor() {

		Transformation objectToWorld = Transformation.scale(2, 2, 2);

		Sphere staticSphere = Sphere.createSphere(Point3D.ORIGIN, 2, true, new TestShader());
		Sphere constructorSphere = new Sphere(objectToWorld, 0d, 360d, 0d, 180d, true, new TestShader());

		Ray testRay = new Ray(new Point3D(-10, 0, 0), Vector3D.X);

		Point3D intersectPoint1 = staticSphere.getIntersectionPoint(testRay);
		Point3D intersectPoint2 = constructorSphere.getIntersectionPoint(testRay);

		assertEquals(constructorSphere, staticSphere);
		assertEquals(intersectPoint2, intersectPoint1);
		assertEquals(constructorSphere.getNormal(intersectPoint2).asVector().normalize(), staticSphere.getNormal(intersectPoint1).asVector().normalize());

		// sphere with radius 3 at the coordinates [0, 1, 1]
		Matrix initMatrix = new Matrix(new double[][]{{3, 0, 0, 0},
				{0, 3, 0, 1},
				{0, 0, 3, 1},
				{0, 0, 0, 1}});

		objectToWorld = new Transformation(initMatrix);
		staticSphere = Sphere.createSphere(new Point3D(0, 1, 1), 3, true, new TestShader());
		constructorSphere = new Sphere(objectToWorld, 0d, 360d, 0d, 180d, true, new TestShader());
		testRay = new Ray(new Point3D(-10, 1.5, 1), Vector3D.X);

		intersectPoint1 = staticSphere.getIntersectionPoint(testRay);
		intersectPoint2 = constructorSphere.getIntersectionPoint(testRay);

		assertEquals(constructorSphere, staticSphere);
		assertEquals(intersectPoint2, intersectPoint1);
		assertEquals(constructorSphere.getNormal(intersectPoint2).asVector().normalize(), staticSphere.getNormal(intersectPoint1).asVector().normalize());

	}

	@Test
	public void testGetIntersectDistance() {
		Ray intersectingRay = new Ray(Point3D.ORIGIN, Normal3D.NORMAL_X.asVector());
		Ray nonIntersectingRay = new Ray(Point3D.ORIGIN, Normal3D.NORMAL_Y.asVector());
		Ray crookedRay = new Ray(Point3D.ORIGIN, new Vector3D(1, 0.25, 0.25).normalize());
		Shape testSphere = createTestSphere();

		double distance;

		distance = testSphere.getIntersectDistance(nonIntersectingRay);
		System.out.println(testSphere + " intersects with " + nonIntersectingRay + " at a distance of " + distance);
		assertEquals(Shape.NO_INTERSECTION, distance, 0);

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

		Shape testSphere = createTestSphere();

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
	public void testGetShader() {
		Shape s = createTestSphere();
		assertNotNull(s.getShader());
	}

	@Test
	public void testGetNormal() {
		Shape testSphere = createTestSphere();
		Shape unitSphere = createUnitSphere();

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

	@SuppressWarnings("serial")
	private static class TestShader implements Shader {
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

		@Override
		public int hashCode() {
			return this.getClass().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return (this == obj) || (obj instanceof TestShader);
		}

	}

}
