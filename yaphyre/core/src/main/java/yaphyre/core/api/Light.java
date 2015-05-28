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

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

import com.google.inject.BindingAnnotation;

import yaphyre.core.math.Color;
import yaphyre.core.math.Point3D;
import yaphyre.core.math.Ray;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 05.07.13
 */
public interface Light {

    /**
     * Flag signaling that this light source has no physical size. Useful for mathematical light
     * sources like point lights. It's sampling response follows a delta function, hence the name.
     *
     * @return true if the light has no physical size.
     */
    public boolean isDelta();

    /**
     * Flag signaling an omnidirectional light source. Ambient light is a valid example for this.
     * No intersection is calculated ever, but the intensity is applied to each point at the end of the
     * light calculation.
     *
     * @return true if the represented light source is omnidirectional.
     */
    public boolean isOmnidirectional();

    /**
     * For delta light sources their exact position is relevant.
     *
     * @return A position in world coordinates representing the mathematical point emitting light.
     */
    public Point3D getPosition();

    /**
     * @return Gets the spectrum of the light source. This is useful for omnidirectional and delta light sources.
     */
    public Color getColor();

    /**
     * @return The total power output of the light source.
     */
    public double getPower();

    /**
     * Calculate the intensity of the spectrum for the given shadow Ray. If the ray is blocked, no light is contributed.
     *
     * @param shadowRay The shadow ray to calculate the intensity for.
     * @return The spectrum of the light contribution.
     */
    @Nonnull
    public Color calculateIntensityForShadowRay(@Nonnull Ray shadowRay);

    /**
     * Marker interface to denote which type of sampler is to be injected.
     */
    @BindingAnnotation
    @Target({FIELD, METHOD, PARAMETER})
    @Retention(RUNTIME)
    public @interface LightSampler {
    }
}
