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

import org.junit.Test;
import yaphyre.core.Shader;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Transformation;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 24.03.13
 */
public class BezierPatchTest {

	@Test
	public void testCreateTriangleIndices() throws Exception {
		BezierPatch patch = new BezierPatch(mock(Transformation.class), mock(Shader.class), 1, 1, 0, new Point3D[]{Point3D.ORIGIN});
		final int uSegments = 4;
		final int vSegments = 3;
		int[][] triangleIndices = patch.createTriangleIndices(uSegments, vSegments);
		assertTrue(triangleIndices.length == uSegments * vSegments * 2);
		for (int triangleIndex = 0; triangleIndex < triangleIndices.length; triangleIndex++) {
			System.out.printf("triangle %2d: a=%2d, b=%2d, c=%2d%n", triangleIndex,
					triangleIndices[triangleIndex][0],
					triangleIndices[triangleIndex][1],
					triangleIndices[triangleIndex][2]);
		}
	}
}
