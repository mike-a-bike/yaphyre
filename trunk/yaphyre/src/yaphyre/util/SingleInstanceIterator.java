package yaphyre.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 */
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
