/*
 * Copyright 2012 Michael Bieri
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package yaphyre.sampler;

import yaphyre.geometry.Point2D;
import yaphyre.samplers.AbstractSampler;
import yaphyre.samplers.RandomSampler;

import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA. User: michael Date: 28.10.12 Time: 20:54 To change this template use File | Settings |
 * File Templates.
 */
public class RandomSamplerTest extends SamplerTest {

	private AbstractSampler sampler;

	@Before
	public void setupSampler() {
		sampler = new RandomSampler(64);
	}

	@Test
	public void testGetUnitSquareSamples() {
		for(int run = 0; run < 1000; run++) {
			for(Point2D point : sampler.getUnitSquareSamples()) {
				super.addMark(point);
			}
		}
		super.setImageName("RandomSamper_UnitSquareSample.png");
	}

	@Test
	public void testGetUnitCircleSamples() {
		for(int run = 0; run < 1000; run++) {
			for(Point2D sampledPoint : sampler.getUnitCircleSamples()) {
				Point2D point = sampledPoint.mul(.5d).add(.5d, .5d);
				super.addMark(point);
			}
		}
		super.setImageName("RandomSamper_UnitCircleSample.png");
	}

}
