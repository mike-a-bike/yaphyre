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

package yaphyre.core;

import com.google.common.base.Objects;

/**
 * A simple abstraction holding four coordinates which describe a window on the
 * raster.
 *
 * @author Michael Bieri
 */
public class RenderWindow {

	private final int xMin, xMax;
	private final int yMin, yMax;

	public RenderWindow(int xMin, int xMax, int yMin, int yMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(getClass())
				.add("xMin", xMin)
				.add("yMin", yMin)
				.add("xMax", xMax)
				.add("yMax", yMax).toString();
	}

	public int getXMin() {
		return xMin;
	}

	public int getXMax() {
		return xMax;
	}

	public int getYMin() {
		return yMin;
	}

	public int getYMax() {
		return yMax;
	}

}
