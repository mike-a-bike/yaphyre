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
package yaphyre.shapes;

import yaphyre.core.BoundingBox;
import yaphyre.core.CollisionInformation;
import yaphyre.core.Shader;
import yaphyre.core.Shape;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Transformation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This shape is special since it does not represent a shape itself. It is a wrapper around another shape which is
 * transformed by a defined transformation. This is used when complicated objects which use a lot of memory should be
 * used multiple times in a scene.<br/> All methods just transform the {@link Point3D}, {@link Ray} or {@link Normal3D}
 * instances using the given transformation in order to calculate the required informations.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 66 $
 */
public class Instance extends AbstractShape {

	private static final long serialVersionUID = -8356972729048615712L;

	private final Shape baseShape;

	private final Transformation instanceTransformation;

	private final BoundingBox boundingBox;

	public Instance(@NotNull Shape baseShape, @NotNull Transformation instanceTransformation, @NotNull Shader shader) {
		super(Transformation.IDENTITY, shader);
		this.baseShape = baseShape;
		this.instanceTransformation = instanceTransformation;
		// TODO implement transformation for base shape bounding box.
		boundingBox = BoundingBox.INFINITE_BOUNDING_BOX;
	}

	@Nullable
	@Override
	public CollisionInformation intersect(@NotNull final Ray ray) {
		final Ray transformedRay = instanceTransformation.inverse().transform(ray);
		if (boundingBox.isHitBy(ray)) {
			return baseShape.intersect(transformedRay);
		}
		return null;
	}

	@NotNull
	@Override
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}
}
