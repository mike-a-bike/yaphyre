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
import javax.annotation.Nonnull;

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

/**
 * A simple ray caster algorithm. Taking into account omnidirectional and mathematical lights.
 *
 * @author Michael Bieri
 * @since 28.06.14
 */
public class RayCaster implements Tracer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RayCaster.class);

    /** 'Empty' ray for usage when the position and/or direction does not matter. */
    public static final Ray DUMMY_RAY = new Ray(Point3D.ORIGIN, Vector3D.Y);

    /**
     * Trace the given ray through the given scene. This includes the interaction of all objects and materials within
     * the scene.
     *
     * @param ray The ray to calculate the shading for.
     * @param scene The scene holding the light sources and objects.
     * @return The exitance spectrum for the given ray and scene.
     */
    @Override
    @Nonnull
    public Optional<Color> traceRay(@Nonnull Ray ray, @Nonnull Scene scene) {
        LOGGER.trace("trace ray: " + ray);
        return scene.hitObject(ray)
            .map(
                collision -> {

                    Color deltaLightIntensity = calculateIncidentLightIntensity(scene, collision);
                    Color omnidirectionalIntensity = calculateOmnidirectionalLightIntensity(scene);

                    Color collisionColor = collision.getShape().getShader().getColor(collision.getUVCoordinate());
                    double cosPhi = collision.getIncidentRay().getDirection().normalize().neg().dot(collision.getNormal());

                    return collisionColor.multiply(deltaLightIntensity.multiply(cosPhi).add(omnidirectionalIntensity));
                }
            );
    }

    /**
     * Calculate the contribution of the omnidirectional (ambient) lights.
     *
     * @param scene The scene holding the light sources.
     * @return The contributed light.
     */
    private Color calculateOmnidirectionalLightIntensity(Scene scene) {
        return scene.getLights()
            .stream()
            .filter(light -> light.isOmnidirectional() && !light.isDelta())
            .map(light -> light.calculateIntensityForShadowRay(DUMMY_RAY))
            .reduce(Color.BLACK, (color1, color2) -> color1.add(color2));
    }

    /**
     * Calculate the contribution of the delta (mathematical) modeled lights.
     *
     * @param scene The scene holding the light sources.
     * @param collision The collision point of the view ray with a shape. This represents the point to calculate the
     * light contribution for.
     * @return The incident light for the sampling point.
     */
    private Color calculateIncidentLightIntensity(Scene scene, CollisionInformation collision) {
        return scene.getLights()
            .stream()
            .filter(light -> light.isDelta() && !light.isOmnidirectional())
            .map(light -> calculateDirectLightIntensity(collision, light))
            .reduce(Color.BLACK, (color1, color2) -> color1.add(color2));
    }

    private Color calculateDirectLightIntensity(CollisionInformation collision, Light light) {
        Point3D collisionPoint = collision.getPoint();
        Vector3D direction = light.getPosition().sub(collisionPoint);
        Vector3D directionNormalized = direction.normalize();
        return light.calculateIntensityForShadowRay(
            new Ray(
                collisionPoint.add(directionNormalized.scale(MathUtils.EPSILON)), // prevent self-shadowing
                directionNormalized,
                MathUtils.EPSILON,
                direction.length()
            ));
    }

}
