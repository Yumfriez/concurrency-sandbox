package com.budaev.java8;

import com.budaev.lock.Resource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class StampedLockLauncher {

	public static void main(String[] args) throws InterruptedException {

		StampedLock stampedLock = new StampedLock();
		Resource resource = new Resource("Text");

		ExecutorService executorService = Executors.newFixedThreadPool(4);

		ReaderRunnable firstReader = new ReaderRunnable(stampedLock, resource);
		ReaderRunnable secondReader = new ReaderRunnable(stampedLock, resource);

		WriterRunnable firstWriter = new WriterRunnable(stampedLock, resource);
		WriterRunnable secondWriter = new WriterRunnable(stampedLock, resource);

		Stream.of(firstReader, secondReader, firstWriter, secondWriter).forEach(executorService::submit);

		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

		singleThreadExecutor.execute(() -> {
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				resource.close();
			}
		});

		Thread activeWritersValidator = new Thread(() -> {
			while (!resource.isClosed()) {
				if (resource.getActiveWriters() > 1) {
					throw new RuntimeException("More than 1 active writers detected");
				}
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		activeWritersValidator.start();

		executorService.shutdown();
		singleThreadExecutor.shutdown();

		executorService.awaitTermination(30, TimeUnit.SECONDS);
		singleThreadExecutor.awaitTermination(30, TimeUnit.SECONDS);

		if (resource.isClosed()) {
			System.out.println(resource.getMessage());
		} else {
			throw new RuntimeException("Resource is not closed");
		}
	}

	public static class WriterRunnable implements Runnable {

		private final StampedLock stampedLock;
		private final Resource resource;

		public WriterRunnable(StampedLock stampedLock, Resource resource) {
			this.stampedLock = stampedLock;
			this.resource = resource;
		}

		@Override
		public void run() {
			while (!resource.isClosed()) {
				final long stamp = stampedLock.writeLock();
				try {
					resource.addWriter();
					resource.appendMessage(" {" + Thread.currentThread().getName() + "} ");
					resource.removeWriter();
				} finally {
					stampedLock.unlockWrite(stamp);
				}
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static class ReaderRunnable implements Runnable {

		private final StampedLock stampedLock;
		private final Resource resource;

		public ReaderRunnable(StampedLock stampedLock, Resource resource) {
			this.stampedLock = stampedLock;
			this.resource = resource;
		}

		@Override
		public void run() {
			while (!resource.isClosed()) {
				final long stamp = stampedLock.readLock();
				try {
					System.out.println(Thread.currentThread().getName() + " " + resource.getMessage());
					TimeUnit.MILLISECONDS.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					stampedLock.unlockRead(stamp);
				}

			}
		}
	}
}
