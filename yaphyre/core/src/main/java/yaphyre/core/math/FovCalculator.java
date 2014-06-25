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

package yaphyre.core.math;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.atan;
import static yaphyre.core.math.MathUtils.EPSILON;

/**
 * Helper enumeration for calculating field of view angles for the most used sensor sizes.
 */
public enum FovCalculator {
	FullFrame35mm(36d, 24d), APS_H(28.7d, 19d), APS_C(23.6d, 15.7d);

	private final double width;

	private final double height;

	private FovCalculator(final double width, final double height) {
		checkArgument(width >= EPSILON);
		checkArgument(height >= EPSILON);
		this.width = width;
		this.height = height;
	}

    /**
     * Calculate the vertical field of view for the given sensor size
     *
     * @param focalLength The focal length used (in mm). Please notice, the value must be greater than
     *                    {@value yaphyre.math.MathUtils#EPSILON}.
     * @return The field of view in rad.
     */
    public double calculateVerticalFov(final double focalLength) {
		checkArgument(focalLength >= EPSILON);
		return calculateFov(focalLength, height);
	}

    /**
     * Calculate the horizontal field of view for the given sensor size
     *
     * @param focalLength The focal length used (in mm). Please notice, the value must be greater than
     *                    {@value yaphyre.math.MathUtils#EPSILON}.
     * @return The field of view in rad.
     */
	public double calculateHorizontalFov(final double focalLength) {
		checkArgument(focalLength >= EPSILON);
		return calculateFov(focalLength, width);
	}

	public double getAspectRatio() {
		return width / height;
	}

	private double calculateFov(final double focalLength, final double filmSize) {
		return 2d * atan(filmSize / (2d * focalLength));
	}

}
