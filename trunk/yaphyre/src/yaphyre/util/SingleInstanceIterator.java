/*
 * Copyright 2013 Michael Bieri
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

package yaphyre.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SingleInstanceIterator<T> implements Iterator<T> {

	private final T instance;
	private boolean hasNext;

	public SingleInstanceIterator(final T instance) {
		this.instance = instance;
		hasNext = true;
	}

	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@Override
	public T next() {
		if (!hasNext) {
			throw new NoSuchElementException("no more elements available for this iterator");
		}
		hasNext = false;
		return instance;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("read-only iterator");
	}
}
