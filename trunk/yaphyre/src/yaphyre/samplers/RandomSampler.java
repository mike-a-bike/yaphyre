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
package yaphyre.samplers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import yaphyre.geometry.Point2D;

/**
 * A pure random sampler. Each time the sampler is called, some new samples are created. This prevents the re-use of
 * the same samplers over and over again.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 46 $
 */
public class RandomSampler extends AbstractSampler {

	private static final Random RANDOM = new Random(System.nanoTime());

	public RandomSampler(int numberOfSamples) {
		super(numberOfSamples);
	}

	@Override
	protected List<Point2D> createSamples(int numberOfSamples) {
		List<Point2D> samples = new ArrayList<Point2D>(numberOfSamples);

		while (samples.size() < numberOfSamples) {
			samples.add(new Point2D(RANDOM.nextDouble(), RANDOM.nextDouble()));
		}

		return samples;
	}

}
