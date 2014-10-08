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

package yaphyre.core.lights;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import yaphyre.core.api.CollisionInformation;
import yaphyre.core.api.Scene;
import yaphyre.core.math.Color;
import yaphyre.core.math.Point3D;
import yaphyre.core.math.Ray;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the PointLight class.
 *
 * @author Michael Bieri
 * @since 25.09.2014
 */
public class PointLightTest {

    // mocks
    private CollisionInformation collisionInformation;
    private Ray ray;
    private Scene scene;

    // class under test
    private PointLight light;

    @Before
    public void setUp() throws Exception {

        // Setup some mocks
        scene = mock(Scene.class);
        collisionInformation = mock(CollisionInformation.class);
        ray = mock(Ray.class);

        // Init the subject under test
        light = new PointLight(100d, Color.WHITE, Point3D.ORIGIN);
        light.setScene(scene);

    }

    @Test
    public void testCalculateIntensityForShadowRayWithCollision() throws Exception {

        // Given: a shadow ray with no collision
        when(scene.hitObjectForShadowRay(ray)).thenReturn(Optional.empty());
        when(ray.getOrigin()).thenReturn(Point3D.ORIGIN);

        // When: calculating the light intensity
        Color color = light.calculateIntensityForShadowRay(ray);

        // Then: The resulting color is not BLACK -> light contributes to the overall intensity
        assertFalse(Color.BLACK.equals(color));

    }

    @Test
    public void testCalculateIntensityForShadowRayWithoutCollision() throws Exception {

        // Given: a shadow ray with a collision
        when(scene.hitObjectForShadowRay(ray)).thenReturn(Optional.of(collisionInformation));

        // When: calculating the light intensity
        Color color = light.calculateIntensityForShadowRay(ray);

        // Then: The resulting color is BLACK -> light does not contribute to the overall intensity
        assertEquals(Color.BLACK, color);

    }
}