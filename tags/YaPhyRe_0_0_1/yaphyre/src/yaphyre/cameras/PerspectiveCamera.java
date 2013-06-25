/*
 * Copyright 2013 Michael Bieri
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

import java.util.Iterator;
import java.util.NoSuchElementException;

import yaphyre.core.Film;
import yaphyre.core.Sampler;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Transformation;
import yaphyre.geometry.Vector3D;
import yaphyre.samplers.JitteredSampler;
import yaphyre.util.RenderStatistics;
import yaphyre.util.SingleInstanceIterator;

import org.jetbrains.annotations.NotNull;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * This perspective camera is based on a simple pin hole camera model. Nonetheless, it emulates effects like depth of
 * field
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 42 $
 */
public class PerspectiveCamera extends AbstractCamera {

	private final PerspectiveCameraSettings cameraSettings;

	private final Point3D focalPoint;

	private final double aspectRatioInv;

	private final Sampler lensSampler;

	public PerspectiveCamera(BaseCameraSettings baseSettings, PerspectiveCameraSettings perspectiveSettings,
			Film film) {
		super(baseSettings, film);
		cameraSettings = perspectiveSettings;
		focalPoint = new Point3D(0, 0, -cameraSettings.getFocalLength());
		aspectRatioInv = 1d / cameraSettings.getAspectRatio();
		if (cameraSettings.getApertureSize() > 0d) {
			lensSampler = new JitteredSampler(4);
		} else {
			lensSampler = null;
		}
	}

	@NotNull
	@Override
	public Iterable</*@NotNull*/ Ray> createCameraRays(@NotNull Point2D viewPlanePoint) {
		Preconditions.checkArgument(viewPlanePoint.getU() >= 0d && viewPlanePoint.getU() <= 1d);
		Preconditions.checkArgument(viewPlanePoint.getV() >= 0d && viewPlanePoint.getV() <= 1d);

		Point3D origin = mapViewPlanePoint(viewPlanePoint, aspectRatioInv);
		Vector3D direction = new Vector3D(focalPoint, origin).normalize();

		final Iterator<Ray> resultIterator;

		if (cameraSettings.getApertureSize() > 0d) {
			resultIterator = new MultiSampleCameraRayIterator(viewPlanePoint, cameraSettings.getApertureSize(),
					cameraSettings.getFocalDistance(), lensSampler, origin, direction, getCamera2World());
		} else {
			Ray singleRay = new Ray(origin, direction);
			singleRay = getCamera2World().transform(singleRay);
			resultIterator = new SingleInstanceIterator<Ray>(singleRay);
		}

		return new Iterable<Ray>() {
			@Override
			public Iterator<Ray> iterator() {
				return resultIterator;
			}
		};
	}

	private class MultiSampleCameraRayIterator implements Iterator<Ray> {

		private final Iterator<Point2D> unitCircleSamples;
		private final double apertureSize;
		private final Point2D viewPlanePoint;
		private final Point3D focalPoint;
		private final Transformation camera2World;

		public MultiSampleCameraRayIterator(final Point2D viewPlanePoint, final double apertureSize, final double focalDistance,
				final Sampler lensSampler, final Point3D origin, final Vector3D direction, final Transformation camera2World) {
			this.viewPlanePoint = viewPlanePoint;
			this.apertureSize = apertureSize;
			this.camera2World = camera2World;
			focalPoint = origin.add(direction.normalize().scale(focalDistance));
			unitCircleSamples = lensSampler.getUnitCircleSamples().iterator();
		}

		@Override
		public boolean hasNext() {
			return unitCircleSamples.hasNext();
		}

		@Override
		public Ray next() {
			if (!unitCircleSamples.hasNext()) {
				throw new NoSuchElementException("no more elements available in this iterator");
			}

			RenderStatistics.incEyeRays();

			Point2D lensSamplePoint = unitCircleSamples.next().mul(apertureSize);
			Point2D newViewPlanePoint = viewPlanePoint.add(lensSamplePoint);

			Point3D newOrigin = PerspectiveCamera.mapViewPlanePoint(newViewPlanePoint, aspectRatioInv);
			Vector3D newDirection = focalPoint.sub(newOrigin).normalize();

			return camera2World.transform(new Ray(newOrigin, newDirection));
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("read-only iterator");
		}
	}

	@NotNull
	private static Point3D mapViewPlanePoint(@NotNull Point2D viewPlanePoint, final double aspectRatioInv1) {
		double u = viewPlanePoint.getU() - 0.5d;
		double v = (viewPlanePoint.getV() - 0.5d) * aspectRatioInv1;
		return new Point3D(u, v, 0d);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("pos", getPosition())
				.add("lookAt", getLookAt())
				.add("apsect ratio", cameraSettings.getAspectRatio())
				.add("focal length", cameraSettings.getFocalLength())
				.add("focal distance", cameraSettings.getFocalDistance())
				.add("lens radius", cameraSettings.getApertureSize())
				.add("film", getFilm())
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

		private final double apertureSize;

		public static PerspectiveCameraSettings create(double aspectRatio, double focalLength) {
			return new PerspectiveCameraSettings(aspectRatio, focalLength, Double.MAX_VALUE, 0d);
		}

		public static PerspectiveCameraSettings create(double aspectRatio, double focalLength, double focalDistance,
				double apertureSize) {
			return new PerspectiveCameraSettings(aspectRatio, focalLength, focalDistance, apertureSize);
		}

		private PerspectiveCameraSettings(double aspectRatio, double focalLength, double focalDistance,
				double apertureSize) {
			this.aspectRatio = aspectRatio;
			this.focalLength = focalLength;
			this.focalDistance = focalDistance;
			this.apertureSize = apertureSize;
		}

		@Override
		public String toString() {
			return Objects.toStringHelper(getClass())
					.add("aspectRatio", aspectRatio)
					.add("focalLength", focalLength)
					.add("focalDistance", focalDistance)
					.add("apertureSize", apertureSize)
					.toString();
		}

		public double getAspectRatio() {
			return aspectRatio;
		}

		public double getFocalLength() {
			return focalLength;
		}

		public double getFocalDistance() {
			return focalDistance;
		}

		public double getApertureSize() {
			return apertureSize;
		}
	}

}
