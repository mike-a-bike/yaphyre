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

package yaphyre.raytracer;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import yaphyre.core.Camera;
import yaphyre.core.CollisionInformation;
import yaphyre.core.Lightsource;
import yaphyre.core.Shape;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class Scene implements Serializable {

	private static final long serialVersionUID = 7351378461914059224L;

	private final List<Shape> shapes;

	private final List<Lightsource> lightsources;

	private final List<Camera> cameras;

	public Scene() {
		shapes = Lists.newArrayList();
		lightsources = Lists.newArrayList();
		cameras = Lists.newArrayList();
	}

	public void addCamera(Camera camera) {
		cameras.add(camera);
	}

	public List<Camera> getCameras() {
		return Collections.unmodifiableList(cameras);
	}

	public void addShape(Shape shape) {
		shapes.add(shape);
	}

	public List<Shape> getShapes() {
		return Collections.unmodifiableList(shapes);
	}

	public void addLightsource(Lightsource lightsource) {
		lightsources.add(lightsource);
	}

	public List<Lightsource> getLightsources() {
		return Collections.unmodifiableList(lightsources);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(getClass()).add("cameras", cameras.size()).add("shapes", shapes.size()).add("lightsources", lightsources
				.size()).toString();
	}

	public CollisionInformation getCollidingShape(Ray ray, double maxDistance, boolean onlyShadowShapes) {

		double nearestCollisionDistance = maxDistance;
		Shape nearestCollisionShape = null;

		for (Shape shape : getShapes()) {
			if (onlyShadowShapes && !shape.throwsShadow()) {
				continue;
			}
			double distance = shape.getIntersectDistance(ray);
			if (distance < nearestCollisionDistance) {
				nearestCollisionDistance = distance;
				nearestCollisionShape = shape;
			}
		}

		CollisionInformation result = null;
		if (nearestCollisionDistance < maxDistance) {
			final Point3D collisionPoint = ray.getPoint(nearestCollisionDistance);
			final Normal3D collisionNormal = nearestCollisionShape.getNormal(collisionPoint).faceForward(ray);
			final Point2D uvCoordinates = nearestCollisionShape.getMappedSurfacePoint(collisionPoint);

			result = new CollisionInformation(ray, nearestCollisionShape, nearestCollisionDistance, collisionPoint, collisionNormal, uvCoordinates);
		}

		return result;
	}

}
