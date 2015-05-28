/*
 * Copyright 2013 Michael Bieri
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

package yaphyre.core.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static yaphyre.core.math.MathUtils.EPSILON;
import static yaphyre.core.math.MathUtils.max;
import static yaphyre.core.math.MathUtils.min;

public class MathUtilsTest {

    @Test
    public void testMin() throws Exception {

        double a = 10d, b = 20d, c = 30d;

        assertEquals(a, min(a, b, c), EPSILON);
        assertEquals(a, min(a, c, b), EPSILON);
        assertEquals(a, min(b, a, c), EPSILON);
        assertEquals(a, min(b, c, a), EPSILON);
        assertEquals(a, min(c, a, b), EPSILON);
        assertEquals(a, min(c, b, a), EPSILON);

    }

    @Test
    public void testMax() throws Exception {

        double a = 10d, b = 20d, c = 30d;

        assertEquals(c, max(a, b, c), EPSILON);
        assertEquals(c, max(a, c, b), EPSILON);
        assertEquals(c, max(b, a, c), EPSILON);
        assertEquals(c, max(b, c, a), EPSILON);
        assertEquals(c, max(c, a, b), EPSILON);
        assertEquals(c, max(c, b, a), EPSILON);

    }

}
