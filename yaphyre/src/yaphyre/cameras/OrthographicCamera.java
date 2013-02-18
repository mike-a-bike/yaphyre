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

import java.text.MessageFormat;

import yaphyre.core.Film;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector3D;

import org.jetbrains.annotations.NotNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * A very simple camera showing an orthographic view of the scene to render.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 42 $
 */
public class OrthographicCamera extends AbstractCamera {

	private final OrthographicCameraSettings cameraSettings;

	private final double viewPlaneWidthStart;

	private final double viewPlaneHeightStart;

	public OrthographicCamera(BaseCameraSettings baseSettings, OrthographicCameraSettings orthographicSettings,
			Film film) {
		super(baseSettings, film);
		cameraSettings = orthographicSettings;
		viewPlaneWidthStart = -(cameraSettings.getViewPlaneWidth() / 2d);
		viewPlaneHeightStart = -(cameraSettings.getViewPlaneHeight() / 2d);
	}

	@Override
	public Iterable<Ray> getCameraRay(@NotNull Point2D viewPlanePoint) {
		Preconditions.checkArgument(viewPlanePoint.getU() >= 0d && viewPlanePoint.getU() <= 1d);
		Preconditions.checkArgument(viewPlanePoint.getV() >= 0d && viewPlanePoint.getV() <= 1d);

		Point2D mappedPoint = mapViewPlanePoint(viewPlanePoint);
		Ray result = new Ray(new Point3D(mappedPoint.getU(), mappedPoint.getV(), 0), Vector3D.Z);
		result = super.getCamera2World().transform(result);

		return Lists.newArrayList(result);
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0} [pos:{1}, lookat:{2}, width:{3}, height:{4}, film:{5}]",
				getClass().getSimpleName(), super.getPosition(), super.getLookAt(), String.valueOf(
				cameraSettings.getViewPlaneWidth()), String.valueOf(cameraSettings.getViewPlaneHeight()),
				super.getFilm());
	}

	/**
	 * Map the view plane point onto a concrete coordinate on this cameras width and height rectangle.
	 *
	 * @param viewPlanePoint
	 * 		The point to map (u, v &isin; [0, 1])
	 *
	 * @return A point which lies on the view plane rectangle (u &isin; [-width/2, +width/2] and v &isin; [-height/2,
	 *         height/2])
	 */
	private Point2D mapViewPlanePoint(Point2D viewPlanePoint) {
		double mappedU = viewPlaneWidthStart + cameraSettings.getViewPlaneWidth() * viewPlanePoint.getU();
		double mappedV = viewPlaneHeightStart + cameraSettings.getViewPlaneHeight() * viewPlanePoint.getV();
		return new Point2D(mappedU, mappedV);
	}

	/**
	 * Parameter class for the initialization of the {@link OrthographicCamera}.
	 *
	 * @author Michael Bieri
	 * @author $LastChangedBy: mike0041@gmail.com $
	 * @version $Revision: 42 $
	 */
	public static class OrthographicCameraSettings {

		private final double viewPlaneWidth;

		private final double viewPlaneHeight;

		public static OrthographicCameraSettings create(double viewPlaneWidth, double viewPlaneHeight) {
			return new OrthographicCameraSettings(viewPlaneWidth, viewPlaneHeight);
		}

		private OrthographicCameraSettings(double viewPlaneWidth, double viewPlaneHeight) {
			this.viewPlaneWidth = viewPlaneWidth;
			this.viewPlaneHeight = viewPlaneHeight;
		}

		public double getViewPlaneWidth() {
			return viewPlaneWidth;
		}

		public double getViewPlaneHeight() {
			return viewPlaneHeight;
		}
	}
}
