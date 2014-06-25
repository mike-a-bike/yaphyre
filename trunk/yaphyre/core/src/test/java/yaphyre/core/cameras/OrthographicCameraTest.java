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

import java.util.Arrays;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;
import yaphyre.core.api.CameraSample;
import yaphyre.core.api.Film;
import yaphyre.core.api.Sampler;
import yaphyre.core.api.Scene;
import yaphyre.core.api.Tracer;
import yaphyre.core.math.Color;
import yaphyre.core.math.Point2D;
import yaphyre.core.math.Ray;
import yaphyre.core.math.Vector3D;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static yaphyre.core.math.MathUtils.EPSILON;

/**
 * Test cases for the OrthographicCamera class.
 *
 * @author Michael Bieri
 * @since 18.02.14
 */
public class OrthographicCameraTest {

    /** Dimensions used for all tests. */
    private static final double DIMENSION = 10;

    /** Size of the film in x dimension. */
    public static final int X_SIZE = 4;

    /** Size of the film in y dimension. */
    public static final int Y_SIZE = 3;

    /** Default location of the camera on the z axis */
    private static final double Z_COORDINATE = 100d;

    /** Instance for all test methods. */
    private OrthographicCamera testCamera;

    /** Mocked tracer instance for verification. */
    private Tracer tracer;

    /** Mocked sampler instance for verification. */
    private Sampler sampler;

    /** Mocked film instance for verification. */
    private Film film;

    /**
     * Setup the object under test.
     */
    @Before
    public void setupTestCamera() {
        film = mock(Film.class);
        when(film.getNativeResolution()).thenReturn(new Pair<>(X_SIZE, Y_SIZE));
        tracer = mock(Tracer.class);
        when(tracer.traceRay(any(Ray.class), any(Scene.class))).thenReturn(Color.BLACK);
        sampler = mock(Sampler.class);
        when(sampler.getUnitSquareSamples()).thenReturn(Arrays.asList(new Point2D(.5d, .5d)));
        testCamera = new OrthographicCamera(film, DIMENSION, DIMENSION, Z_COORDINATE);
        testCamera.setTracer(tracer);
        testCamera.setSampler(sampler);
    }

    @Test
    public void testCreateCameraRay() throws Exception {
        // corner case [0, 0]
        Ray cameraRay = testCamera.createCameraRay(new Point2D(0d, 0d));
        assertEquals(-DIMENSION / 2d, cameraRay.getOrigin().getX(), EPSILON);
        assertEquals(-DIMENSION / 2d, cameraRay.getOrigin().getY(), EPSILON);
        assertEquals(Z_COORDINATE, cameraRay.getOrigin().getZ(), EPSILON);
        assertEquals(cameraRay.getDirection(), Vector3D.Z.neg());

        // corner case [1, 1]
        cameraRay = testCamera.createCameraRay(new Point2D(1d, 1d));
        assertEquals(DIMENSION / 2d, cameraRay.getOrigin().getX(), EPSILON);
        assertEquals(DIMENSION / 2d, cameraRay.getOrigin().getY(), EPSILON);
        assertEquals(Z_COORDINATE, cameraRay.getOrigin().getZ(), EPSILON);

        // case [0.5, 0.5] --> [0, 0, 0]
        cameraRay = testCamera.createCameraRay(new Point2D(0.5d, 0.5d));
        assertEquals(0d, cameraRay.getOrigin().getX(), EPSILON);
        assertEquals(0d, cameraRay.getOrigin().getY(), EPSILON);
        assertEquals(Z_COORDINATE, cameraRay.getOrigin().getZ(), EPSILON);
    }

    @Test
    public void testRenderScene() throws Exception {
        Scene scene = mock(Scene.class);
        testCamera.renderScene(scene);
        verify(sampler, times(X_SIZE * Y_SIZE)).getUnitSquareSamples();
        verify(tracer, times(X_SIZE * Y_SIZE)).traceRay(any(Ray.class), any(Scene.class));
        verify(film, times(X_SIZE * Y_SIZE)).addCameraSample(any(CameraSample.class));
    }
}
