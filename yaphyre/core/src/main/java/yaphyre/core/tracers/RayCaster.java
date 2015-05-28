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

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
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

    /**
     * 'Empty' ray for usage when the position and/or direction does not matter.
     */
    public static final Ray DUMMY_RAY = new Ray(Point3D.ORIGIN, Vector3D.Y);

    /**
     * Trace the given ray through the given scene. This includes the interaction of all objects and materials within
     * the scene.
     *
     * @param ray   The ray to calculate the shading for.
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

                            List<Light> sceneLights = scene.getLights();

                            Color deltaLightIntensity = calculateLightContribution(
                                    sceneLights,
                                    light -> light.isDelta() && !light.isOmnidirectional(),
                                    light -> calculateDirectLightIntensity(collision, light)
                            );

                            Color omnidirectionalIntensity = calculateLightContribution(
                                    sceneLights,
                                    light -> light.isOmnidirectional() && !light.isDelta(),
                                    light -> light.calculateIntensityForShadowRay(DUMMY_RAY)
                            );

                            Color collisionColor = collision.getShape().getShader().getColor(collision.getUVCoordinate());
                            double cosPhi = collision.getIncidentRay().getDirection().normalize().neg().dot(collision.getNormal());

                            return collisionColor.multiply(deltaLightIntensity.multiply(cosPhi).add(omnidirectionalIntensity));
                        }
                );
    }

    /**
     * Calculate the contribution of the selected collection of lights.
     *
     * @param lights             The collection of lights to consider.
     * @param lightPredicate     Filter selecting the light collection to calculate the contribution for.
     * @param lightColorFunction Mapping function calculating the contribution of a single light source.
     * @return The contributed light.
     */
    @Nonnull
    private Color calculateLightContribution(@Nonnull List<Light> lights,
                                             @Nonnull Predicate<Light> lightPredicate,
                                             @Nonnull Function<Light, Color> lightColorFunction) {
        return lights.stream()
                .filter(lightPredicate)
                .map(lightColorFunction)
                .reduce(Color.BLACK, (color1, color2) -> color1.add(color2));
    }

    private Color calculateDirectLightIntensity(CollisionInformation collision, Light light) {
        Point3D shadowRayOrigin = collision.getPoint().add(collision.getNormal().scale(MathUtils.EPSILON)); // prevent self-shadowing
        Vector3D direction = light.getPosition().sub(shadowRayOrigin);
        Vector3D directionNormalized = direction.normalize();
        return light.calculateIntensityForShadowRay(
                new Ray(
                        shadowRayOrigin,
                        directionNormalized,
                        0d, // start at zero to get correct results when the shape is convex.
                        direction.length()
                ));
    }

}
