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

import com.google.common.base.Preconditions;
import yaphyre.core.Camera;
import yaphyre.core.Film;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Transformation;
import yaphyre.geometry.Vector3D;

/**
 * A common super class for all implemented {@link Camera}. This handles some common stuff like transformations and the
 * film instances.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 42 $
 */
public abstract class AbstractCamera implements Camera {

	private final Transformation world2Camera;

	private final Transformation camera2World;

	private final BaseCameraSettings cameraSettings;

	private Film film;

	public AbstractCamera(BaseCameraSettings baseSettings, Film film) {
		cameraSettings = baseSettings;
		world2Camera = Transformation.lookAt(baseSettings.getPosition(), baseSettings.getLookAt(),
				baseSettings.getUp());
		camera2World = world2Camera.inverse();
		this.film = film;
	}

	@Override
	public abstract Iterable<Ray> createCameraRays(Point2D viewPlanePoint);

	@Override
	public Film getFilm() {
		return film;
	}

	@Override
	public void setFilm(Film film) {
		this.film = film;
	}

	protected double getNearClip() {
		return cameraSettings.getNearClip();
	}

	protected double getFarClip() {
		return cameraSettings.getFarClip();
	}

	protected Point3D getPosition() {
		return cameraSettings.getPosition();
	}

	protected Point3D getLookAt() {
		return cameraSettings.getLookAt();
	}

	protected Vector3D getUp() {
		return cameraSettings.getUp();
	}

	protected Transformation getCamera2World() {
		return camera2World;
	}

	protected Transformation getWorld2Camera() {
		return world2Camera;
	}

	/**
	 * Base settings common for each camera like, near and far clipping pane and a film instance.
	 *
	 * @author Michael Bieri
	 * @author $LastChangedBy: mike0041@gmail.com $
	 * @version $Revision: 42 $
	 */
	public static class BaseCameraSettings {
		private final double nearClip;

		private final double farClip;

		private final Point3D position;

		private final Point3D lookAt;

		private final Vector3D up;

		public static BaseCameraSettings create(Point3D position, Point3D lookAt) {
			return create(0d, Double.MAX_VALUE, position, lookAt, Vector3D.Y);
		}

		public static BaseCameraSettings create(Point3D position, Point3D lookAt, Vector3D up) {
			return create(0d, Double.MAX_VALUE, position, lookAt, up);
		}

		public static BaseCameraSettings create(double nearClip, double farClip, Point3D position, Point3D lookAt, Vector3D up) {
			return new BaseCameraSettings(nearClip, farClip, position, lookAt, up);
		}

		private BaseCameraSettings(double nearClip, double farClip, Point3D position, Point3D lookAt, Vector3D up) {
			Preconditions.checkArgument(!position.equals(lookAt), "the position and look at point must not be equal");
			this.nearClip = nearClip;
			this.farClip = farClip;
			this.position = position;
			this.lookAt = lookAt;
			this.up = up;
		}

		public double getNearClip() {
			return nearClip;
		}

		public double getFarClip() {
			return farClip;
		}

		public Point3D getPosition() {
			return position;
		}

		public Point3D getLookAt() {
			return lookAt;
		}

		public Vector3D getUp() {
			return up;
		}

	}

}