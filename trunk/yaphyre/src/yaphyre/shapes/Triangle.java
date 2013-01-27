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

public class Triangle extends AbstractShape {

	private final Point3D a, b, c;

	private final Normal3D normal;

	public Triangle(final Point3D a, final Point3D b, final Point3D c, final Shader shader, final boolean throwsShadow) {
		super(Transformation.IDENTITY, shader, throwsShadow);
		this.a = a;
		this.b = b;
		this.c = c;
		normal = b.sub(a).cross(c.sub(a)).normalize().asNormal();
	}

	@Override
	public double getIntersectDistance(final Ray ray) {
		return 0;
	}

	@Override
	public Normal3D getNormal(final Point3D surfacePoint) {
		return normal;
	}

	@Override
	public Point2D getMappedSurfacePoint(final Point3D surfacePoint) {
		return null;
	}

	@Override
	public boolean isInside(final Point3D point) {
		return false;
	}

}
