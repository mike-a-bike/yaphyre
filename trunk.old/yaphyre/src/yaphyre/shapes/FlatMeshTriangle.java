/*
 * Copyright 2013 Michael Bieri
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

import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;

import org.jetbrains.annotations.NotNull;

public class FlatMeshTriangle extends MeshTriangle {

	private final Normal3D normal;

	@NotNull
	public static FlatMeshTriangle create(@NotNull final Point3D v0, @NotNull final Point3D v1, @NotNull final Point3D v2) {
		return new FlatMeshTriangle(v0, v1, v2, new Point2D(0, 0), new Point2D(1, 0), new Point2D(0, 1));
	}

	@NotNull
	public static FlatMeshTriangle create(@NotNull final Point3D v0, @NotNull final Point3D v1, @NotNull final Point3D v2,
			@NotNull final Point2D uv0, @NotNull final Point2D uv1, @NotNull final Point2D uv2) {

		return new FlatMeshTriangle(v0, v1, v2, uv0, uv1, uv2);

	}

	public FlatMeshTriangle(@NotNull final Point3D v0, @NotNull final Point3D v1, @NotNull final Point3D v2,
			@NotNull Point2D uv0, @NotNull Point2D uv1, @NotNull Point2D uv2) {
		super(v0, v1, v2, uv0, uv1, uv2);

		normal = v1.sub(v0).cross(v2.sub(v0)).normalize().asNormal();
	}

	@NotNull
	@Override
	protected Normal3D calculateNormal(final double alpha, final double beta, final double gamma) {
		return normal;
	}

}
