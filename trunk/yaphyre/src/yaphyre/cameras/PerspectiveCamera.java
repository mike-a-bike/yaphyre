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
package yaphyre.cameras;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import yaphyre.core.Camera;
import yaphyre.core.Film;
import yaphyre.core.Sampler;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector3D;
import yaphyre.samplers.JitteredSampler;

/**
 * This perspective camera is based on a simple pin hole camera model.
 * Nonetheless, it emulates effects like depth of field
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 42 $
 */
public class PerspectiveCamera extends AbstractCamera implements Camera {

	private final PerspectiveCameraSettings cameraSettings;

	private final Point3D focalPoint;

	private final double aspectRatioInv;

	private final Sampler lensSampler;

	public PerspectiveCamera(BaseCameraSettings baseSettings, PerspectiveCameraSettings perspectiveSettings, Film film) {
		super(baseSettings, film);
		this.cameraSettings = perspectiveSettings;
		this.focalPoint = new Point3D(0, 0, -this.cameraSettings.getFocalLength());
		this.aspectRatioInv = 1d / this.cameraSettings.getAspectRatio();
		if (this.cameraSettings.getLensRadius() > 0d) {
			this.lensSampler = new JitteredSampler(4);
		} else {
			this.lensSampler = null;
		}
	}

	@Override
	public Ray getCameraRay(Point2D viewPlanePoint) {
		Preconditions.checkArgument(viewPlanePoint.getU() >= 0d && viewPlanePoint.getU() <= 1d);
		Preconditions.checkArgument(viewPlanePoint.getV() >= 0d && viewPlanePoint.getV() <= 1d);

		Point3D mappedPoint = this.mapViewPlanePoint(viewPlanePoint);
		Vector3D direction = new Vector3D(this.focalPoint, mappedPoint).normalize();

		if (this.cameraSettings.getLensRadius() > 0d) {
			// UGLY, better implementation for random sampling needed...
			Point2D lensPoint = this.lensSampler.getUnitCircleSamples().iterator().next().mul(this.cameraSettings.getLensRadius());

			Point3D focusPoint = mappedPoint.add(direction.scale(this.cameraSettings.getFocalDistance()));
			mappedPoint = mappedPoint.add(lensPoint);
			direction = new Vector3D(focusPoint, mappedPoint).normalize();
		}

		Ray result = new Ray(mappedPoint, direction);

		result = super.getCamera2World().transform(result);

		return result;
	}

	private Point3D mapViewPlanePoint(Point2D viewPlanePoint) {
		double u = viewPlanePoint.getU() - 0.5d;
		double v = (viewPlanePoint.getV() - 0.5d) * this.aspectRatioInv;
		return new Point3D(u, v, 0d);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("pos", this.getPosition())
				.add("lookAt", this.getLookAt())
				.add("apsect ratio", this.cameraSettings.getAspectRatio())
				.add("focal length", this.cameraSettings.getFocalLength())
				.add("focal distance", this.cameraSettings.getFocalDistance())
				.add("lens radius", this.cameraSettings.getLensRadius())
				.add("film", this.getFilm())
				.toString();
	}

	/**
	 * Parameter class for the initialization of the {@link PerspectiveCamera}.
	 *
	 * @author Michael Bieri
	 * @author $LastChangedBy: mike0041@gmail.com $
	 * @version $Revision: 42 $
	 */
	public static class PerspectiveCameraSettings {
		private final double aspectRatio;

		private final double focalLength;

		private final double focalDistance;

		private final double lensRadius;

		public static PerspectiveCameraSettings create(double aspectRatio, double focalLength) {
			return new PerspectiveCameraSettings(aspectRatio, focalLength, Double.MAX_VALUE, 0d);
		}

		public static PerspectiveCameraSettings create(double aspectRatio, double focalLength, double focalDistance, double lensRadius) {
			return new PerspectiveCameraSettings(aspectRatio, focalLength, focalDistance, lensRadius);
		}

		private PerspectiveCameraSettings(double aspectRatio, double focalLength, double focalDistance, double lensRadius) {
			this.aspectRatio = aspectRatio;
			this.focalLength = focalLength;
			this.focalDistance = focalDistance;
			this.lensRadius = lensRadius;
		}

		@Override
		public String toString() {
			return Objects.toStringHelper(this.getClass()).add("aspectRatio", this.aspectRatio).add("focalLength", this.focalLength).add("focalDistance", this.focalDistance).add("lensRadius", this.lensRadius).toString();
		}

		public double getAspectRatio() {
			return this.aspectRatio;
		}

		public double getFocalLength() {
			return this.focalLength;
		}

		public double getFocalDistance() {
			return this.focalDistance;
		}

		public double getLensRadius() {
			return this.lensRadius;
		}
	}

}
