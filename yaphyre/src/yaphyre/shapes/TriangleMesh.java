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

import yaphyre.core.Shader;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Transformation;

/**
 * Created with IntelliJ IDEA. User: michael Date: 16.02.13 Time: 13:02 To change this template use File | Settings | File
 * Templates.
 */
public class TriangleMesh extends AbstractShape {

	/**
	 * Initialize the common fields for all {@link yaphyre.core.Shape}s. Each {@link yaphyre.core.Shape} defines a point of origin for
	 * its own, which is translated to the world coordinate space using the given transformation. {@link yaphyre.geometry.Ray}s are
	 * translated by the inverse of the {@link yaphyre.geometry.Transformation} to calculate an eventual intersection.</br> Please
	 * remember, that the order of the {@link yaphyre.geometry.Transformation} matters. It is not the same if the object is rotated an
	 * then translated or first translated and then rotated.
	 *
	 *
	 * @param objectToWorld
	 * 		The {@link yaphyre.geometry.Transformation} used to map world coordinates to object coordinates.
	 * @param shader
	 * 		The {@link yaphyre.core.Shader} instance to use when rendering this {@link yaphyre.core.Shape}.
	 * @throws NullPointerException
	 * 		If either <code>objectToWorld</code> or <code>shader</code> is <code>null</code> a {@link NullPointerException} is
	 * 		thrown
	 */
	protected TriangleMesh(final Transformation objectToWorld, final Shader shader)
			throws NullPointerException {
		super(objectToWorld, shader);
	}

	@Override
	public Normal3D getNormal(final Point3D surfacePoint) {
		return null;
	}

	@Override
	public Point2D getMappedSurfacePoint(final Point3D surfacePoint) {
		return null;
	}

	@Override
	public double getIntersectDistance(final Ray ray) {
		return 0;
	}
}
