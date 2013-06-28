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

import com.google.common.base.Objects;

/**
 * Simple wrapper class holding an instance with its associated id. The id may be null.
 *
 * @param <T> The type of the object held by a concrete instance.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 37 $
 */
public class IdentifiableObject<T> {

	private final T object;

	private final String id;

	public IdentifiableObject(String id, T object) {
		this.object = object;
		this.id = id;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(IdentifiableObject.class).add("id", id).add("object", object).toString();
	}

	public String getId() {
		return id;
	}

	public T getObject() {
		return object;
	}

}
