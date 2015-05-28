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
import java.util.stream.Stream;
import javax.inject.Inject;

import com.google.common.base.MoreObjects;
import com.google.inject.Injector;

import yaphyre.core.math.Ray;

import static java.util.Comparator.comparingDouble;

/**
 * Scene holding all the relevant objects. This is also responsible for intersection a ray with the objects
 * contained within the scene.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: $
 * @version $Revision: $
 */
public class Scene {

    private final List<Shape> shapes;

    private final List<Light> lights;

    private final List<Camera> cameras;

    private final Injector injector;

    @Inject
    public Scene(Injector injector) {
        this.injector = injector;
        shapes = new ArrayList<>();
        lights = new ArrayList<>();
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

    public void addLight(Light light) {
        injector.injectMembers(light);
        lights.add(light);
    }

    public List<Light> getLights() {
        return Collections.unmodifiableList(lights);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("cameras", cameras.size())
                .add("shapes", shapes.size())
                .add("lights", lights.size()).toString();
    }

    public Optional<CollisionInformation> hitObject(Ray ray) {
        return prepareCollisionInformationStream(ray).min(comparingDouble(CollisionInformation::getDistance));
    }

    public Optional<CollisionInformation> hitObjectForShadowRay(Ray ray) {
        return prepareCollisionInformationStream(ray).findFirst();
    }

    private Stream<CollisionInformation> prepareCollisionInformationStream(Ray ray) {
        return shapes.stream()
                .filter(shape -> shape.getBoundingBox().isHitBy(ray))
                .map(shape -> shape.intersect(ray))
                .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty));
    }

}
