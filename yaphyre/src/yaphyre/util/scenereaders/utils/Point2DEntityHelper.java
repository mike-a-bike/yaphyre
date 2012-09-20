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

import yaphyre.geometry.Point2D;

import org.joox.Match;

public class Point2DEntityHelper implements EntityHelper<Point2D> {

	public static final EntityHelper<Point2D> INSTANCE = new Point2DEntityHelper();

	@Override
	public Point2D decodeEntity(Match entityMatch) {
		double u = entityMatch.attr("u", Double.class);
		double v = entityMatch.attr("v", Double.class);
		return new Point2D(u, v);
	}

}
