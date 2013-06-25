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

package yaphyre.scenereaders.yaphyre.entityhandlers;

import java.util.Map;

import yaphyre.core.Shader;
import yaphyre.core.Shape;
import yaphyre.shaders.Material;

import org.joox.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class MaterialEntityHandler extends EntityHandler<IdentifiableObject<Material>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MaterialEntityHandler.class);

	@Override
	public IdentifiableObject<Material> decodeEntity(Match entityMatch, Map<String, IdentifiableObject<Material>> knownMaterials, Map<String, IdentifiableObject<Shader>> knownShaders, Map<String, IdentifiableObject<Shape>> knownShapes) {
		LOGGER.trace("enter decodeEntity: {}", entityMatch);

		Preconditions.checkArgument(entityMatch.tag().equals("material"));

		String id = entityMatch.id();
		double ambient = readNumericAttribute(entityMatch, "ambient", 0d, Double.class);
		double diffuse = readNumericAttribute(entityMatch, "diffuse", 0d, Double.class);
		double specular = readNumericAttribute(entityMatch, "specular", 0d, Double.class);
		double reflection = readNumericAttribute(entityMatch, "reflection", 0d, Double.class);
		double refraction = readNumericAttribute(entityMatch, "refraction", 0d, Double.class);

		IdentifiableObject<Material> result = new IdentifiableObject<Material>(id, new Material(ambient, diffuse,
				specular, reflection, refraction));

		LOGGER.trace("exit decodeEntity: {}", result);

		return result;
	}

	@Override
	public String getXPath() {
		return "/scene/material";
	}

}
