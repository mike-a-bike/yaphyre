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

import yaphyre.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This sampler creates a regular pattern with nxn cells with n = sqrt(number of
 * samples). Within these cells the sampler points are generated with a random
 * generator. This sampler produces better results than the pure random sampler,
 * although not yet very well distributed samples.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 46 $
 */
public class JitteredSampler extends AbstractSampler {

	private static final Random RANDOM = new Random(System.nanoTime());

	public JitteredSampler(int numberOfSamples) {
		super(numberOfSamples);
	}

	@Override
	protected List<Point2D> createSamples(int numberOfSamples) {
		int samplesPerSide = (int) Math.sqrt(numberOfSamples);
		List<Point2D> result = new ArrayList<Point2D>(samplesPerSide * samplesPerSide);
		double intervall = 1d / samplesPerSide;
		for (int row = 0; row < samplesPerSide; row++) {
			for (int col = 0; col < samplesPerSide; col++) {
				result.add(new Point2D((row + RANDOM.nextDouble()) * intervall, (col + RANDOM.nextDouble()) * intervall));
			}
		}
		return result;
	}

}
