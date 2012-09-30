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

package yaphyre.geometry;

import static java.lang.Math.atan;

public enum FovCalculator {
	Format35mm {
		final static double FILM_WIDTH = 36d;
		final static double FILM_HEIGHT = 24d;
		@Override
		public double calculateVerticalFov(final double focalLength) {
			return super.calculateFov(focalLength, FILM_HEIGHT);
		}
		@Override
		public double calculateHorizontalFov(final double focalLength) {
			return super.calculateFov(focalLength, FILM_WIDTH);
		}
	};

	public abstract double calculateVerticalFov(final double focalLength);

	public abstract double calculateHorizontalFov(final double focalLength);

	protected double calculateFov(final double focalLength, final double filmSize) {
		return 2d * atan(filmSize / 2d * focalLength);
	}

}
