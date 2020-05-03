package com.budaev.synchronization;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class ClassSynchronization {

	public static final Set<Thread> activeThreads = new HashSet<>();

	protected Integer counter = 0;

	public void execute() {
		synchronized (ClassSynchronization.class) {
			executeSynchronizedMethod();
		}
	}

	protected void executeSynchronizedMethod() {
		Thread currentThread = Thread.currentThread();
		activeThreads.add(currentThread);
		counter++;

		System.out.println(currentThread.getName() + " incremented");
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		activeThreads.remove(currentThread);
	}
}
