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

package yaphyre.cameras;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yaphyre.core.CameraSample;
import yaphyre.core.Film;
import yaphyre.core.Scene;
import yaphyre.math.Color;
import yaphyre.math.Normal3D;
import yaphyre.math.Point2D;
import yaphyre.math.Point3D;
import yaphyre.math.Ray;
import yaphyre.math.Transformation;
import yaphyre.math.Vector3D;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.tan;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 05.07.13
 */
public class PerspectiveCamera extends FilmBasedCamera {

	private static final Logger LOGGER = LoggerFactory.getLogger(PerspectiveCamera.class);

	private final Point3D position;
	private final Point3D lookAt;
	private final Normal3D up;
	private final double fieldOfView;
	private final double aspectRatio;
	private final double nearDistance;
	private final double farDistance;

	private Transformation cameraToWorld;
	private Transformation worldToCamera;
	private Point3D virtualOrigin;
	public static final Range<Double> VALID_COORDINATE_RANGE = Range.between(0d, 1d);

	public PerspectiveCamera(@Nonnull Film film,
	                         @Nonnull Point3D position, @Nonnull Point3D lookAt, @Nonnull Normal3D up,
	                         @Nonnegative double fieldOfView, @Nonnegative double aspectRatio,
	                         @Nonnegative double nearDistance, @Nonnegative double farDistance) {
		super(film);

		this.position = position;
		this.lookAt = lookAt;
		this.up = up;
		this.fieldOfView = fieldOfView;
		this.aspectRatio = aspectRatio;
		this.nearDistance = nearDistance;
		this.farDistance = farDistance;

		this.setupCamera();
	}

	private void setupCamera() {
		worldToCamera = Transformation.lookAt(position, lookAt, up.asVector());
		cameraToWorld = worldToCamera.inverse();
		double virtualZ = 1d / (2d * tan(fieldOfView / 2d));
		virtualOrigin = new Point3D(0d, 0d, -virtualZ);
	}

	@Nonnull
    @Override
	protected Ray createCameraRay(@Nonnull Point2D samplePoint) {
		checkArgument(VALID_COORDINATE_RANGE.contains(samplePoint.getU()));
		checkArgument(VALID_COORDINATE_RANGE.contains(samplePoint.getV()));

		final Point3D samplePoint3D = new Point3D(samplePoint.getU() - 1d / 2d, samplePoint.getV() - 1d / 2d, 0d);
		final Vector3D direction = new Vector3D(virtualOrigin, samplePoint3D).normalize();
		final Ray samplingRay = new Ray(virtualOrigin, direction);
		final Ray transformedRay = cameraToWorld.inverse().transform(samplingRay);

		return transformedRay;
	}

	@Override
	public void renderScene(@Nonnull Scene scene) {

		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("entering renderScene: " + scene);
		}

        final int xResolution = this.getFilm().getNativeResolution().getFirst();
		final int yResolution = this.getFilm().getNativeResolution().getSecond();

		final double xStep = 1d / xResolution;
		final double yStep = 1d / yResolution;

		for (int xCoordinate = 0; xCoordinate < xResolution; xCoordinate++) {
			for (int yCoordinate = 0; yCoordinate < yResolution; yCoordinate++) {
				final Point2D samplePoint = new Point2D(xCoordinate * xStep, yCoordinate * yStep);
				final Ray cameraRay = createCameraRay(samplePoint);
				final Color sampleColor = getTracer().traceRay(cameraRay, scene);
				getFilm().addCameraSample(new CameraSample(samplePoint, sampleColor));
			}
		}

		LOGGER.trace("exiting renderScene");
	}
}
