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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import yaphyre.geometry.Point2D;

/**
 * This is the simplest sampler possible. It creates just one sample which lies in the middle of the unit square.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 46 $
 */
public class SinglePointSampler extends AbstractSampler {

	private static final List<Point2D> sampleList = Collections.unmodifiableList(Arrays.asList(new Point2D(0.5, 0.5)));

	/** Since there is only one set, there is no need to shuffle the sets prior to returning one. */
	@Override
	public Iterable<Point2D> getUnitSquareSamples() {
		return createSamples(1);
	}

	/**
	 * This implementation creates just one sample which lies in the middle of the unit square. It ignores the number of
	 * samples requested by the user.
	 */
	@Override
	protected List<Point2D> createSamples(int numberOfSamples) {
		return sampleList;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
