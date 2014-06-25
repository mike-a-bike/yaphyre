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

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 04.07.13
 */
public class VectorMathTest {
	@Test
	public void testReflect() throws Exception {

		Vector3D wi = Vector3D.X;
		Normal3D n = Vector3D.X.neg().asNormal();

		Vector3D wo = VectorMath.reflect(wi, n);

		assertEquals(wi.neg(), wo);

	}

}
