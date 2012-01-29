/*
 * Copyright 2011 Michael Bieri
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
package yaphyre.util.scenereaders;

import static org.joox.JOOX.$;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.joox.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yaphyre.core.Lightsource;
import yaphyre.core.SceneReaders;
import yaphyre.core.Shader;
import yaphyre.core.Shape;
import yaphyre.raytracer.Scene;
import yaphyre.shaders.Material;
import yaphyre.util.scenereaders.entityhandlers.EntityHandler;
import yaphyre.util.scenereaders.entityhandlers.IdentifiableObject;
import yaphyre.util.scenereaders.entityhandlers.MaterialEntityHandler;
import yaphyre.util.scenereaders.entityhandlers.PointlightEntityHandler;
import yaphyre.util.scenereaders.entityhandlers.SphereEntityHandler;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * XML document reader. Creates a {@link Scene} representation by reading an XML
 * document in multiple stages. Since various instances depend on each other, it
 * is vital to parse the document in more than one go.
 * 
 * @version $Revision: 37 $
 * 
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
public class MultiStageXMLSceneReader implements SceneReaders<InputStream> {

  private static final Logger LOGGER = LoggerFactory.getLogger(MultiStageXMLSceneReader.class);

  private static final List<EntityHandler<IdentifiableObject<Material>>> MATERIAL_HANDLERS;

  private static final List<EntityHandler<IdentifiableObject<Shader>>> SHADER_HANDLERS;

  private static final List<EntityHandler<IdentifiableObject<Shape>>> PRIMITIVE_HANDLERS;

  private static final List<EntityHandler<IdentifiableObject<Lightsource>>> LIGHT_HANDLERS;

  static {
    MATERIAL_HANDLERS = initMaterialHandlers();
    SHADER_HANDLERS = initShaderHandlers();
    PRIMITIVE_HANDLERS = initPrimitiveHandlers();
    LIGHT_HANDLERS = initLightHandlers();
  }

  private static List<EntityHandler<IdentifiableObject<Material>>> initMaterialHandlers() {
    List<EntityHandler<IdentifiableObject<Material>>> result = Lists.newArrayList();
    result.add(new MaterialEntityHandler());
    return result;
  }

  private static List<EntityHandler<IdentifiableObject<Shader>>> initShaderHandlers() {
    List<EntityHandler<IdentifiableObject<Shader>>> result = Lists.newArrayList();
    return result;
  }

  private static List<EntityHandler<IdentifiableObject<Shape>>> initPrimitiveHandlers() {
    List<EntityHandler<IdentifiableObject<Shape>>> result = Lists.newArrayList();
    result.add(new SphereEntityHandler());
    return result;
  }

  private static List<EntityHandler<IdentifiableObject<Lightsource>>> initLightHandlers() {
    List<EntityHandler<IdentifiableObject<Lightsource>>> result = Lists.newArrayList();
    result.add(new PointlightEntityHandler());
    return result;
  }

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
        this.readDocumentStage(stage, result, xmlDocument);
      }

    } catch (Throwable throwable) {
      Throwables.propagate(throwable);
    }

    LOGGER.info("Scene read. Result = {}", result);
    return result;
  }

  private void readDocumentStage(ReaderStage stage, Scene scene, Match document) {
    LOGGER.debug("+ stage: {}", stage);
    switch (stage) {
    case Init:
      break;
    case PreProcess:
      break;
    case Material:
      this.handleStageMaterial(scene, document);
      break;
    case Shader:
      this.handleStageShader(scene, document);
      break;
    case Primitive:
      this.handleStagePrimitive(scene, document);
      break;
    case Complex:
      break;
    case Light:
      this.handleStageLight(scene, document);
      break;
    case Camera:
      break;
    case PostProcess:
      break;
    case Finish:
      break;
    default:
      LOGGER.warn("Unknown parser stage: {}", stage);
      break;
    }
  }

  private void handleStageMaterial(Scene scene, Match document) {
    for (EntityHandler<IdentifiableObject<Material>> materialHandler : MATERIAL_HANDLERS) {
      Match materials = document.xpath(materialHandler.getXPath());
      for (Match material : materials.each()) {
        IdentifiableObject<Material> decodedMaterial = materialHandler.decodeEntity(material, this.knownMaterials, this.knownShaders, this.knownShapes);
        this.knownMaterials.put(decodedMaterial.getId(), decodedMaterial);
      }
    }
  }

  private void handleStageShader(Scene scene, Match document) {
    // TODO Auto-generated method stub

  }

  private void handleStagePrimitive(Scene scene, Match document) {
    // TODO Auto-generated method stub

  }

  private void handleStageLight(Scene scene, Match document) {
    for (EntityHandler<IdentifiableObject<Lightsource>> lightHandler : LIGHT_HANDLERS) {
      Match lights = document.xpath(lightHandler.getXPath());
      for (Match light : lights.each()) {
        scene.addLightsource(lightHandler.decodeEntity(light, this.knownMaterials, this.knownShaders, this.knownShapes).getObject());
      }
    }
  }

  private static enum ReaderStage {
    Init, PreProcess, Material, Shader, Primitive, Complex, Light, Camera, PostProcess, Finish;
  }

}
