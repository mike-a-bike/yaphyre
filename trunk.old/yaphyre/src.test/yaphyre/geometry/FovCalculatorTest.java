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

import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FovCalculatorTest {
	@Test
	public void testCalculateVerticalFov() throws Exception {
		final double focalLength = 50d;
		final double fullFrameFov = FovCalculator.FullFrame35mm.calculateVerticalFov(focalLength);
		final double apshFov = FovCalculator.APS_H.calculateVerticalFov(focalLength);
		final double apscFov = FovCalculator.APS_C.calculateVerticalFov(focalLength);

		System.out.printf("Vertical Field of View for %.0fmm: FullFrame: %.3f, APS-H: %.3f, APS-C: %.3f%n", focalLength, toDegrees(fullFrameFov), toDegrees(apshFov), toDegrees(apscFov));

		// rough test if the expected values lies within range (full frame fov is 26.991...)
		assertTrue(toRadians(27d) > fullFrameFov && fullFrameFov > apshFov && apshFov > apscFov);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCalculateVerticalFovFail() throws Exception {
		FovCalculator.FullFrame35mm.calculateVerticalFov(0d);
	}

	@Test
	public void testCalculateHorizontalFov() throws Exception {
		final double focalLength = 50d;
		final double fullFrameFov = FovCalculator.FullFrame35mm.calculateHorizontalFov(focalLength);
		final double apshFov = FovCalculator.APS_H.calculateHorizontalFov(focalLength);
		final double apscFov = FovCalculator.APS_C.calculateHorizontalFov(focalLength);

		System.out.printf("Horizontal Field of View for %.0fmm: FullFrame: %.3f, APS-H: %.3f, APS-C: %.3f%n", focalLength, toDegrees(fullFrameFov), toDegrees(apshFov), toDegrees(apscFov));

		// rough test if the expected values lies within range (full frame fov is 39.598...)
		assertTrue(toRadians(40d) > fullFrameFov && fullFrameFov > apshFov && apshFov > apscFov);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCalculateHorizontalFovFail() throws Exception {
		FovCalculator.FullFrame35mm.calculateHorizontalFov(0d);
	}
}
