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

import javax.annotation.Nonnull;
import yaphyre.core.api.CollisionInformation;
import yaphyre.core.math.Color;
import yaphyre.core.math.Point3D;
import yaphyre.core.math.Ray;

import static org.apache.commons.math3.util.FastMath.pow;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 08.07.13
 */
public class PointLight extends AbstractLight {

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
    public double calculateIntensityForShadowRay(@Nonnull Ray shadowRay) {
        return getScene().hitObject(shadowRay)
            .map(CollisionInformation::getDistance)
            .filter(d -> d < getPosition().sub(shadowRay.getOrigin()).length())
            .map(d -> 1d / pow((1d + d), 3))
            .orElse(0d);
    }

}
