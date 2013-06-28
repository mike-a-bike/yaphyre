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

package yaphyre.geometry;

import org.junit.Test;

import java.text.MessageFormat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class VectorTest {

	@Test
	public void testNewVector() {
		Vector3D v = new Vector3D(0d, 1d, 2d);
		assertNotNull(v);
		assertEquals(0d, v.getX(), 0);
		assertEquals(1d, v.getY(), 0);
		assertEquals(2d, v.getZ(), 0);
	}

	@Test
	public void testVectorWithPoints() {
		Point3D startPoint = new Point3D(1d, 1d, 1d);
		Point3D endPoint = new Point3D(2d, 2d, 2d);
		Vector3D v = new Vector3D(startPoint, endPoint);
		assertNotNull(v);
		assertEquals(new Vector3D(1, 1, 1), v);
	}

	@SuppressWarnings({"EqualsBetweenInconvertibleTypes", "ObjectEqualsNull"})
	@Test
	public void testEqualsObject() {
		Vector3D v1 = new Vector3D(0d, 0d, 0d);
		Vector3D v2 = new Vector3D(1d, 2d, 3d);
		assertTrue(v1.equals(v1));
		assertFalse(v1.equals(null));
		assertFalse(v1.equals("Hello, World"));
		assertTrue(v1.equals(Vector3D.NULL));
		assertFalse(v1.equals(v2));
		assertFalse(v2.equals(Vector3D.X));
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
		Vector3D v1 = new Vector3D(1d, 1d, 1d);
		Vector3D v2 = new Vector3D(2d, 2d, 2d);

		Vector3D result = v1.add(v2);
		assertEquals(3d, result.getX(), 0);
		assertEquals(3d, result.getY(), 0);
		assertEquals(3d, result.getZ(), 0);

		result = Vector3D.X.add(Vector3D.Y).add(Vector3D.Z);
		assertEquals(1d, result.getX(), 0);
		assertEquals(1d, result.getY(), 0);
		assertEquals(1d, result.getZ(), 0);
	}

	@Test
	public void testSub() {
		Vector3D v1 = new Vector3D(1d, 1d, 1d);
		Vector3D v2 = new Vector3D(2d, 2d, 2d);

		Vector3D result = v1.sub(v2);
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

		double length = Vector3D.NULL.length();
		System.out.println("Length of " + Vector3D.NULL + " is: " + length);
		assertEquals(0d, length, 0);

		length = Vector3D.X.length();
		System.out.println("Length of " + Vector3D.X + " is: " + length);
		assertEquals(1d, length, 0);

		length = Vector3D.Y.length();
		System.out.println("Length of " + Vector3D.Y + " is: " + length);
		assertEquals(1d, length, 0);

		length = Vector3D.Z.length();
		System.out.println("Length of " + Vector3D.Z + " is: " + length);
		assertEquals(1d, length, 0);

		Vector3D v = new Vector3D(1d, 1d, 1d);
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

		Vector3D v = Normal3D.NORMAL_X.asVector();
		Vector3D result = v.normalize();
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

		Vector3D v = Vector3D.NULL;
		double scalar = 100d;
		Vector3D result = v.scale(scalar);
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

		Vector3D v1 = Normal3D.NORMAL_X.asVector();
		Vector3D v2 = Normal3D.NORMAL_Y.asVector();
		double result = v1.dot(v2);
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

		// result is the z axis normal since the cross product of the x-y plane lies
		// on the z-axis.
		Vector3D v1 = Vector3D.X;
		Vector3D v2 = Vector3D.Y;
		Vector3D result = v1.cross(v2);
		System.out.println(v1 + " x " + v2 + " = " + result);
		assertEquals(Vector3D.Z, result);

		v1 = Vector3D.Y;
		v2 = Vector3D.X;
		result = v1.cross(v2);
		System.out.println(v1 + " x " + v2 + " = " + result);
		assertEquals(Vector3D.Z.neg(), result);

		v1 = Vector3D.Y;
		v2 = Vector3D.Z;
		result = v1.cross(v2);
		System.out.println(v1 + " x " + v2 + " = " + result);
		assertEquals(Vector3D.X, result);

		v1 = Vector3D.X;
		v2 = Vector3D.Z;
		result = v1.cross(v2);
		System.out.println(v1 + " x " + v2 + " = " + result);
		assertEquals(Vector3D.Y.neg(), result);

		v1 = new Vector3D(2, 0, 0);
		v2 = new Vector3D(0, 10, 0);
		result = v1.cross(v2);
		System.out.println(v1 + " x " + v2 + " = " + result);
		// The new vector should have a length of 2 * 10
		// and a direction along the z-axis
		assertEquals(20d, result.length(), 0);
		assertEquals(Vector3D.Z, result.normalize());

	}

	@Test
	public void testToString() {

		String stringRep = Vector3D.NULL.toString();

		assertEquals("Vector3D{x=0.0, y=0.0, z=0.0}", stringRep);
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
