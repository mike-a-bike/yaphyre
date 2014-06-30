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

package yaphyre.core.shaders;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import yaphyre.core.api.Material;
import yaphyre.core.api.Shader;
import yaphyre.core.math.Color;
import yaphyre.core.math.Point2D;

/**
 * Simple shader returning a constant color for all points. This shader does not take into account different materials.
 *
 * @author axmbi03
 * @since 30.06.2014
 */
@Immutable
public class ColorShader implements Shader {

    private final Color color;

    public ColorShader(@Nonnull Color color) {
        this.color = color;
    }

    @Nonnull
    @Override
    public Color getColor(@Nonnull Point2D uvCoordinate) {
        return color;
    }

    @Nonnull
    @Override
    public Material getMaterial(@Nonnull Point2D uvCoordinate) {
        return null;
    }

}
