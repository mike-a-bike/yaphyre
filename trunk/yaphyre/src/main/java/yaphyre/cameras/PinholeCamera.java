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

package yaphyre.cameras;

import yaphyre.core.Film;
import yaphyre.core.Sampler;
import yaphyre.core.Scene;
import yaphyre.math.Normal3D;
import yaphyre.math.Point3D;
import yaphyre.math.Transformation;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 05.07.13
 */
public class PinholeCamera extends FilmBasedCamera {

	private final Point3D position;
	private final Point3D lookAt;
	private final Normal3D up;
	private final double fieldOfView;
	private final double aspectRatio;
	private final double nearDistance;
	private final double farDistance;
	private final Sampler sampler;

	private Transformation cameraToWorld;
	private Transformation worldToCamera;

	public PinholeCamera(Film film,
	                     Point3D position, Point3D lookAt, Normal3D up,
	                     double fieldOfView, double aspectRatio,
	                     double nearDistance, double farDistance, Sampler sampler) {
		super(film);

		this.position = position;
		this.lookAt = lookAt;
		this.up = up;
		this.fieldOfView = fieldOfView;
		this.aspectRatio = aspectRatio;
		this.nearDistance = nearDistance;
		this.farDistance = farDistance;
		this.sampler = sampler;

		this.setCamera();
	}

	private void setCamera() {
		worldToCamera = Transformation.lookAt(position, lookAt, up.asVector());
		cameraToWorld = worldToCamera.inverse();
	}

	@Override
	public void renderScene(Scene scene) {
	}

}
