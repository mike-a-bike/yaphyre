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

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.joox.Match;
import yaphyre.core.Shader;
import yaphyre.core.Shape;
import yaphyre.geometry.Transformation;
import yaphyre.shaders.Material;
import yaphyre.util.scenereaders.utils.HelperFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Abstract super class for all EnityHandler implementations. Each
 * implementation handles one or more entities. Each {@link EntityHandler}
 * supply an XPath expression which is used to select the corresponding
 * instances from the whole document.
 *
 * @param <T> The concrete type created by the implementing handler.
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 37 $
 */
public abstract class EntityHandler<T extends IdentifiableObject<?>> {

	/**
	 * The method which decodes the entity. This method gets a selected match
	 * based on the returned XPath selector by the {@link #getXPath()} method. The
	 * {@link Match} represents a single instance of the selection result. As
	 * supporting information the method gets all the relevant, already known
	 * instances of {@link Material}, {@link Shader} and {@link Shape}. So xml
	 * references may be replaced by actual object references.
	 *
	 * @param entityMatch    The {@link Match} selected by the XPath expression.
	 * @param knownMaterials All known {@link Material}s.
	 * @param knownShaders   The known {@link Shader}s
	 * @param knownShapes    The decoded {@link Shape}s
	 * @return An {@link IdentifiableObject} of the correct type containing the
	 *         decoded information.
	 */
	public abstract T decodeEntity(Match entityMatch, Map<String, IdentifiableObject<Material>> knownMaterials, Map<String, IdentifiableObject<Shader>> knownShaders, Map<String, IdentifiableObject<Shape>> knownShapes);

	/**
	 * Returns the XPath selector for this handler (see <a
	 * href="http://jquery.com">jQuery</a> for more information).
	 *
	 * @return An XPath selector.
	 */
	public abstract String getXPath();

	/**
	 * If the referenced entities are restricted to specific parent tags, this
	 * method returns a list of these tags. If the usage is not restricted, an
	 * empty {@link List} is returned.
	 *
	 * @return A list with valid parents. If no restriction applies, an empty
	 *         {@link List} is returned.
	 */
	public List<String> getValidParents() {
		return Collections.emptyList();
	}

	/**
	 * Decodes a transformation from the given {@link Match}. Each element
	 * containing a transformation uses the tag <i>transform</i> to mark it.
	 *
	 * @param entityMatch The {@link Match} representing the surrounding entity.
	 * @return A {@link Transformation} instance if the entity contains a
	 *         transformation, <code>null</code> otherwise.
	 */
	protected Transformation decodeTransform(Match entityMatch) {
		Transformation transformation = Transformation.IDENTITY;
		Match transformMatch = entityMatch.child("transform");
		if (!transformMatch.isEmpty()) {
			transformation = HelperFactory.getTransformationHelper().decodeEntity(transformMatch);
		}
		return transformation;
	}

	/**
	 * Reads a numerical value from an attribute. If the attribute is not set, the
	 * null substitution value is returned.
	 *
	 * @param match            The {@link Match} which contains the attribute.
	 * @param attribute        The name of the attribute.
	 * @param nullSubstitution If the attribute is not set, use this value.
	 * @param valueTypeClass   The type of the expected value type (used because generic types
	 *                         are not preserved).
	 * @return A valid value of the requested numerical type.
	 */
	protected <V extends Number> V readNumericAttribute(Match match, String attribute, V nullSubstitution, Class<V> valueTypeClass) {
		Preconditions.checkNotNull(nullSubstitution);
		if (Strings.isNullOrEmpty(match.attr(attribute))) {
			return nullSubstitution;
		}
		return match.attr(attribute, valueTypeClass);
	}

}
