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

import org.joox.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yaphyre.core.Lightsource;
import yaphyre.core.SceneReaders;
import yaphyre.raytracer.Scene;
import yaphyre.util.scenereaders.entityhandlers.EntityHandler;
import yaphyre.util.scenereaders.entityhandlers.IdentifiableObject;
import yaphyre.util.scenereaders.entityhandlers.PointlightEntityHandler;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

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

  private static final List<EntityHandler<IdentifiableObject<Lightsource>>> LIGHT_HANDLERS;

  static {
    LIGHT_HANDLERS = Lists.newArrayList();
    LIGHT_HANDLERS.add(((EntityHandler<IdentifiableObject<Lightsource>>)new PointlightEntityHandler()));
  }

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
        readDocumentStage(stage, result, xmlDocument);
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
      break;
    case Shader:
      break;
    case Primitive:
      break;
    case Complex:
      break;
    case Light:
      handleStageLight(scene, document);
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

  private void handleStageLight(Scene scene, Match document) {
    for(EntityHandler<IdentifiableObject<Lightsource>> lightHandler : LIGHT_HANDLERS) {
      Match lights = document.xpath(lightHandler.getXPath());
      for(Match light : lights.each()) {
        scene.addLightsource(lightHandler.decodeEnity(light).getObject());
      }
    }
  }

  public static enum ReaderStage {
    Init, PreProcess, Material, Shader, Primitive, Complex, Light, Camera, PostProcess, Finish;
  }

}
