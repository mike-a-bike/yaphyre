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
import yaphyre.geometry.Point2D;

public class CameraSample {

	private Point2D rasterPoint;

	private Point2D lensCoordinates;

	@Override
	public String toString() {
		return Objects.toStringHelper(getClass()).add("rasterPoint", rasterPoint).add("lensCoordinates", lensCoordinates).toString();
	}

	public Point2D getRasterPoint() {
		return this.rasterPoint;
	}

	public void setRasterPoint(Point2D rasterPoint) {
		this.rasterPoint = rasterPoint;
	}

	public Point2D getLensCoordinates() {
		return this.lensCoordinates;
	}

	public void setLensCoordinates(Point2D lensCoordinates) {
		this.lensCoordinates = lensCoordinates;
	}


}
