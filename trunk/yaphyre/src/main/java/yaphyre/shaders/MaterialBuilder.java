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

/**
 * Helper class for building materials. Use it like this:<br/> <p/>
 * <pre>
 * Material mirror = MaterialBuilder.init().ambient(0.05).diffuse(0.2).reflection(0.8).build();
 * </pre>
 *
 * @author Michael Bieri
 */
public class MaterialBuilder {

	private double ambient;

	private double diffuse;

	private double specular;

	private double reflection;

	private double refraction;

	/**
	 * Needed because static and non static methods cannot be overloaded.
	 *
	 * @return A new instance of {@link MaterialBuilder} ready to use.
	 */
	public static MaterialBuilder start() {
		return new MaterialBuilder();
	}

	private MaterialBuilder() {
	}

	public MaterialBuilder ambient(double ambient) {
		this.ambient = ambient;
		return this;
	}

	public MaterialBuilder diffuse(double diffuse) {
		this.diffuse = diffuse;
		return this;
	}

	public MaterialBuilder specular(double specular) {
		this.specular = specular;
		return this;
	}

	public MaterialBuilder reflection(double reflection) {
		this.reflection = reflection;
		return this;
	}

	public MaterialBuilder refraction(double refraction) {
		this.refraction = refraction;
		return this;
	}

	/**
	 * Factory method. This builds the actual {@link Material} instance. Please notice, this method can be called multiple
	 * times.
	 *
	 * @return A new {@link Material} instance with all the values from the factory set.
	 */
	public Material build() {
		return new Material(ambient, diffuse, specular, reflection, refraction);
	}

}
