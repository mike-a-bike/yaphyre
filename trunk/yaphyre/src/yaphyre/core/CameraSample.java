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

import yaphyre.geometry.Point2D;

import com.google.common.base.Objects;

public class CameraSample {

	private final Point2D rasterPoint;

	private final Point2D lensCoordinates;

	public CameraSample(Point2D rasterPoint) {
		this(rasterPoint, Point2D.ZERO);
	}

	public CameraSample(final Point2D rasterPoint, final Point2D lensCoordinates) {
		this.rasterPoint = rasterPoint;
		this.lensCoordinates = lensCoordinates;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final CameraSample that = (CameraSample) o;

		if (lensCoordinates != null ? !lensCoordinates.equals(that.lensCoordinates) : that.lensCoordinates != null) {
			return false;
		}
		if (rasterPoint != null ? !rasterPoint.equals(that.rasterPoint) : that.rasterPoint != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = rasterPoint != null ? rasterPoint.hashCode() : 0;
		result = 31 * result + (lensCoordinates != null ? lensCoordinates.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(getClass()).add("rasterPoint", rasterPoint).add("lensCoordinates",
				lensCoordinates).toString();
	}

	public Point2D getRasterPoint() {
		return this.rasterPoint;
	}

	public Point2D getLensCoordinates() {
		return this.lensCoordinates;
	}

}
