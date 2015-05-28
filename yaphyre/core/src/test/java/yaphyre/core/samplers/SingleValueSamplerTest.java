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

import java.util.stream.Stream;
import javax.annotation.Nonnull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import yaphyre.core.math.Point2D;
import yaphyre.core.math.Point3D;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the SingleValueSampler.
 *
 * @author Michael Bieri
 * @since 04.02.14
 */
public class SingleValueSamplerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private SingleValueSampler singleValueSampler;

    @Before
    public void setUp() {
        singleValueSampler = new SingleValueSampler();
    }

    /**
     * Checks the number of samples (one) and its value on a unit square (0.5, 0.5).
     */
    @Test
    public void testGetUnitSquareSamples() {
        validateSingleSample(new Point2D(0.5d, 0.5d), singleValueSampler.getUnitSquareSamples());
    }

    @Test
    public void testGetUnitCircleSamples() {
        validateSingleSample(Point2D.ZERO, singleValueSampler.getUnitCircleSamples());
    }

    @Test
    public void testGetUnitHemisphereSamples() {
        validateSingleSample(new Point3D(0, 1, 0), singleValueSampler.getUnitHemisphereSamples(100));
    }

    /**
     * This should throw a RuntimeException, since there is no single point to sample on a unit sphere.
     */
    @Test
    public void testGetUnitSphereSamples() {
        thrown.expect(RuntimeException.class);
        singleValueSampler.getUnitSphereSamples();
    }

    /**
     * Validate a collection of samples, represented by the Iterable instance, against the given reference value. The
     * method check also, if the total number of samples is exactly one.
     *
     * @param reference The value reference to compare against using equals(...).
     * @param samples   The collection of samples.
     * @param <T>       The type of expected samples.
     */
    private static <T> void validateSingleSample(@Nonnull T reference, @Nonnull Stream<T> samples) {
        long sampleCount = 0;
        sampleCount = samples.peek(sample -> assertEquals(reference, sample)).count();
        assertEquals(1, sampleCount);
    }

}
