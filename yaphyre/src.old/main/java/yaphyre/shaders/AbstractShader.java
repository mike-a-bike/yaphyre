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
import yaphyre.geometry.Transformation;
import yaphyre.util.Color;

/**
 * An abstract super class which contains an optional {@link Transformation} used to transform the shader u/v
 * coordinates onto the object u/v coordinates.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 91 $
 */
public abstract class AbstractShader implements Shader {

	private static final long serialVersionUID = -309222730473881534L;

	private final Transformation shaderToObject;

	protected AbstractShader(Transformation shaderToObject) {
		this.shaderToObject = shaderToObject;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((shaderToObject == null) ? 0 : shaderToObject.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AbstractShader)) {
			return false;
		}
		AbstractShader other = (AbstractShader) obj;
		if (shaderToObject == null) {
			if (other.shaderToObject != null) {
				return false;
			}
		} else if (!shaderToObject.equals(other.shaderToObject)) {
			return false;
		}
		return true;
	}

	protected Transformation getShaderToOjbect() {
		return shaderToObject;
	}

	protected Point2D toShaderCoordinate(Point2D point) {
		if (shaderToObject == null || shaderToObject.equals(Transformation.IDENTITY)) {
			return point;
		}
		return shaderToObject.inverse().transform(point);
	}

	protected Point2D toObjectCoordinate(Point2D point) {
		if (shaderToObject == null || shaderToObject.equals(Transformation.IDENTITY)) {
			return point;
		}
		return shaderToObject.transform(point);
	}

	@Override
	public abstract Color getColor(Point2D uvCoordinate);

	@Override
	public abstract Material getMaterial(Point2D uvCoordinate);

}
