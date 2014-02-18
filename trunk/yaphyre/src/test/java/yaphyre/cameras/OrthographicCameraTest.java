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

package yaphyre.cameras;

import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import yaphyre.core.Film;
import yaphyre.math.Point2D;
import yaphyre.math.Ray;
import yaphyre.math.Vector3D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static yaphyre.math.MathUtils.EPSILON;

/**
 * YaPhyRe
 *
 * @author mike0041@gmail.com
 * @since 18.02.14
 */
public class OrthographicCameraTest {

    /** Dimensions used for all tests. */
    private static final int DIMENSION = 10;

    /** Instance for all test methods. */
    private OrthographicCamera testCamera;

    /**
     * Setup the object under test.
     */
    @Before
    public void setupTestCamera() {
        Film film = mock(Film.class);
        when(film.getNativeResolution()).thenReturn(new Pair<>(4, 3));
        testCamera = new OrthographicCamera<>(film, DIMENSION, DIMENSION);
    }

    @Test
    public void testCreateCameraRay() throws Exception {
        Ray cameraRay = testCamera.createCameraRay(new Point2D(0d, 0d));
        assertEquals(-DIMENSION / 2d, cameraRay.getOrigin().getX(), EPSILON);
        assertEquals(-DIMENSION / 2d, cameraRay.getOrigin().getY(), EPSILON);
        assertEquals(0d, cameraRay.getOrigin().getZ(), EPSILON);
        assertEquals(cameraRay.getDirection(), Vector3D.Z.neg());

        cameraRay = testCamera.createCameraRay(new Point2D(1d, 1d));
        assertEquals(DIMENSION / 2d, cameraRay.getOrigin().getX(), EPSILON);
        assertEquals(DIMENSION / 2d, cameraRay.getOrigin().getY(), EPSILON);
        assertEquals(0d, cameraRay.getOrigin().getZ(), EPSILON);

        cameraRay = testCamera.createCameraRay(new Point2D(0.5d, 0.5d));
        assertEquals(0d, cameraRay.getOrigin().getX(), EPSILON);
        assertEquals(0d, cameraRay.getOrigin().getY(), EPSILON);
        assertEquals(0d, cameraRay.getOrigin().getZ(), EPSILON);
    }

    @Test
    @Ignore
    public void testRenderScene() throws Exception {
        fail("Not implemented yet");
    }
}
