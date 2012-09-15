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

import java.io.Serializable;

public class Material implements Serializable {

	private static final long serialVersionUID = -7396033500247262551L;

	private final double ambient;

	private final double diffuse;

	private final double specular;

	private final double reflection;

	private final double refraction;

	public Material(double ambient, double diffuse, double specular, double reflection, double refraction) {
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
		this.reflection = reflection;
		this.refraction = refraction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(ambient);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(diffuse);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(reflection);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(refraction);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(specular);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Material other = (Material) obj;
		if (Double.doubleToLongBits(ambient) != Double.doubleToLongBits(other.ambient))
			return false;
		if (Double.doubleToLongBits(diffuse) != Double.doubleToLongBits(other.diffuse))
			return false;
		if (Double.doubleToLongBits(reflection) != Double.doubleToLongBits(other.reflection))
			return false;
		if (Double.doubleToLongBits(refraction) != Double.doubleToLongBits(other.refraction))
			return false;
		if (Double.doubleToLongBits(specular) != Double.doubleToLongBits(other.specular))
			return false;
		return true;
	}

	public double getAmbient() {
		return this.ambient;
	}

	public double getDiffuse() {
		return this.diffuse;
	}

	public double getSpecular() {
		return this.specular;
	}

	public double getReflection() {
		return this.reflection;
	}

	public double getRefraction() {
		return this.refraction;
	}

}
