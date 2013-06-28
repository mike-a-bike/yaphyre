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

package yaphyre.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ColorTest {

	private static final Color RED = new Color(1, 0, 0);

	private static final Color GREEN = new Color(0, 1, 0);

	private static final Color BLUE = new Color(0, 0, 1);

	private static final Color CYAN = new Color(0, 1, 1);

	private static final Color MAGENTA = new Color(1, 0, 1);

	private static final Color YELLOW = new Color(1, 1, 0);

	private static final Color WHITE = new Color(1, 1, 1);

	private static final Color BLACK = new Color(0, 0, 0);

	private static final Color GRAY75 = new Color(0.75, 0.75, 0.75);

	private static final Color GRAY50 = new Color(0.5, 0.5, 0.5);

	private static final Color GRAY25 = new Color(0.25, 0.25, 0.25);

	@Test
	public void testFromByteValue() {

		double doubleValue = Color.fromByteValue(0);
		assertEquals(0, doubleValue, 0);

		doubleValue = Color.fromByteValue(255);
		assertEquals(1, doubleValue, 0);

		doubleValue = Color.fromByteValue(510);
		assertEquals(2, doubleValue, 0);

	}

	@Test
	public void testToByteValue() {

		int byteValue = Color.toByteValue(1);
		assertEquals(255, byteValue);

		byteValue = Color.toByteValue(0);
		assertEquals(0, byteValue);

		byteValue = Color.toByteValue(0.5d);
		assertEquals(127, byteValue);

		byteValue = Color.toByteValue(2d);
		assertEquals(510, byteValue);
	}

	@Test
	public void testColor() {

		Color color = new Color(java.awt.Color.RED);
		assertEquals(RED, color);

		color = new Color(java.awt.Color.BLACK);
		assertEquals(BLACK, color);

		color = new Color(1, 1, 1);
		assertEquals(WHITE, color);

		color = new Color(0, 0, 1);
		assertEquals(BLUE, color);

		color = new Color(0, 0, 0);
		assertEquals(BLACK, color);

	}

	@Test(expected = NullPointerException.class)
	public void testColorFail() {
		new Color(null);
	}

	@SuppressWarnings("EqualsBetweenInconvertibleTypes")
	@Test
	public void testEquals() {

		Color c1 = BLACK;
		Color c2 = null;
		assertFalse(c1.equals(c2));
		assertFalse(c1.equals("Hello, World"));
		assertTrue(c1.equals(c1));

		c2 = new Color(0, 0, 0);
		assertTrue(c1.equals(c2));
		assertTrue(c2.equals(c1));
		assertEquals(c1.hashCode(), c2.hashCode());

		c2 = new Color(1, 1, 1);
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		assertFalse(c1.hashCode() == c2.hashCode());

	}

	@Test
	public void testMultiplyScalar() {

		Color color = WHITE.multiply(0);
		assertEquals(BLACK, color);

		color = BLACK.multiply(1);
		assertEquals(BLACK, color);

		color = WHITE.multiply(0.25d);
		assertEquals(GRAY25, color);

		color = WHITE.multiply(0.5);
		assertEquals(GRAY50, color);

		color = WHITE.multiply(0.75);
		assertEquals(GRAY75, color);

	}

	@Test
	public void testMultiplyColor() {

		Color color = WHITE.multiply(WHITE);
		assertEquals(WHITE, color);

		color = WHITE.multiply(RED);
		assertEquals(RED, color);

		color = WHITE.multiply(BLACK);
		assertEquals(BLACK, color);

		color = WHITE.multiply(GRAY50);
		assertEquals(GRAY50, color);

		color = GRAY50.multiply(GRAY50);
		assertEquals(GRAY25, color);

	}

	@Test
	public void testAdd() {

		Color yellow = RED.add(GREEN);
		assertEquals(YELLOW, yellow);

		Color cyan = GREEN.add(BLUE);
		assertEquals(CYAN, cyan);

		Color magenta = RED.add(BLUE);
		assertEquals(MAGENTA, magenta);

		Color red = RED.add(BLACK);
		assertEquals(RED, red);
	}

	@Test
	public void testClip() {

		Color color = WHITE.clip();
		assertEquals(WHITE, color);

		color = BLACK.clip();
		assertEquals(BLACK, color);

		color = new Color(2, 2, 2).clip();
		assertEquals(WHITE, color);

		color = new Color(2, 0, 0).clip();
		assertEquals(RED, color);

	}

	@Test
	public void testRescale() {

		Color color = WHITE.rescale();
		assertEquals(WHITE, color);

		color = BLACK.rescale();
		assertEquals(BLACK, color);

		color = new Color(2d, 0, 0).rescale();
		assertEquals(RED, color);

		Color refColor = new Color(1d / 3d, 2d / 3d, 3d / 3d);
		color = new Color(1, 2, 3).rescale();
		assertEquals(refColor, color);

	}

}
