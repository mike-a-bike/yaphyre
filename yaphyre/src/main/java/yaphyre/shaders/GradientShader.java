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
package yaphyre.shaders;

import yaphyre.geometry.Point2D;
import yaphyre.util.Color;

/**
 * Basic shader which blends two colors depending on the coordinates. So, it is like a
 * procedural shader
 *
 * @author Michael Bieri
 * @author $LastChangedBy$
 * @version $Revision$
 */
public class GradientShader extends AbstractShader {

	private final Color fromColor, toColor;

	private final BlendDirection blendDirection;

	private final Material material;

	public GradientShader(Material material, Color fromColor, Color toColor, BlendDirection blendDirection) {
		super(null);
		this.material = material;
		this.fromColor = fromColor;
		this.toColor = toColor;
		this.blendDirection = blendDirection;
	}

	@Override
	public Color getColor(Point2D uvCoordinate) {
		return blendDirection.getBlendedColor(fromColor, toColor, uvCoordinate);
	}

	@Override
	public Material getMaterial(Point2D uvCoordinate) {
		return material;
	}

	public enum BlendDirection {
		uAxis {
			@Override
			protected Color getBlendedColor(final Color fromColor, final Color toColor, final Point2D uvCoordinate) {
				Color result = Color.BLACK;
				result = result.add(toColor.multiply(uvCoordinate.getU()));
				result = result.add(fromColor.multiply(1d - uvCoordinate.getU()));
				return result;
			}
		},
		vAxis {
			@Override
			protected Color getBlendedColor(final Color fromColor, final Color toColor, final Point2D uvCoordinate) {
				Color result = Color.BLACK;
				result = result.add(toColor.multiply(uvCoordinate.getV()));
				result = result.add(fromColor.multiply(1d - uvCoordinate.getV()));
				return result;
			}
		};

		abstract protected Color getBlendedColor(final Color fromColor, final Color toColor, final Point2D uvCoordinate);
	}

}
