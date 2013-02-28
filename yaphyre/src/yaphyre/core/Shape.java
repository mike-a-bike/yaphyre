/*
 * Copyright 2012 Michael Bieri
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package yaphyre.core;

import java.io.Serializable;

import yaphyre.geometry.Ray;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface implemented by all {@link Shape} of the rendering system.
 *
 * @author Michael Bieri
 * @author $LastChangedBy$
 * @version $Revision$
 */
public interface Shape extends Serializable {

	/**
	 * Gets the {@link Shader} instance associated with this shape instance.
	 *
	 * @return The {@link Shader} for this shape.
	 */
	@NotNull
	public Shader getShader();

	/**
	 * Create the intersection informations for the given {@link Ray} and this shape. If the {@link Ray} does not
	 * intersect this shape at all, then <code>null</code> is returned. Otherwise the collision informations are returned.
	 * The results created by this method are as accurate as possible.
	 *
	 * @param ray
	 * 		The {@link Ray} to check for intersection.
	 *
	 * @return The {@link CollisionInformation} instance describing the intersection between the {@link Ray} and this
	 *         {@link Shape} instance. <code>null</code> if no intersection happens.
	 */
	@Nullable
	public CollisionInformation intersect(@NotNull Ray ray);

	@NotNull
	public BoundingBox getBoundingBox();

}
