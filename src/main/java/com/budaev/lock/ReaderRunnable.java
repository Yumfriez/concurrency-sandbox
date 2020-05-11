package com.budaev.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class ReaderRunnable implements Runnable {

	private final ReadWriteLock readWriteLock;
	private final Resource resource;

	public ReaderRunnable(ReadWriteLock readWriteLock, Resource resource) {
		this.readWriteLock = readWriteLock;
		this.resource = resource;
	}

	@Override
	public void run() {
		while (!resource.isClosed()) {
			readWriteLock.readLock().lock();
			try {
				System.out.println(Thread.currentThread().getName() + " " + resource.getMessage());
				TimeUnit.MILLISECONDS.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				readWriteLock.readLock().unlock();
			}

		}
	}
}
