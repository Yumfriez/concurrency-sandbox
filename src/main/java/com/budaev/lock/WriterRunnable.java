package com.budaev.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class WriterRunnable implements Runnable {

	private final ReadWriteLock readWriteLock;
	private final Resource resource;

	public WriterRunnable(ReadWriteLock readWriteLock, Resource resource) {
		this.readWriteLock = readWriteLock;
		this.resource = resource;
	}

	@Override
	public void run() {
		while (!resource.isClosed()) {
			readWriteLock.writeLock().lock();
			try {
				resource.addWriter();
				resource.appendMessage(" {" + Thread.currentThread().getName() + "} ");
				resource.removeWriter();
			} finally {
				readWriteLock.writeLock().unlock();
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
