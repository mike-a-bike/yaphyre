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

import java.util.Arrays;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static yaphyre.math.MathUtils.EPSILON;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 30.05.14
 */
public class SolverTest {

    @Test
    public void testLinearSolver() {

        double[] solutions;

        // test x - 3 = 0 ( 3 )
        solutions = Solver.Linear.solve(-3, 1);
        System.out.println(Arrays.toString(solutions));
        assertNotNull(solutions);
        assertTrue(solutions.length == 1);
        assertEquals(3, solutions[0], EPSILON);

        // test 3 = 0 (no solution)
        solutions = Solver.Linear.solve(3, 0);
        System.out.println(Arrays.toString(solutions));
        assertNotNull(solutions);
        assertTrue(solutions.length == 0);

    }

    @Test
    public void testQuadraticSolver() {

        double[] solutions;

        // test x^2 + x - 12 = 0 (-4 / 3)
        solutions = Solver.Quadratic.solve(-12, 1, 1);
        assertNotNull(solutions);
        System.out.println(Arrays.toString(solutions));
        assertNotNull(solutions);
        assertTrue(solutions.length == 2);
        assertEquals(-4d, solutions[0], EPSILON);
        assertEquals(3d, solutions[1], EPSILON);

        // test -2x^2 + 10x - 12 = 0 (2 / 3)
        solutions = Solver.Quadratic.solve(-12, 10, -2);
        System.out.println(Arrays.toString(solutions));
        assertNotNull(solutions);
        assertTrue(solutions.length == 2);
        assertEquals(2d, solutions[0], EPSILON);
        assertEquals(3d, solutions[1], EPSILON);

        // test x^2 = 0 (0)
        solutions = Solver.Quadratic.solve(0, 0, 1);
        System.out.println(Arrays.toString(solutions));
        assertNotNull(solutions);
        assertTrue(solutions.length == 1);
        assertEquals(0d, solutions[0], EPSILON);

        // solve x^2 + x + 100 = 0 (no solution)
        solutions = Solver.Quadratic.solve(100, 1, 1);
        System.out.println(Arrays.toString(solutions));
        assertNotNull(solutions);
        assertTrue(solutions.length == 0);
    }

    @Test
    public void testCubicSolver() {

        // test x^3 - 4x^2 - 17x + 60 = 0 (-4 / 3 / 5)
//        double[] solutions = Solver.Cubic.solve(60, -17, -4, 1);
//        System.out.println(Arrays.toString(solutions));
//        assertNotNull(solutions);
//        assertTrue(solutions.length == 3);
//        assertEquals(-4d, solutions[0], EPSILON);
//        assertEquals(3d, solutions[1], EPSILON);
//        assertEquals(5d, solutions[2], EPSILON);

        // test 2x^3 - 4x^2 - 22x + 24 = 0 (-3, 1, 4)
        double[] solutions = Solver.Cubic.solve(24, -22, -4, 2);
        System.out.println(Arrays.toString(solutions));
        assertNotNull(solutions);
        assertTrue(solutions.length == 3);
        assertEquals(-3d, solutions[0], EPSILON);
        assertEquals(1d, solutions[1], EPSILON);
        assertEquals(4d, solutions[2], EPSILON);


        // test 3x^3 - 10x^2 + 14x + 27 = 0 (-1)

        // test x^3 + 6x^2 + 12x + 8 = 0 (-2, -2, -2)
    }
}
