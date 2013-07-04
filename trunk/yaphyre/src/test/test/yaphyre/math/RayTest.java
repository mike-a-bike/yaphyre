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

package yaphyre.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RayTest {

	@Test
	public void testRay() {
		Point3D origin = new Point3D(1, 1, 1);
		Vector3D direction = Normal3D.NORMAL_X.asVector();

		Ray r = new Ray(origin, direction);
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

		Ray r = new Ray(origin, direction);
		assertNotNull(r);
		assertNotNull(r.getDirection());
		assertEquals(Normal3D.NORMAL_X.asVector(), r.getDirection());

	}

}
