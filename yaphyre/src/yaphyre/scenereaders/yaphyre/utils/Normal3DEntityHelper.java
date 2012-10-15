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

package yaphyre.scenereaders.yaphyre.utils;

import yaphyre.geometry.Normal3D;

import org.joox.Match;

public class Normal3DEntityHelper implements EntityHelper<Normal3D> {

	public static final EntityHelper<Normal3D> INSTANCE = new Normal3DEntityHelper();

	@Override
	public Normal3D decodeEntity(Match entityMatch) {
		double x = entityMatch.attr("x", Double.class);
		double y = entityMatch.attr("y", Double.class);
		double z = entityMatch.attr("z", Double.class);
		return new Normal3D(x, y, z);
	}

}
