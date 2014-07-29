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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.PrivateModule;

import org.junit.Before;
import org.junit.Test;

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
import static org.mockito.Mockito.mock;

public class SimpleSphereTest {

    private Injector mockedInjector;
    private Injector solverInjector;

    private TestSolver quadraticSolver;

    @Before
    public void setUp() throws Exception {
        quadraticSolver = new TestSolver();
        mockedInjector = Guice.createInjector(new PrivateModule() {
            @Override
            protected void configure() {
                bind(Solver.class).annotatedWith(Solver.Quadratic.class).toInstance(quadraticSolver);
                expose(Solver.class).annotatedWith(Solver.Quadratic.class);
            }
        });
        solverInjector = Guice.createInjector(new PrivateModule() {
            @Override
            protected void configure() {
                bind(Solver.class).annotatedWith(Solver.Quadratic.class).toInstance(Solvers.Quadratic);
                expose(Solver.class).annotatedWith(Solver.Quadratic.class);
            }
        });
    }

    @Test
    public void testIntersectsRejectedByRangeConstraints() throws Exception {

        SimpleSphere sphere = new SimpleSphere(Transformation.IDENTITY, mock(Shader.class));
        mockedInjector.injectMembers(sphere);

        Point3D rayOrigin = new Point3D(10, 0, 0);
        Vector3D direction = Point3D.ORIGIN.sub(rayOrigin).normalize();
        Ray testRay = new Ray(rayOrigin, direction, 10d, 20d);

        quadraticSolver.setExpectedResults(100d, 110d);
        assertFalse("No intersection is expected", sphere.intersect(testRay).isPresent());

        quadraticSolver.setExpectedResults(1d, 2d);
        assertFalse("No intersection is expected", sphere.intersect(testRay).isPresent());

        quadraticSolver.setExpectedResults(1d, 15d);
        assertTrue("Intersection expected", sphere.intersect(testRay).isPresent());

        quadraticSolver.setExpectedResults(15d, 100d);
        assertTrue("Intersection expected", sphere.intersect(testRay).isPresent());
    }

    @Test
    public void testIntersect() throws Exception {

        SimpleSphere sphere = new SimpleSphere(Transformation.IDENTITY, mock(Shader.class));
        solverInjector.injectMembers(sphere);

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

    private static class TestSolver implements Solver {

        private double[] expectedResults;

        @Override
        public double[] solve(double... c) throws IllegalArgumentException {
            return expectedResults;
        }

        public void setExpectedResults(double... expectedResults) {
            this.expectedResults = expectedResults;
        }
    }
}