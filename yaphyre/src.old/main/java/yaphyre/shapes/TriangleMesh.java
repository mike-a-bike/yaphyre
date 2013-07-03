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

import yaphyre.core.BoundingBox;
import yaphyre.core.CollisionInformation;
import yaphyre.core.Shader;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Transformation;

/**
 * Created with IntelliJ IDEA. User: michael Date: 16.02.13 Time: 13:02 To change this template use File | Settings | File
 * Templates.
 */
public class TriangleMesh extends AbstractShape {

	private final Point3D[] vertices;
	private final int[][] triangles;

	/**
	 * Initialize the common fields for all {@link yaphyre.core.Shape}s. Each {@link yaphyre.core.Shape} defines a point of origin for
	 * its own, which is translated to the world coordinate space using the given transformation. {@link yaphyre.geometry.Ray}s are
	 * translated by the inverse of the {@link yaphyre.geometry.Transformation} to calculate an eventual intersection.</br> Please
	 * remember, that the order of the {@link yaphyre.geometry.Transformation} matters. It is not the same if the object is rotated an
	 * then translated or first translated and then rotated.
	 *
	 * @param objectToWorld The {@link yaphyre.geometry.Transformation} used to map world coordinates to object coordinates.
	 * @param shader        The {@link yaphyre.core.Shader} instance to use when rendering this {@link yaphyre.core.Shape}.
	 * @param vertices      An array of {@link Point3D} representing vertices used as points of triangles.
	 * @param triangles     A two dimensional array containing indices of vertices defining the corners of the triangles. Each sub-
	 *                      array has to have a length of 3 since each entry represents a triangle (no n-gon support with implicit
	 *                      tessellation yet)
	 */
	protected TriangleMesh(final Transformation objectToWorld, final Shader shader,
	                       final Point3D[] vertices, final int[][] triangles) {
		super(objectToWorld, shader);
		this.vertices = new Point3D[vertices.length];
		System.arraycopy(vertices, 0, this.vertices, 0, vertices.length);
		this.triangles = new int[triangles.length][3];
		for (int triangleIndex = 0; triangleIndex < triangles.length; triangleIndex++) {
			if (triangles[triangleIndex].length != 3) {
				throw new IllegalArgumentException("Please make sure, that all triangles have exactly three vertices.");
			}
			System.arraycopy(triangles[triangleIndex], 0, this.triangles[triangleIndex], 0, 3);
		}
	}

	@Override
	public CollisionInformation intersect(final Ray ray) {
		return null;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return null;
	}

}
