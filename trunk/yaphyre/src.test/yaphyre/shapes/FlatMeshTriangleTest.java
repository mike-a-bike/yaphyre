/*
 * Copyright 2013 Michael Bieri
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

package yaphyre.shapes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.Random;

import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector3D;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA. User: michael Date: 27.01.13 Time: 11:50 To change this template use File | Settings |
 * File Templates.
 */
public class FlatMeshTriangleTest {

	private final Random RAND = new Random(System.currentTimeMillis());

	@Test
	@Ignore
	public void testGetIntersectDistance() throws Exception {
		fail("not implemented yet");
	}

	@Test
	@Ignore
	public void testGetNormal() throws Exception {
		fail("not implemented yet");

	}

	@Test
	@Ignore
	public void testGetMappedSurfacePoint() throws Exception {
		fail("not implemented yet");

	}

	@Test
	public void testIsHitBy() throws Exception {
		FlatMeshTriangle t = FlatMeshTriangle.create(Point3D.ORIGIN, Point3D.ORIGIN.add(Vector3D.X), Point3D.ORIGIN.add(Vector3D.Y));
		Ray r = new Ray(new Point3D(0.25d, 0.25d, 1), Vector3D.Z.neg());

		assertTrue(t.isHitBy(r));

	}

	@Test
	@Ignore
	public void testIntersect() throws Exception {
		fail("not implemented yet");

	}

	@Test
	@Ignore
	public void testGetIntersectionPoint() throws Exception {
		fail("not implemented yet");

	}
}
