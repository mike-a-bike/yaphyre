/*
 * Copyright 2013 Michael Bieri
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

package yaphyre.core;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import yaphyre.math.Color;
import yaphyre.math.Point2D;

import java.io.Serializable;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 27.07.13
 */
public class CameraSample implements Serializable {
	private final Point2D samplePoint;
	private final Point2D lensCoordinates;
	private final Color sampleColor;

	public CameraSample(Point2D samplePoint, Color color) {
		this(samplePoint, Point2D.ZERO, color);
	}

	public CameraSample(Point2D samplePoint, Point2D lensCoordinates, Color sampleColor) {
		this.samplePoint = samplePoint;
		this.lensCoordinates = lensCoordinates;
		this.sampleColor = sampleColor;
	}

	public Point2D getSamplePoint() {
		return samplePoint;
	}

	public Point2D getLensCoordinates() {
		return lensCoordinates;
	}

	public Color getSampleColor() {
		return sampleColor;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.samplePoint)
				.append(this.lensCoordinates)
				.append(this.sampleColor)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final CameraSample other = (CameraSample) obj;
		return new EqualsBuilder()
				.append(this.samplePoint, other.samplePoint)
				.append(this.lensCoordinates, other.lensCoordinates)
				.append(this.sampleColor, other.sampleColor)
				.isEquals();
	}

}
