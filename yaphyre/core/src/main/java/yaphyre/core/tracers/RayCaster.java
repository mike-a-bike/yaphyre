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

package yaphyre.core.tracers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yaphyre.core.api.CollisionInformation;
import yaphyre.core.api.Light;
import yaphyre.core.api.Scene;
import yaphyre.core.api.Tracer;
import yaphyre.core.math.Color;
import yaphyre.core.math.MathUtils;
import yaphyre.core.math.Point3D;
import yaphyre.core.math.Ray;
import yaphyre.core.math.Vector3D;

import java.util.Optional;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 28.06.14
 */
public class RayCaster implements Tracer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RayCaster.class);

    @Override
    public Optional<Color> traceRay(Ray ray, Scene scene) {
        LOGGER.trace("trace ray: " + ray);
        return scene.hitObject(ray)
            .map(
                collision -> {

                    double deltaLightIntensity = calculateIncidentLightIntesity(scene, collision);
                    double omnidirectionalIntensity = calculateOmnidirectionalLightIntensity(scene);

                    Color collisionColor = collision.getShape().getShader().getColor(collision.getUVCoordinate());
                    double cosPhi = collision.getIncidentRay().getDirection().normalize().neg().dot(collision.getNormal());

                    return collisionColor.multiply(deltaLightIntensity * cosPhi + omnidirectionalIntensity);
                }
            );
    }

    private double calculateIncidentLightIntesity(Scene scene, CollisionInformation collision) {
        return scene.getLights()
            .stream()
            .filter(light -> light.isDelta() && !light.isOmnidirectional())
            .peek(l -> LOGGER.trace("Sampling delta light: " + l))
            .mapToDouble(light -> calculateDirectLightIntensity(collision, light))
            .peek(d -> LOGGER.trace("Intensity: " + d))
            .sum();
    }

    private double calculateOmnidirectionalLightIntensity(Scene scene) {
        return scene.getLights()
            .stream()
            .filter(light -> light.isOmnidirectional() && !light.isDelta())
            .peek(l -> LOGGER.trace("Sampling omnidirectional light: " + l))
            .mapToDouble(Light::getPower)
            .peek(d -> LOGGER.trace("Intensity: " + d))
            .sum();
    }

    private double calculateDirectLightIntensity(CollisionInformation collision, Light light) {
        final Point3D collisionPoint = collision.getPoint();
        final Vector3D direction = light.getPosition().sub(collisionPoint);
        return light.calculateIntensityForShadowRay(
            new Ray(
                collisionPoint,
                direction,
                MathUtils.EPSILON,
                collision.getDistance()
            ));
    }

}
