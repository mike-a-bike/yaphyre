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

package yaphyre.core.shapes;

import java.util.Optional;
import javax.annotation.Nonnull;

import yaphyre.core.api.CollisionInformation;
import yaphyre.core.api.Shader;
import yaphyre.core.math.BoundingBox;
import yaphyre.core.math.Ray;
import yaphyre.core.math.Transformation;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 28.06.14
 */
public class Torus extends AbstractShape {

    private static final double INNER_RADIUS = .25d;
    private static final double INNER_RADIUS_2 = INNER_RADIUS * INNER_RADIUS;
    private static final double OUTER_RADIUS = 1d;
    private static final double OUTER_RADIUS_2 = OUTER_RADIUS * OUTER_RADIUS;

    /**
     * Initialize the common fields for all {@link yaphyre.core.api.Shape}s. Each {@link yaphyre.core.api.Shape} defines a point of origin for its own,
     * which is translated to the world coordinate space using the given transformation. {@link yaphyre.core.math.Ray}s are translated by
     * the inverse of the {@link yaphyre.core.math.Transformation} to calculate an eventual intersection.</br> Please remember, that the
     * order of the {@link yaphyre.core.math.Transformation} matters. It is not the same if the object is rotated an then translated or
     * first translated and then rotated.
     *
     * @param objectToWorld The {@link yaphyre.core.math.Transformation} used to map world coordinates to object coordinates.
     * @param shader        The {@link yaphyre.core.api.Shader} instance to use when rendering this {@link yaphyre.core.api.Shape}.
     */
    protected Torus(@Nonnull Transformation objectToWorld, @Nonnull Shader shader) {
        super(objectToWorld, shader);
    }

    @Nonnull
    @Override
    public Optional<CollisionInformation> intersect(@Nonnull Ray ray) {
        return Optional.empty();
    }

    @Nonnull
    @Override
    public BoundingBox getBoundingBox() {
        return BoundingBox.INFINITE_BOUNDING_BOX;
    }
}
