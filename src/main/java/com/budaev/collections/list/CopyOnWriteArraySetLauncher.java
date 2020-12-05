package com.budaev.collections.list;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class CopyOnWriteArraySetLauncher {

	public static void main(String[] args) {
		Set<String> copyOnWriteArraySet = new CopyOnWriteArraySet<>();
		copyOnWriteArraySet.add("first");
		copyOnWriteArraySet.add("second");
		copyOnWriteArraySet.add("third");

		Set<String> hashSet = new HashSet<>(copyOnWriteArraySet);

		final Iterator<String> concurrentIterator = copyOnWriteArraySet.iterator();

		System.out.println(concurrentIterator.next());
		System.out.println("REMOVED: " + copyOnWriteArraySet.remove("second"));
		System.out.println(concurrentIterator.next());

		final Iterator<String> iterator = hashSet.iterator();

		System.out.println(iterator.next());
		System.out.println("REMOVED: " + hashSet.remove("second"));
		try {

			System.out.println(iterator.next());
		} catch (ConcurrentModificationException ex) {
			System.err.println("Expected exception thrown: " + ex.getClass());
		}
	}
}
