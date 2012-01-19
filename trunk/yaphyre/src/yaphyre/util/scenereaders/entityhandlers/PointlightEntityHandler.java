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
package yaphyre.util.scenereaders.entityhandlers;

import java.util.Collections;
import java.util.List;

import org.joox.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yaphyre.core.Lightsource;
import yaphyre.geometry.Transformation;
import yaphyre.lights.Pointlight;
import yaphyre.util.Color;
import yaphyre.util.scenereaders.utils.HelperFactory;

import com.google.common.base.Preconditions;

/**
 * This handler reads a point light source from the given match.
 *
 * @version $Revision: 37 $
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 *
 */
public class PointlightEntityHandler extends EntityHandler<IdentifiableObject<Lightsource>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(PointlightEntityHandler.class);

  @Override
  public IdentifiableObject<Lightsource> decodeEntity(Match entityMatch) {

    LOGGER.trace("enter decodeEntity: {}", entityMatch);

    Preconditions.checkArgument(entityMatch.tag().equals("light"));
    Preconditions.checkArgument(entityMatch.attr("type").equals("point"));

    String id = entityMatch.id();
    Color lightColor = HelperFactory.getColorHelper().decodeEntity(entityMatch.child("color"));
    double intensity = entityMatch.child("intensity").attr("value", Double.class);
    Transformation transformation = super.decodeTransform(entityMatch);

    transformation.getClass();

    IdentifiableObject<Lightsource> result = new IdentifiableObject<Lightsource>(id, new Pointlight(transformation, lightColor, intensity));

    LOGGER.trace("exiting decodeEntity: {}", result);

    return result;
  }

  @Override
  public String getXPath() {
    return "//light[@type = \"point\"]";
  }

  @Override
  public List<String> getValidParents() {
    return Collections.emptyList();
  }

}
