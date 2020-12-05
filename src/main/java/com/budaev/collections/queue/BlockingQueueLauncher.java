package com.budaev.collections.queue;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class BlockingQueueLauncher {

	public static void main(String[] args) throws InterruptedException {

		new LinkedBlockingDeque<>();

		execute(new LinkedBlockingQueue<>(3));
		System.out.println();
		execute(new ArrayBlockingQueue<>(3, true));
		System.out.println();
		execute(new LinkedBlockingDeque<>(3));
	}

	private static <T extends BlockingQueue<Integer>> void execute(T numbers) throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(3);

		IntStream.range(0, 2).forEach(i -> executor.submit(() -> {
			IntStream.range(0, 5).boxed().forEach(number -> {
				if (numbers.offer(number)) {
					System.out.println(Thread.currentThread().getName() + " Offer: " + number);
				}
			});
			IntStream.range(0, 5).boxed().forEach(number -> {
				try {
					numbers.put(number);
					System.out.println(Thread.currentThread().getName() + " Put: " + number);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			});
		}));

		executor.submit(() -> {
			int counter = 0;
			boolean shouldPoll = true;
			while (shouldPoll) {
				try {
					final Integer number = numbers.poll(1, TimeUnit.SECONDS);
					//					final Integer number = numbers.take(); -- for the new element awaiting
					if (!Objects.isNull(number)) {
						counter++;
					} else {
						shouldPoll = false;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Total items: " + counter);
		});

		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.MINUTES);
	}
}
