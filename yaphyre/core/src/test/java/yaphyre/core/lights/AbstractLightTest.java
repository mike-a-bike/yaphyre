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

import org.junit.Test;
import yaphyre.core.math.Color;
import yaphyre.core.math.Ray;

import javax.annotation.Nonnull;

import static java.lang.StrictMath.pow;
import static org.junit.Assert.assertEquals;
import static yaphyre.core.math.MathUtils.EPSILON;

/**
 * Test class for the sole purpose of testing the distance attenuation.
 *
 * @author Michael Bieri
 * @since 27.09.2014
 */
public class AbstractLightTest {

    @Test
    public void testAttenuationForDistance() throws Exception {

        TestLight light = new TestLight(1d, Color.WHITE);

        double distance = 0d;
        do {
            validateDistance(light, distance);
            distance += .5d;
        } while (distance < 100d);

    }

    private void validateDistance(TestLight light, double distance) {
        double expected = 1d / (pow(1d + distance, 2d));
        assertEquals(expected, light.testLightAttenuation(distance), EPSILON);
    }

    private class TestLight extends AbstractLight {

        public TestLight(double power, Color color) {
            super(power, color);
        }

        @Nonnull
        @Override
        public Color calculateIntensityForShadowRay(@Nonnull Ray shadowRay) {
            return Color.BLACK;
        }

        public double testLightAttenuation(double distance) {
            return super.attenuationForDistance(distance);
        }
    }

}