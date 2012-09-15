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

package yaphyre.util.scenereaders.entityhandlers;

import org.joox.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yaphyre.core.Shader;
import yaphyre.core.Shape;
import yaphyre.shaders.Material;
import yaphyre.shaders.SimpleShader;
import yaphyre.util.Color;
import yaphyre.util.scenereaders.utils.HelperFactory;

import java.util.Map;

public class SimpleShaderEnityHandler extends EntityHandler<IdentifiableObject<Shader>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleShaderEnityHandler.class);

	@Override
	public IdentifiableObject<Shader> decodeEntity(Match entityMatch, Map<String, IdentifiableObject<Material>> knownMaterials, Map<String, IdentifiableObject<Shader>> knownShaders, Map<String, IdentifiableObject<Shape>> knownShapes) {

		LOGGER.trace("enter decodeEntity: {}", entityMatch);

		String id = entityMatch.id();
		Color shaderColor = HelperFactory.getColorHelper().decodeEntity(entityMatch.child("color"));
		String materialRef = entityMatch.child("material").attr("ref");
		Material material = knownMaterials.get(materialRef).getObject();

		IdentifiableObject<Shader> result = new IdentifiableObject<Shader>(id, new SimpleShader(material, shaderColor));

		LOGGER.trace("exit decodeEntity: {}", result);

		return result;
	}

	@Override
	public String getXPath() {
		return "/scene/shader[@type = \"simple\"]";
	}

}
