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

import yaphyre.geometry.Point3D;

import org.joox.Match;

class Point3DEntityHelper implements EntityHelper<Point3D> {

	public static final EntityHelper<Point3D> INSTANCE = new Point3DEntityHelper();

	@Override
	public Point3D decodeEntity(Match entityMatch) {
		double x = entityMatch.attr("x", Double.class);
		double y = entityMatch.attr("y", Double.class);
		double z = entityMatch.attr("z", Double.class);
		return new Point3D(x, y, z);
	}

}
