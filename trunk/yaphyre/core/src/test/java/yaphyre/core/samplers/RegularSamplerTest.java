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

package yaphyre.core.samplers;

import java.util.Iterator;
import javax.annotation.Nonnull;
import org.junit.Test;
import yaphyre.core.math.Point2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test for the RegularSampler. This sampler created regularly spaced samples in the range [0, 1).
 *
 * @author Michael Bieri
 * @since 18.02.14
 */
public class RegularSamplerTest {
    
    @Test
    public void testGetUnitSquareSamples() throws Exception {
        RegularSampler sampler = new RegularSampler(2);
        validateSamples(sampler.getUnitSquareSamples().iterator(),
                new Point2D(0.25d, 0.25d), new Point2D(0.75d, 0.75d));

        sampler = new RegularSampler(4);
        validateSamples(sampler.getUnitSquareSamples().iterator(),
                new Point2D(0.125, 0.125d), new Point2D(0.375d, 0.375d),
                new Point2D(0.625d, 0.625d), new Point2D(0.875d, 0.875d));
    }

    @SafeVarargs
    private final <T> void validateSamples(@Nonnull Iterator<T> samples, T... referenceValues) {
        for (T referenceValue : referenceValues) {
            assertEquals(referenceValue, samples.next());
        }
        assertFalse(samples.hasNext());
    }

}
