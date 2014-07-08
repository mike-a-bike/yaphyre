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

import java.util.Optional;

import yaphyre.core.api.Light;
import yaphyre.core.api.Scene;
import yaphyre.core.api.Tracer;
import yaphyre.core.math.Color;
import yaphyre.core.math.MathUtils;
import yaphyre.core.math.Point3D;
import yaphyre.core.math.Ray;
import yaphyre.core.math.Vector3D;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 28.06.14
 */
public class RayCaster implements Tracer {

    @Override
    public Optional<Color> traceRay(Ray ray, Scene scene) {
        return scene.hitObject(ray).map(
            collision -> {
                final Color collisionColor = collision.getShape().getShader().getColor(collision.getUVCoordinate());
                final double cosPhi = collision.getIncidentRay().getDirection().normalize().neg().dot(collision.getNormal());
                final double deltaLightIntensity = scene.getLights()
                    .stream()
                    .filter(light -> light.isDelta() && !light.isOmnidirectional())
                    .mapToDouble(light -> {
                        final Point3D collisionPoint = collision.getPoint();
                        final Vector3D direction = light.getPosition().sub(collisionPoint);
                        return light.calculateIntensityForShadowRay(
                            new Ray(
                                collisionPoint,
                                direction,
                                MathUtils.EPSILON,
                                collision.getDistance()
                            ));
                    })
                    .reduce(0d, (d1, d2) -> d1 + d2);
                final double omnidirectionalIntensity = scene.getLights()
                    .stream()
                    .filter(light -> light.isOmnidirectional() && !light.isDelta())
                    .mapToDouble(Light::getPower)
                    .reduce(0d, (d1, d2) -> d1 + d2);
                return collisionColor.multiply(deltaLightIntensity * cosPhi + omnidirectionalIntensity);
            }
        );
    }

}
