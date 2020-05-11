package com.budaev.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class ReadWriteLockLauncher {

	public static void main(String[] args) throws InterruptedException {

		ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
		Resource resource = new Resource("Text");

		ExecutorService executorService = Executors.newFixedThreadPool(4);

		ReaderRunnable firstReader = new ReaderRunnable(readWriteLock, resource);
		ReaderRunnable secondReader = new ReaderRunnable(readWriteLock, resource);

		WriterRunnable firstWriter = new WriterRunnable(readWriteLock, resource);
		WriterRunnable secondWriter = new WriterRunnable(readWriteLock, resource);

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

}
