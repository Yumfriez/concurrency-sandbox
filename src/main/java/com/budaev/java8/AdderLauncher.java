package com.budaev.java8;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class AdderLauncher {

	public static void main(String[] args) throws InterruptedException {
		LongAdder counter = new LongAdder();
		ExecutorService executorService = Executors.newFixedThreadPool(8);

		int numberOfThreads = 4;
		int numberOfIncrements = 100;

		IntStream.range(0, numberOfThreads)
				.forEach(t -> executorService.execute(() -> IntStream.range(0, numberOfIncrements).forEach(i -> counter.increment())));

		DoubleAdder doubleAdder = new DoubleAdder();

		IntStream.range(0, numberOfThreads)
				.forEach(t -> executorService.execute(() -> IntStream.range(0, numberOfIncrements).forEach(i -> doubleAdder.add(1.1))));

		executorService.shutdown();
		executorService.awaitTermination(10, TimeUnit.SECONDS);

		if (counter.sumThenReset() != numberOfIncrements * numberOfThreads) {
			throw new RuntimeException("Wrong sum");
		}

		if (counter.sum() != 0) {
			throw new RuntimeException("Sum was not reset");
		}

		System.out.println("Long adders finished");

		final BigDecimal sum = BigDecimal.valueOf(doubleAdder.sumThenReset()).setScale(2, RoundingMode.HALF_UP);
		final BigDecimal expectedSum = BigDecimal.valueOf(numberOfIncrements * numberOfThreads * 1.1).setScale(2, RoundingMode.HALF_UP);
		if (expectedSum.compareTo(sum) != 0) {
			throw new RuntimeException("Wrong sum");
		}

		if (doubleAdder.sum() != 0) {
			throw new RuntimeException("Sum was not reset");
		}

		System.out.println("Double adders finished");
	}
}
