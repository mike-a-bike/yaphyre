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

package yaphyre.core.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import com.google.common.base.Objects;
import com.google.inject.Injector;
import yaphyre.core.math.Ray;

import static java.util.Comparator.comparingDouble;

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

    private final Injector injector;

    @Inject
	public Scene(Injector injector) {
        this.injector = injector;
        shapes = new ArrayList<>();
		lightsources = new ArrayList<>();
		cameras = new ArrayList<>();
	}

	public void addCamera(Camera camera) {
        injector.injectMembers(camera);
		cameras.add(camera);
	}

	public List<Camera> getCameras() {
		return Collections.unmodifiableList(cameras);
	}

	public void addShape(Shape shape) {
        injector.injectMembers(shape);
		shapes.add(shape);
	}

	public List<Shape> getShapes() {
		return Collections.unmodifiableList(shapes);
	}

	public void addLightsource(Light lightsource) {
        injector.injectMembers(lightsource);
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

	public Optional<CollisionInformation> hitObject(Ray ray) {
        return getShapes().stream()
            .filter(shape -> shape.getBoundingBox().isHitBy(ray))
            .map(shape -> shape.intersect(ray))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .min(comparingDouble(CollisionInformation::getDistance));
    }

}
