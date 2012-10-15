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
package yaphyre.scenereaders.yaphyre;

import static org.joox.JOOX.$;

import java.io.InputStream;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import yaphyre.core.Lightsource;
import yaphyre.core.Shader;
import yaphyre.core.Shape;
import yaphyre.raytracer.Scene;
import yaphyre.scenereaders.Capabilities;
import yaphyre.scenereaders.SceneReaders;
import yaphyre.scenereaders.yaphyre.entityhandlers.EntityHandler;
import yaphyre.scenereaders.yaphyre.entityhandlers.IdentifiableObject;
import yaphyre.scenereaders.yaphyre.entityhandlers.MaterialEntityHandler;
import yaphyre.scenereaders.yaphyre.entityhandlers.PlaneEntityHandler;
import yaphyre.scenereaders.yaphyre.entityhandlers.PointlightEntityHandler;
import yaphyre.scenereaders.yaphyre.entityhandlers.SimpleShaderEnityHandler;
import yaphyre.scenereaders.yaphyre.entityhandlers.SphereEntityHandler;
import yaphyre.shaders.Material;

import org.joox.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * XML document reader. Creates a {@link Scene} representation by reading an XML document in multiple stages. Since
 * various instances depend on each other, it is vital to parse the document in more than one go.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 37 $
 */
public class MultiStageXMLSceneReader implements SceneReaders {

	private static final Logger LOGGER = LoggerFactory.getLogger(MultiStageXMLSceneReader.class);

	private final Map<String, IdentifiableObject<Material>> knownMaterials = Maps.newHashMap();

	private final Map<String, IdentifiableObject<Shader>> knownShaders = Maps.newHashMap();

	private final Map<String, IdentifiableObject<Shape>> knownShapes = Maps.newHashMap();

	@Override
	public Scene readScene(InputStream source) {
		LOGGER.info("Starting scene reader");
		LOGGER.debug("source: {}", source);

		Scene result = null;

		try {
			Preconditions.checkNotNull(source);
			Match xmlDocument = $(source);
			result = new Scene();

			for (ReaderStage stage : ReaderStage.values()) {
				stage.handleStage(xmlDocument, result, knownMaterials, knownShaders, knownShapes);
			}

		} catch (Throwable throwable) {
			Throwables.propagate(throwable);
		}

		LOGGER.info("Scene read. Result = {}", result);
		return result;
	}

	@Override
	public Set<Capabilities> getCapabilities() {
		return EnumSet.allOf(Capabilities.class);
	}

	private static enum ReaderStage {
		Init{
			@Override
			void handleStage(final Match document, final Scene scene, final Map<String, IdentifiableObject<Material>> knownMaterials,
					final Map<String, IdentifiableObject<Shader>> knownShaders,
					final Map<String, IdentifiableObject<Shape>> knownShapes) { }
		},
		PreProcess{
			@Override
			void handleStage(final Match document, final Scene scene, final Map<String, IdentifiableObject<Material>> knownMaterials,
					final Map<String, IdentifiableObject<Shader>> knownShaders,
					final Map<String, IdentifiableObject<Shape>> knownShapes) { }
		},
		Material {
			@Override
			void handleStage(final Match document, final Scene scene, final Map<String, IdentifiableObject<Material>> knownMaterials,
					final Map<String, IdentifiableObject<Shader>> knownShaders,
					final Map<String, IdentifiableObject<Shape>> knownShapes) {
				for (EntityHandler<IdentifiableObject<Material>> materialHandler : MATERIAL_HANDLERS) {
					Match materials = document.xpath(materialHandler.getXPath());
					for (Match material : materials.each()) {
						IdentifiableObject<Material> decodedMaterial = materialHandler.decodeEntity(material, knownMaterials, knownShaders, knownShapes);
						knownMaterials.put(decodedMaterial.getId(), decodedMaterial);
					}
				}
			}
		},
		Shader {
			@Override
			void handleStage(final Match document, final Scene scene, final Map<String, IdentifiableObject<Material>> knownMaterials,
					final Map<String, IdentifiableObject<Shader>> knownShaders,
					final Map<String, IdentifiableObject<Shape>> knownShapes) {
				for (EntityHandler<IdentifiableObject<Shader>> shaderHandler : SHADER_HANDLERS) {
					Match shaders = document.xpath(shaderHandler.getXPath());
					for (Match shader : shaders.each()) {
						IdentifiableObject<Shader> decodedShader = shaderHandler.decodeEntity(shader, knownMaterials, knownShaders, knownShapes);
						knownShaders.put(decodedShader.getId(), decodedShader);
					}
				}
			}
		},
		Primitive {
			@Override
			void handleStage(final Match document, final Scene scene, final Map<String, IdentifiableObject<Material>> knownMaterials,
					final Map<String, IdentifiableObject<Shader>> knownShaders,
					final Map<String, IdentifiableObject<Shape>> knownShapes) {
				for (EntityHandler<IdentifiableObject<Shape>> shapeHandler : PRIMITIVE_HANDLERS) {
					Match primitives = document.xpath(shapeHandler.getXPath());
					for (Match primitive : primitives.each()) {
						IdentifiableObject<Shape> decodedPrimitive = shapeHandler.decodeEntity(primitive, knownMaterials, knownShaders, knownShapes);
						knownShapes.put(decodedPrimitive.getId(), decodedPrimitive);
						scene.addShape(decodedPrimitive.getObject());
					}
				}
			}
		},
		Complex {
			@Override
			void handleStage(final Match document, final Scene scene, final Map<String, IdentifiableObject<Material>> knownMaterials,
					final Map<String, IdentifiableObject<Shader>> knownShaders,
					final Map<String, IdentifiableObject<Shape>> knownShapes) {
				for(EntityHandler<IdentifiableObject<Shape>> complexShapeHandler : COMPLEX_SHAPE_HANDLERS) {
					Match complexShapes = document.xpath(complexShapeHandler.getXPath());
					for(Match complexShape : complexShapes.each()) {
						IdentifiableObject<Shape> decodedComplexShape = complexShapeHandler.decodeEntity(complexShape, knownMaterials, knownShaders, knownShapes);
						knownShapes.put(decodedComplexShape.getId(), decodedComplexShape);
						scene.addShape(decodedComplexShape.getObject());
					}
				}
			}
		},
		Light {
			@Override
			void handleStage(final Match document, final Scene scene, final Map<String, IdentifiableObject<Material>> knownMaterials,
					final Map<String, IdentifiableObject<Shader>> knownShaders,
					final Map<String, IdentifiableObject<Shape>> knownShapes) {
				for (EntityHandler<IdentifiableObject<Lightsource>> lightHandler : LIGHT_HANDLERS) {
					Match lights = document.xpath(lightHandler.getXPath());
					for (Match light : lights.each()) {
						scene.addLightsource(lightHandler.decodeEntity(light, knownMaterials, knownShaders, knownShapes).getObject());
					}
				}
			}
		},
		Camera{
			@Override
			void handleStage(final Match document, final Scene scene, final Map<String, IdentifiableObject<Material>> knownMaterials,
					final Map<String, IdentifiableObject<Shader>> knownShaders,
					final Map<String, IdentifiableObject<Shape>> knownShapes) { }
		},
		PostProcess {
			@Override
			void handleStage(final Match document, final Scene scene, final Map<String, IdentifiableObject<Material>> knownMaterials,
					final Map<String, IdentifiableObject<Shader>> knownShaders,
					final Map<String, IdentifiableObject<Shape>> knownShapes) { }
		},
		Finish {
			@Override
			void handleStage(final Match document, final Scene scene, final Map<String, IdentifiableObject<Material>> knownMaterials,
					final Map<String, IdentifiableObject<Shader>> knownShaders,
					final Map<String, IdentifiableObject<Shape>> knownShapes) { }
		};

		abstract void handleStage(Match document, Scene scene, final Map<String, IdentifiableObject<Material>> knownMaterials,
				final Map<String, IdentifiableObject<Shader>> knownShaders,
				final Map<String, IdentifiableObject<Shape>> knownShapes);

		private static final List<EntityHandler<IdentifiableObject<Material>>> MATERIAL_HANDLERS;

		private static final List<EntityHandler<IdentifiableObject<Shader>>> SHADER_HANDLERS;

		private static final List<EntityHandler<IdentifiableObject<Shape>>> PRIMITIVE_HANDLERS;

		private static final List<EntityHandler<IdentifiableObject<Shape>>> COMPLEX_SHAPE_HANDLERS;

		private static final List<EntityHandler<IdentifiableObject<Lightsource>>> LIGHT_HANDLERS;

		static {
			MATERIAL_HANDLERS = initMaterialHandlers();
			SHADER_HANDLERS = initShaderHandlers();
			PRIMITIVE_HANDLERS = initPrimitiveHandlers();
			COMPLEX_SHAPE_HANDLERS = initComplexShapesHandlers();
			LIGHT_HANDLERS = initLightHandlers();
		}

		private static List<EntityHandler<IdentifiableObject<Material>>> initMaterialHandlers() {
			List<EntityHandler<IdentifiableObject<Material>>> result = Lists.newArrayList();
			result.add(new MaterialEntityHandler());
			return result;
		}

		private static List<EntityHandler<IdentifiableObject<Shader>>> initShaderHandlers() {
			List<EntityHandler<IdentifiableObject<Shader>>> result = Lists.newArrayList();
			result.add(new SimpleShaderEnityHandler());
			return result;
		}

		private static List<EntityHandler<IdentifiableObject<Shape>>> initPrimitiveHandlers() {
			List<EntityHandler<IdentifiableObject<Shape>>> result = Lists.newArrayList();
			result.add(new SphereEntityHandler());
			result.add(new PlaneEntityHandler());
			return result;
		}

		private static List<EntityHandler<IdentifiableObject<Shape>>> initComplexShapesHandlers() {
			List<EntityHandler<IdentifiableObject<Shape>>> result = Lists.newArrayList();
			return result;
		}

		private static List<EntityHandler<IdentifiableObject<Lightsource>>> initLightHandlers() {
			List<EntityHandler<IdentifiableObject<Lightsource>>> result = Lists.newArrayList();
			result.add(new PointlightEntityHandler());
			return result;
		}

	}

}
