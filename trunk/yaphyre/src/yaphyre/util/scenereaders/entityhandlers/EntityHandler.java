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

import java.util.List;

import org.joox.Match;

import yaphyre.geometry.Transformation;
import yaphyre.util.scenereaders.utils.HelperFactory;

/**
 * Abstract super class for all EnityHandler implementations. Each
 * implementation handles one or more entities. Each {@link EntityHandler}
 * supply an XPath expression which is used to select the corresponding
 * instances from the whole document.
 *
 * @version $Revision: 37 $
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 *
 * @param <T>
 *          The concrete type created by the implementing handler.
 */
public abstract class EntityHandler<T extends IdentifiableObject<?>> {

  public abstract T decodeEntity(Match entityMatch);

  public abstract String getXPath();

  public abstract List<String> getValidParents();

  /**
   * Decodes a transformation from the given {@link Match}. Each element
   * containing a transformation uses the tag <i>transform</i> to mark it.
   *
   * @param entityMatch
   *          The {@link Match} representing the surrounding entity.
   *
   * @return A {@link Transformation} instance if the entity contains a
   *         transformation, <code>null</code> otherwise.
   */
  protected Transformation decodeTransform(Match entityMatch) {
    Transformation transformation = null;
    Match transformMatch = entityMatch.child("transform");
    if (!transformMatch.isEmpty()) {
      transformation = HelperFactory.getTransformationHelper().decodeEntity(transformMatch);
    }
    return transformation;
  }

}
