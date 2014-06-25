/*
 * Copyright 2013 Michael Bieri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yaphyre.core.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static yaphyre.core.math.MathUtils.EPSILON;
import static yaphyre.core.math.Point2D.ZERO;

public class Point2DTest {
	@Test
	public void testAddPoint2D() throws Exception {
		Point2D p1 = ZERO;
		Point2D p2 = new Point2D(1d, 1d);
		Point2D p3 = new Point2D(-1d, -1d);

		Point2D result = p1.add(p2);

		assertEquals(1d, result.getU(), EPSILON);
		assertEquals(1d, result.getV(), EPSILON);

		result = p1.add(p1);

		assertEquals(0d, result.getU(), EPSILON);
		assertEquals(0d, result.getV(), EPSILON);

		result = p2.add(p2);

		assertEquals(2d, result.getU(), EPSILON);
		assertEquals(2d, result.getV(), EPSILON);

		result = p1.add(p3);

		assertEquals(-1d, result.getU(), EPSILON);
		assertEquals(-1d, result.getV(), EPSILON);

	}

	@Test
	public void testAddDiscrete() throws Exception {
		Point2D p1 = ZERO;

		Point2D result = p1.add(1d, 1d);

		assertEquals(1d, result.getU(), EPSILON);
		assertEquals(1d, result.getV(), EPSILON);

		result = p1.add(0d, 0d);

		assertEquals(0d, result.getU(), EPSILON);
		assertEquals(0d, result.getV(), EPSILON);

		result = p1.add(-1d, -1d);

		assertEquals(-1d, result.getU(), EPSILON);
		assertEquals(-1d, result.getV(), EPSILON);

	}
}
