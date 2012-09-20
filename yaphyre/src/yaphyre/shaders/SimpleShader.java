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

import yaphyre.core.Shader;
import yaphyre.geometry.Point2D;
import yaphyre.util.Color;

/**
 * This class implements a very simple {@link Shader}. It contains a single material which is returned independent from
 * the object coordinates.
 *
 * @author Michael Bieri
 * @author $LastChangedBy$
 * @version $Revision$
 */
public class SimpleShader extends AbstractShader {

	private static final long serialVersionUID = 5072616518896339854L;

	private final Color color;

	private final Material material;

	public SimpleShader(Material material, java.awt.Color awtColor) {
		this(material, new Color(awtColor));
	}

	public SimpleShader(Material material, double red, double green, double blue) {
		this(material, new Color(red, green, blue));
	}

	public SimpleShader(Material material, Color color) {
		super(null);
		this.material = material;
		this.color = color;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		result = prime * result + color.hashCode();
		result = prime * result + material.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof  SimpleShader)) {
			return false;
		}
		SimpleShader other = (SimpleShader) obj;
		if (!super.equals(obj)) {
			return false;
		}
		if (!color.equals(other.color)) {
			return false;
		}
		if (!material.equals(other.material)) {
			return false;
		}
		return true;
	}

	@Override
	public Color getColor(Point2D uvCoordinate) {
		return color;
	}

	@Override
	public Material getMaterial(Point2D uvCoordinate) {
		return material;
	}

}
