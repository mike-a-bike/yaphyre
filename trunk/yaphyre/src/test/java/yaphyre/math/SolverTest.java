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

package yaphyre.math;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 30.05.14
 */
public class SolverTest {

    @Test
    public void testLinearSolver() {

        // test x - 3 = 0 ( 3 )
        final double[] solutions = Solver.Linear.solve(-3, 1);
        assertNotNull(solutions);
        assertTrue(solutions.length == 1);
        assertEquals(3, solutions[0], 0d);

    }

    @Test
    public void testQuadraticSolver() {

        // test x^2 + x - 12 = 0 (-4 / 3)
        double[] solutions = Solver.Quadratic.solve(-12, 1, 1);
        assertNotNull(solutions);
        System.out.println(Arrays.toString(solutions));
        assertTrue(solutions.length == 2);
        assertEquals(-4d, solutions[0], 0d);
        assertEquals(3d, solutions[1], 0d);

        // test -2x^2 + 10x - 12 = 0 (2 / 3)
        solutions = Solver.Quadratic.solve(-12, 10, -2);
        System.out.println(Arrays.toString(solutions));
        assertTrue(solutions.length == 2);
        assertEquals(2d, solutions[0], 0d);
        assertEquals(3d, solutions[1], 0d);

        // test x^2 = 0
        solutions = Solver.Quadratic.solve(0, 0, 1);
        System.out.println(Arrays.toString(solutions));
        assertTrue(solutions.length == 1);
        assertEquals(0d, solutions[0], 0d);

        // solve 3x^2 - 8x - 17 = 0
        solutions = Solver.Quadratic.solve(-17, -8, 3);
        System.out.println(Arrays.toString(solutions));
    }

    @Test
    public void testCubicSolver() {

        // test x^3 - 4x^2 - 17x + 60 = 0 (-4 / 3 / 5)
        double[] solutions = Solver.Cubic.solve(60, -17, -4, 1);
        System.out.println(Arrays.toString(solutions));
        assertNotNull(solutions);
        assertTrue(solutions.length == 3);
        assertEquals(-4d, solutions[0], 0d);
        assertEquals(3d, solutions[1], 0d);
        assertEquals(5d, solutions[2], 0d);
    }
}
