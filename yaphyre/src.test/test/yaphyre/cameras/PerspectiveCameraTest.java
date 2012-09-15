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

package test.yaphyre.cameras;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import yaphyre.cameras.AbstractCamera.BaseCameraSettings;
import yaphyre.cameras.PerspectiveCamera;
import yaphyre.cameras.PerspectiveCamera.PerspectiveCameraSettings;
import yaphyre.films.ImageFile;
import yaphyre.geometry.Point3D;

public class PerspectiveCameraTest {

	@Test
	public void testGetCameraRay() {
		fail("Not yet implemented");
	}

	@Test
	public void testPerspectiveCamera() {
		BaseCameraSettings baseSettings = BaseCameraSettings.create(new Point3D(0, 0, -1), Point3D.ORIGIN);
		PerspectiveCameraSettings perspectiveSettings = PerspectiveCameraSettings.create(4d / 3d, 50d);

		PerspectiveCamera camera = new PerspectiveCamera(baseSettings, perspectiveSettings, new ImageFile(640, 480));

		System.out.println("Testing camera: ".concat(camera.toString()));

		assertNotNull(camera);
		assertNotNull(camera.getFilm());

	}

}
