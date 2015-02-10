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

package yaphyre.core.shapes;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import yaphyre.core.api.CollisionInformation;
import yaphyre.core.api.Shader;
import yaphyre.core.math.Point3D;
import yaphyre.core.math.Ray;
import yaphyre.core.math.Solver;
import yaphyre.core.math.Solvers;
import yaphyre.core.math.Transformation;
import yaphyre.core.math.Vector3D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimpleSphereTest {

    @Mock
    private Solver solver;

    @Mock
    private Shader shader;

    @Test
    public void testIntersectsRejectedByRangeConstraints() throws Exception {

        SimpleSphere sphere = new SimpleSphere(Transformation.IDENTITY, shader);
        sphere.setSolver(solver);

        Point3D rayOrigin = new Point3D(10, 0, 0);
        Vector3D direction = Point3D.ORIGIN.sub(rayOrigin).normalize();
        Ray testRay = new Ray(rayOrigin, direction, 10d, 20d);

        when(solver.solve(anyVararg())).thenReturn(new double[]{100d, 110d});
        assertFalse("No intersection is expected", sphere.intersect(testRay).isPresent());

        when(solver.solve(anyVararg())).thenReturn(new double[]{1d, 2d});
        assertFalse("No intersection is expected", sphere.intersect(testRay).isPresent());

        when(solver.solve(anyVararg())).thenReturn(new double[]{-1d, -2d});
        assertFalse("No intersection is expected", sphere.intersect(testRay).isPresent());

        when(solver.solve(anyVararg())).thenReturn(new double[]{1d, 15d});
        assertTrue("Intersection expected", sphere.intersect(testRay).isPresent());

        when(solver.solve(anyVararg())).thenReturn(new double[]{15d, 100d});
        assertTrue("Intersection expected", sphere.intersect(testRay).isPresent());

        when(solver.solve(anyVararg())).thenReturn(new double[]{15d});
        assertTrue("Intersection expected", sphere.intersect(testRay).isPresent());
    }

    @Test
    public void testIntersect() throws Exception {

        SimpleSphere sphere = new SimpleSphere(Transformation.IDENTITY, shader);
        sphere.setSolver(Solvers.Quadratic);

        Point3D rayOrigin = new Point3D(10, 0, 0);
        Vector3D direction = Point3D.ORIGIN.sub(rayOrigin).normalize();
        Ray testRay = new Ray(rayOrigin, direction);

        Optional<CollisionInformation> optional = sphere.intersect(testRay);
        assertTrue("Intersection expected", optional.isPresent());

        CollisionInformation collisionInformation = optional.get();
        Point3D expected = new Point3D(1, 0, 0);
        assertEquals("Expected intersection point: " + expected, expected, collisionInformation.getPoint());
        assertEquals("Collision distance of 9 (position (10) - radius (1)) expected", 9d, collisionInformation.getDistance(), 0d);

    }

}