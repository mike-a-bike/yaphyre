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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import yaphyre.core.Shader;
import yaphyre.core.Shape;
import yaphyre.geometry.Point3D;

import org.junit.Ignore;
import org.junit.Test;

public class SphereTest {

	//region test data creation
	private Shape createTestSphere() {
		Shader testShader = mock(Shader.class);
		return Sphere.createSphere(new Point3D(2d, 0d, 0d), 1d, testShader);
	}

	private Shape createUnitSphere() {
		Shader shader = mock(Shader.class);
		return Sphere.createSphere(Point3D.ORIGIN, 1d, shader);
	}
	//endregion

	@Test
	@Ignore
	public void testSphere() {
		fail("Not implemented yet");
	}

	@Test
	@Ignore
	public void testStaticSphereConstructor() {
		fail("Not implemented yet");
	}

	@Test
	public void testGetShader() {
		Shape s = createUnitSphere();
		assertNotNull(s.getShader());
	}

}
