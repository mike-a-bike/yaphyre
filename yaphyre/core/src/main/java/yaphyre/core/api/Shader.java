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

import javax.annotation.Nonnull;

import yaphyre.core.math.Color;
import yaphyre.core.math.Point2D;

/**
 * The common interface for all shading activities. It provides two method. One for accessing the material at the given
 * u- v- coordinates. And another for accessing the material properties at the given coordinates.
 *
 * @author Michael Bieri
 * @author $LastChangedBy$
 * @version $Revision$
 */
public interface Shader {

    @Nonnull
    Color getColor(@Nonnull final Point2D uvCoordinate);

    @Nonnull
    Material getMaterial(@Nonnull final Point2D uvCoordinate);

}
