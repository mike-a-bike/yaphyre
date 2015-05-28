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

package yaphyre.core.lights;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.inject.Inject;

import yaphyre.core.api.Light;
import yaphyre.core.api.Sampler;
import yaphyre.core.api.Scene;
import yaphyre.core.math.Color;
import yaphyre.core.math.Point3D;

/**
 * Base class for all light sources. This contains the following properties:
 * <ul>
 * <li>color: The color of the light emited by this light.</li>
 * <li>power: The power of the light source.</li>
 * <li>sampler: The sampler used to sample light values.</li>
 * <li>scene: A reference to the containing scene.</li>
 * </ul>
 *
 * @author axmbi03
 * @since 07.07.2014
 */
public abstract class AbstractLight implements Light {

    private final Color color;
    private final double power;
    private Sampler sampler;
    private Scene scene;

    public AbstractLight(double power, Color color) {
        this.color = color;
        this.power = power;
    }

    @Override
    public boolean isDelta() {
        return false;
    }

    @Override
    public boolean isOmnidirectional() {
        return false;
    }

    @Override
    public Point3D getPosition() {
        return Point3D.ORIGIN;
    }

    public Sampler getSampler() {
        return sampler;
    }

    @Inject
    public void setSampler(@Nonnull @LightSampler Sampler sampler) {
        this.sampler = sampler;
    }

    public Scene getScene() {
        return scene;
    }

    @Inject
    public void setScene(@Nonnull Scene scene) {
        this.scene = scene;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public double getPower() {
        return power;
    }

    @Nonnegative
    protected double attenuationForDistance(@Nonnegative double distance) {
        double attenuationFactor = 1 + distance;
        return 1d / (attenuationFactor * attenuationFactor);
    }
}
