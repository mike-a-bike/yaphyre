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

import java.util.Optional;
import javax.annotation.Nonnull;
import yaphyre.core.math.BoundingBox;
import yaphyre.core.math.Ray;

/**
 * Interface implemented by all {@link Shape} of the rendering system.
 *
 * @author Michael Bieri
 * @author $LastChangedBy$
 * @version $Revision$
 */
public interface Shape {

	/**
	 * Gets the {@link Shader} instance associated with this shape instance.
	 *
	 * @return The {@link Shader} for this shape.
	 */
    @Nonnull
	public Shader getShader();

	/**
	 * Create the intersection informations for the given {@link yaphyre.core.math.Ray} and this shape.
     * If the {@link yaphyre.core.math.Ray} does not intersect this shape at all, then {@link java.util.Optional#empty()} is
     * returned. Otherwise the collision information are returned. The results created by this method are as accurate
     * as possible.
	 *
	 * @param ray The {@link yaphyre.core.math.Ray} to check for intersection.
	 *
	 * @return An {@link java.util.Optional} of {@link CollisionInformation} instance describing the
     * intersection between the {@link yaphyre.core.math.Ray} and this {@link Shape} instance.
     * {@link java.util.Optional#empty()} if the ray misses the Shape.
	 */
    @Nonnull
	public Optional<CollisionInformation> intersect(@Nonnull Ray ray);

    @Nonnull
	public BoundingBox getBoundingBox();

    @Nonnull
    public BoundingBox getAxisAlignedBoundingBox();

}
