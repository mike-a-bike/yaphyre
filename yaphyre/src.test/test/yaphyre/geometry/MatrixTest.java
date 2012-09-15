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

package test.yaphyre.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import yaphyre.geometry.MathUtils;
import yaphyre.geometry.Matrix;

public class MatrixTest {

	private Matrix createTestMatrix() {
		return new Matrix(1, 2, 3, 4,
				5, 6, 7, 8,
				9, 10, 11, 12,
				13, 14, 15, 16);
	}

	@Test
	public void testMatrix() {
		Matrix M = createTestMatrix();
		assertNotNull(M);
		assertEquals(1d, M.get(0, 0), 0d);
		assertEquals(16d, M.get(3, 3), 0d);
	}

	@Test
	public void testMatrixDoubleArrays() {
		Matrix M = createTestMatrix();
		assertNotNull(M);
		assertEquals(1d, M.get(0, 0), 0d);
		assertEquals(16d, M.get(3, 3), 0d);
	}

	@Test
	public void testMatrixAdd() {
		Matrix M1 = createTestMatrix();
		Matrix M2 = createTestMatrix();
		Matrix Mr = M1.add(M2);
		Matrix R = new Matrix(2, 4, 6, 8,
				10, 12, 14, 16,
				18, 20, 22, 24,
				26, 28, 30, 32);

		assertEquals(R, Mr);

	}

	@Test
	public void testMatrixMulScalar() {
		Matrix M = createTestMatrix();
		Matrix Mr = M.mul(2);
		Matrix R = new Matrix(2, 4, 6, 8,
				10, 12, 14, 16,
				18, 20, 22, 24,
				26, 28, 30, 32);

		assertEquals(R, Mr);

		M = createTestMatrix();
		Mr = M.mul(1d / 2d);
		R = new Matrix(0.5, 1, 1.5, 2,
				2.5, 3, 3.5, 4,
				4.5, 5, 5.5, 6,
				6.5, 7, 7.5, 8);

		assertEquals(R, Mr);

	}

	@Test
	public void testMatrixMulMatrix() {
		Matrix M1 = new Matrix(3, 0, 0, 0,
				0, 4, 0, 0,
				0, 0, 5, 0,
				0, 0, 0, 1);

		Matrix M2 = new Matrix(1, 0, 0, -1,
				0, 1, 0, -2,
				0, 0, 1, -3,
				0, 0, 0, 1);

		Matrix Mr = new Matrix(3, 0, 0, -1,
				0, 4, 0, -2,
				0, 0, 5, -3,
				0, 0, 0, 1);

		assertEquals(Mr, M2.mul(M1));

	}

	@Test
	public void testTransposed() {
		Matrix M = createTestMatrix();
		Matrix Mt = M.transpose();
		Matrix R = new Matrix(1, 5, 9, 13,
				2, 6, 10, 14,
				3, 7, 11, 15,
				4, 8, 12, 16);

		assertEquals(R, Mt);
	}

	@Test
	public void testIsInvertible() {
		Matrix M;

		M = new Matrix(1, 7, 4, 2, 0, 9, 2, 5, 2, 2, 3, 2, 9, 9, 9, 9);
		assertTrue(M.isInvertible());
		assertNotNull(M.inverse());
		assertFalse(Math.abs(M.getDeterminat()) < MathUtils.EPSILON);

		M = createTestMatrix();
		assertFalse(M.isInvertible());
		assertTrue(M.inverse() == null);
		assertTrue(Math.abs(M.getDeterminat()) < MathUtils.EPSILON);
	}

	@Test
	public void testInverse() {
		Matrix M = new Matrix(1, 7, 4, 2,
				0, 9, 2, 5,
				2, 2, 3, 2,
				9, 9, 9, 9);
		Matrix Mi = M.inverse();
		Matrix Mr = M.mul(Mi);

		assertEquals(Matrix.IDENTITY, Mr);

		M = new Matrix(1, 0, 0, 1,
				0, 1, 0, 1,
				0, 0, 1, 1,
				0, 0, 0, 1);
		Mi = M.inverse();
		Mr = M.mul(Mi);

		assertEquals(Matrix.IDENTITY, Mr);

		M = new Matrix(2, 0, 0, 0,
				0, 2, 0, 0,
				0, 0, 2, 0,
				0, 0, 0, 1);
		Mi = M.inverse();
		Mr = M.mul(Mi);

		assertEquals(Matrix.IDENTITY, Mr);

		M = new Matrix(3, 0, 0, 1,
				0, 3, 0, 1,
				0, 0, 3, 1,
				0, 0, 0, 1);
		Mi = M.inverse();
		Mr = M.mul(Mi);

		assertEquals(Matrix.IDENTITY, Mr);
	}
}
