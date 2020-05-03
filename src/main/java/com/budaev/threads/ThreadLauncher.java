package com.budaev.threads;

import java.time.LocalDateTime;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class ThreadLauncher {

	public static void main(String[] args) throws InterruptedException {

		Thread cur = Thread.currentThread();

		ThreadExtension threadExtension = new ThreadExtension(cur, LocalDateTime.now());
		Thread threadWithRunnable = new Thread(new RunnableImpl(cur, LocalDateTime.now()));

		threadExtension.start();
		threadWithRunnable.start();

		threadExtension.join();
		threadWithRunnable.join();
	}
}
