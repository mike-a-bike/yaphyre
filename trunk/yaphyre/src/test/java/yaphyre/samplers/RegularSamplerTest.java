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

package yaphyre.samplers;

import java.util.Iterator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 18.02.14
 */
public class RegularSamplerTest {
    
    @Test
    public void testGetSamples() throws Exception {
        RegularSampler sampler = new RegularSampler(2);
        validateSamples(sampler.getSamples().iterator(), 0.25d, 0.75d);

        sampler = new RegularSampler(4);
        validateSamples(sampler.getSamples().iterator(), 0.125d, 0.375d, 0.625d, 0.875d);
    }

    @Test
    public void testGetUnitSquareSamples() throws Exception {
        fail("Not implemented yet");
    }

    private <T> void validateSamples(Iterator<T> samples, T... referenceValues) {
        for (T referenceValue : referenceValues) {
            assertEquals(referenceValue, samples.next());
        }
        assertFalse(samples.hasNext());
    }

    @Test
    public void testGetSampleCount() throws Exception {
        RegularSampler sampler = new RegularSampler(4);
        assertEquals(4, sampler.getSampleCount());
        
        sampler = new RegularSampler(1);
        assertEquals(1, sampler.getSampleCount());
        
        sampler = new RegularSampler(1000);
        assertEquals(1000, sampler.getSampleCount());
    }
}
