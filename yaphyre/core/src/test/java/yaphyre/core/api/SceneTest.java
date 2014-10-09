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

package yaphyre.core.api;

import java.util.Optional;

import com.google.inject.Injector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import yaphyre.core.math.BoundingBox;
import yaphyre.core.math.Ray;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for verifying the different hit functions.
 */
@RunWith(MockitoJUnitRunner.class)
public class SceneTest {

    @Mock
    private Injector injector;
    @Mock
    private Shape shape;
    @Mock
    private BoundingBox boundingBox;
    @Mock
    private CollisionInformation collisionInformation;
    @Mock
    private Ray ray;

    private Scene scene;

    @Before
    public void setUp() throws Exception {
        when(shape.getBoundingBox()).thenReturn(boundingBox);
        scene = new Scene(injector);
        scene.addShape(shape);
    }

    @Test
    public void testHitObjectAndMissBoundingBox() throws Exception {

        when(boundingBox.isHitBy(any())).thenReturn(false);

        Optional<CollisionInformation> hitObject = scene.hitObject(ray);

        assertNotNull(hitObject);
        assertFalse(hitObject.isPresent());
        verify(boundingBox).isHitBy(ray);
        verify(shape, never()).intersect(ray);

    }

    @Test
    public void testHitObjectAndMissShape() throws Exception {

        when(boundingBox.isHitBy(any())).thenReturn(true);
        when(shape.intersect(any())).thenReturn(Optional.empty());

        Optional<CollisionInformation> hitObject = scene.hitObject(ray);

        assertNotNull(hitObject);
        assertFalse(hitObject.isPresent());
        verify(boundingBox).isHitBy(ray);
        verify(shape).intersect(ray);

    }

    @Test
    public void testHitObjectAndHit() throws Exception {

        when(collisionInformation.getDistance()).thenReturn(10d);
        when(boundingBox.isHitBy(any())).thenReturn(true);
        when(shape.intersect(any())).thenReturn(Optional.of(collisionInformation));

        Optional<CollisionInformation> hitObject = scene.hitObject(ray);

        assertNotNull(hitObject);
        assertTrue(hitObject.isPresent());
        assertEquals(10d, hitObject.get().getDistance(), 0d);
        verify(boundingBox).isHitBy(ray);
        verify(shape).intersect(ray);
    }

    @Test
    public void testHitObjectAndHitMultipleShapes() throws Exception {

        CollisionInformation mockCollisionInformation = mock(CollisionInformation.class);
        BoundingBox mockBoundingBox = mock(BoundingBox.class);
        Shape mockShape = mock(Shape.class);
        scene.addShape(mockShape);
        when(mockShape.getBoundingBox()).thenReturn(mockBoundingBox);

        when(collisionInformation.getDistance()).thenReturn(10d);
        when(boundingBox.isHitBy(any())).thenReturn(true);
        when(shape.intersect(any())).thenReturn(Optional.of(collisionInformation));
        when(mockCollisionInformation.getDistance()).thenReturn(5d);
        when(mockBoundingBox.isHitBy(any())).thenReturn(true);
        when(mockShape.intersect(any())).thenReturn(Optional.of(mockCollisionInformation));

        Optional<CollisionInformation> hitObject = scene.hitObject(ray);

        assertNotNull(hitObject);
        assertTrue(hitObject.isPresent());
        assertEquals(5d, hitObject.get().getDistance(), 0d);

    }

    @Test
    public void testHitObjectForShadowRayAndMiss() throws Exception {

        when(boundingBox.isHitBy(any())).thenReturn(false);

        Optional<CollisionInformation> objectForShadowRay = scene.hitObjectForShadowRay(ray);

        assertNotNull(objectForShadowRay);
        assertFalse(objectForShadowRay.isPresent());
        verify(boundingBox).isHitBy(ray);
        verify(shape, never()).intersect(ray);

    }

    @Test
    public void testHitObjectForShadowRayAndHit() throws Exception {

        when(boundingBox.isHitBy(any())).thenReturn(true);
        when(shape.intersect(any())).thenReturn(Optional.of(collisionInformation));

        Optional<CollisionInformation> hitObjectForShadowRay = scene.hitObjectForShadowRay(ray);

        assertNotNull(hitObjectForShadowRay);
        assertTrue(hitObjectForShadowRay.isPresent());
        verify(boundingBox, times(1)).isHitBy(ray);
        verify(shape, times(1)).intersect(ray);
    }
}