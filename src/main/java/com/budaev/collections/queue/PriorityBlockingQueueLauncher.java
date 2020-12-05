package com.budaev.collections.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class PriorityBlockingQueueLauncher {

	public static void main(String[] args) throws InterruptedException {

		List<Integer> integers = new ArrayList<Integer>() {
			{
				add(1);
				add(3);
				add(5);
				add(4);
				add(2);
			}
		};

		PriorityBlockingQueue<Integer> numbers = new PriorityBlockingQueue<>(5);

		System.out.println("Adding to queue");
		numbers.addAll(integers);

		final Thread thread = new Thread(() -> {
			System.out.println("Polling...");
			while (true) {
				try {
					Integer poll = numbers.poll(1, TimeUnit.SECONDS);
					if (Objects.isNull(poll)) {
						return;
					}
					System.out.println("Polled: " + poll);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();

		thread.join();

	}
}
