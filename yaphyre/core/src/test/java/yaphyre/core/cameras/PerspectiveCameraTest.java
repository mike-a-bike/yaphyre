/*
 * Copyright 2014 Michael Bieri
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

package yaphyre.core.cameras;

import org.junit.Test;
import yaphyre.core.api.Film;
import yaphyre.core.math.FovCalculator;
import yaphyre.core.math.Normal3D;
import yaphyre.core.math.Point2D;
import yaphyre.core.math.Point3D;
import yaphyre.core.math.Ray;
import yaphyre.core.math.Vector3D;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static yaphyre.core.math.MathUtils.EPSILON;

public class PerspectiveCameraTest {

    @Test
    public void testCreateCameraRay() throws Exception {
        Film film = mock(Film.class);
        FovCalculator fovCalculator = FovCalculator.FullFrame35mm;
        PerspectiveCamera camera = new PerspectiveCamera(
                film, null,
                Point3D.ORIGIN, Point3D.ORIGIN.add(Vector3D.Z), Normal3D.NORMAL_Y,
                fovCalculator.calculateHorizontalFov(50d),
                fovCalculator.getAspectRatio(),
                EPSILON, 1d / EPSILON
        );

        Ray cameraRay;

        cameraRay = camera.createCameraRay(new Point2D(.5d, .5d));
        Vector3D direction = cameraRay.getDirection();
        assertEquals(0d, direction.getX(), EPSILON);
        assertEquals(0d, direction.getY(), EPSILON);
        assertEquals(1d, direction.getZ(), EPSILON);

        cameraRay = camera.createCameraRay(new Point2D(0d, .5d));
        direction = cameraRay.getDirection();
        assertEquals(1d, direction.length(), EPSILON);

        cameraRay = camera.createCameraRay(new Point2D(1d, .5d));
        direction = cameraRay.getDirection();
        assertEquals(1d, direction.length(), EPSILON);

        cameraRay = camera.createCameraRay(new Point2D(.5d, .0d));
        direction = cameraRay.getDirection();
        assertEquals(1d, direction.length(), EPSILON);

        cameraRay = camera.createCameraRay(new Point2D(.5d, 1d));
        direction = cameraRay.getDirection();
        assertEquals(1d, direction.length(), EPSILON);

        cameraRay = camera.createCameraRay(new Point2D(0d, 0d));
        direction = cameraRay.getDirection();
        assertEquals(1d, direction.length(), EPSILON);

        cameraRay = camera.createCameraRay(new Point2D(1d, 1d));
        direction = cameraRay.getDirection();
        assertEquals(1d, direction.length(), EPSILON);
    }
}