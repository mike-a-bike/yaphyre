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

package yaphyre.util.scenereaders.utils;

import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Transformation;
import yaphyre.geometry.Vector3D;
import yaphyre.util.Color;

public abstract class HelperFactory {

	public static EntityHelper<Color> getColorHelper() {
		return ColorEntityHelper.INSTANCE;
	}

	public static EntityHelper<Vector3D> getVector3DHelper() {
		return Vector3DEntityHelper.INSTANCE;
	}

	public static EntityHelper<Normal3D> getNormal3DHelper() {
		return Normal3DEntityHelper.INSTANCE;
	}

	public static EntityHelper<Point3D> getPoint3DHelper() {
		return Point3DEntityHelper.INSTANCE;
	}

	public static EntityHelper<Point2D> getPoint2DHelper() {
		return Point2DEntityHelper.INSTANCE;
	}

	public static EntityHelper<Transformation> getTransformationHelper() {
		return TransformationEntityHelper.INSTANCE;
	}

}
