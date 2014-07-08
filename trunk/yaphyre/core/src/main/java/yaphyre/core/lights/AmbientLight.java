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

import yaphyre.core.math.Color;
import yaphyre.core.math.Ray;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 05.07.13
 */
public class AmbientLight extends AbstractLight {

    public AmbientLight(double power) {
        super(power, Color.WHITE);
    }

    @Override
    public boolean isOmnidirectional() {
        return true;
    }

    @Override
    public boolean isDelta() {
        return false;
    }

    @Override
    public double calculateIntensityForShadowRay(@Nonnull Ray shadowRay) {
        return getPower();
    }
}
