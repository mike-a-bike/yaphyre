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

import org.joox.Match;
import yaphyre.util.Color;

final class ColorEntityHelper implements EntityHelper<Color> {

	public static final EntityHelper<Color> INSTANCE = new ColorEntityHelper();

	@Override
	public Color decodeEntity(Match entityMatch) {
		double r = entityMatch.attr("r", Double.class);
		double g = entityMatch.attr("g", Double.class);
		double b = entityMatch.attr("b", Double.class);
		return new Color(r, g, b);
	}
}