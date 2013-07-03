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

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SingleInstanceIteratorTest {

	private static final String TEST_STRING = "Test-String";

	@Test
	public void testHasNext() throws Exception {
		SingleInstanceIterator<String> iterator = createTestInstance(TEST_STRING);
		assertTrue(iterator.hasNext());
		iterator.next();
		assertFalse(iterator.hasNext());
	}

	@Test(expected = NoSuchElementException.class)
	public void testNext() throws Exception {
		SingleInstanceIterator<String> iterator = createTestInstance(TEST_STRING);
		assertEquals(TEST_STRING, iterator.next());
		iterator.next();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRemove() throws Exception {
		SingleInstanceIterator<String> iterator = createTestInstance(TEST_STRING);
		iterator.remove();
	}

	private <T> SingleInstanceIterator<T> createTestInstance(T instance) {
		return new SingleInstanceIterator<T>(instance);
	}
}
