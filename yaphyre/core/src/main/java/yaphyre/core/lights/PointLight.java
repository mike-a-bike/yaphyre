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

package yaphyre.core.lights;

import java.util.Optional;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yaphyre.core.api.CollisionInformation;
import yaphyre.core.math.Color;
import yaphyre.core.math.Point3D;
import yaphyre.core.math.Ray;

/**
 * Point light implementation. A mathematical (delta) light source having no physical size and sending an
 * equal amount of light in all directions.
 *
 * @author Michael Bieri
 * @since 08.07.13
 */
public class PointLight extends AbstractLight {

    private static final Logger LOGGER = LoggerFactory.getLogger(PointLight.class);

    private final Point3D position;

    public PointLight(double power, Color color, Point3D position) {
        super(power, color);
        this.position = position;
    }

    public Point3D getPosition() {
        return position;
    }

    /**
     * This class models a mathematical light source which exists as a point and thus has no physical size
     * whatsoever.
     */
    @Override
    public boolean isDelta() {
        return true;
    }

    @Override
    @Nonnull
    public Color calculateIntensityForShadowRay(@Nonnull Ray shadowRay) {
        Optional<CollisionInformation> collisionInformationOptional = getScene().hitObjectForShadowRay(shadowRay);

        if (LOGGER.isTraceEnabled() && collisionInformationOptional.isPresent()) {
            LOGGER.trace("shadowRay collision: {}", collisionInformationOptional);
        }

        return collisionInformationOptional
            .map(
                coll -> Color.BLACK
            )
            .orElseGet(
                () -> getColor().multiply(calculateIntensityForDistance(getPosition().sub(shadowRay.getOrigin()).length()))
            );
    }

    @Nonnegative
    private double calculateIntensityForDistance(@Nonnegative double distance) {
        return attenuationForDistance(distance) * getPower();
    }

}
