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
import yaphyre.geometry.Ray;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class Scene implements Serializable {

	private static final long serialVersionUID = 7351378461914059224L;

	private final List<Shape> shapes;

	private final List<Lightsource> lightsources;

	private final List<Camera> cameras;

	public Scene() {
		this.shapes = Lists.newArrayList();
		this.lightsources = Lists.newArrayList();
		this.cameras = Lists.newArrayList();
	}

	public void addCamera(Camera camera) {
		this.cameras.add(camera);
	}

	public List<Camera> getCameras() {
		return Collections.unmodifiableList(this.cameras);
	}

	public void addShape(Shape shape) {
		this.shapes.add(shape);
	}

	public List<Shape> getShapes() {
		return Collections.unmodifiableList(this.shapes);
	}

	public void addLightsource(Lightsource lightsource) {
		this.lightsources.add(lightsource);
	}

	public List<Lightsource> getLightsources() {
		return Collections.unmodifiableList(this.lightsources);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this.getClass()).add("cameras", this.cameras.size()).add("shapes",
				this.shapes.size()).add("lightsources", this.lightsources.size()).toString();
	}

	public CollisionInformation getCollidingShape(Ray ray, double maxDistance, boolean onlyShadowShapes) {

		CollisionInformation result = null;

		double nearestCollisionDistance = maxDistance;
		Shape nearestCollisionShape = null;

		for (Shape shape : this.getShapes()) {
			if (onlyShadowShapes && shape.throwsShadow() == false) {
				continue;
			}
			double distance = shape.getIntersectDistance(ray);
			if (distance < nearestCollisionDistance) {
				nearestCollisionDistance = distance;
				nearestCollisionShape = shape;
			}
		}

		if (nearestCollisionDistance < maxDistance) {
			result = new CollisionInformation(ray, nearestCollisionShape, nearestCollisionDistance, ray.getPoint(nearestCollisionDistance));
		}

		return result;
	}

}
