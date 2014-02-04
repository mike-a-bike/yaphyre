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

package yaphyre.core;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.inject.Injector;
import org.apache.commons.lang3.Range;
import yaphyre.math.Ray;

import java.util.Collections;
import java.util.List;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @author $LastChangedBy: $
 * @version $Revision: $
 */
public class Scene {

	private final List<Shape> shapes;

	private final List<Light> lightsources;

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

	public void addLightsource(Light lightsource) {
		lightsources.add(lightsource);
	}

	public List<Light> getLightsources() {
		return Collections.unmodifiableList(lightsources);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(getClass())
				.add("cameras", cameras.size())
				.add("shapes", shapes.size())
				.add("lightsources", lightsources.size()).toString();
	}

	public CollisionInformation getCollisionInformation(Ray ray, double maxDistance) {

		double nearestCollisionDistance = maxDistance;
		CollisionInformation result = null;

		for (Shape shape : getShapes()) {
			CollisionInformation collisionInformation = shape.intersect(ray);
			if (collisionInformation != null && collisionInformation.getDistance() < nearestCollisionDistance) {
				nearestCollisionDistance = collisionInformation.getDistance();
				result = collisionInformation;
			}
		}

		return result;
	}


	public CollisionInformation hitObject(Ray ray, Range<Double> hitRange) {
		return null;
	}

    public void injectMembers(Injector injector) {
        for (Shape shape : shapes) {
            injector.injectMembers(shape);
        }
        for (Light lightsource : lightsources) {
            injector.injectMembers(lightsource);
        }
        for (Camera camera : cameras) {
            injector.injectMembers(camera);
        }
    }
}
