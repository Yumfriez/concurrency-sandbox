package com.budaev.collections;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class CopyOnWriteArrayListLauncher {

	public static void main(String[] args) {
		List<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
		copyOnWriteArrayList.add("first");
		copyOnWriteArrayList.add("second");
		copyOnWriteArrayList.add("third");

		List<String> arrayList = new ArrayList<>(copyOnWriteArrayList);

		final Iterator<String> concurrentIterator = copyOnWriteArrayList.iterator();

		System.out.println(concurrentIterator.next());
		System.out.println("REMOVED: " + copyOnWriteArrayList.remove(1));
		System.out.println(concurrentIterator.next());

		final Iterator<String> iterator = arrayList.iterator();

		System.out.println(iterator.next());
		System.out.println("REMOVED: " + arrayList.remove(1));
		try {

			System.out.println(iterator.next());
		} catch (ConcurrentModificationException ex) {
			System.err.println("Expected exception thrown: " + ex.getClass());
		}

	}
}
