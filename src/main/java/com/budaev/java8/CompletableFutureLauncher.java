package com.budaev.java8;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class CompletableFutureLauncher {

	public static void main(String[] args) {

		ExecutorService executorService = Executors.newFixedThreadPool(3);

		final List<CompletableFuture<String>> completableFutures = IntStream.range(0, 3)
				.mapToObj(it -> getCompletableFuture(executorService))
				.collect(Collectors.toList());

		//Approach for returning parametrized list as a result
		final CompletableFuture<List<String>> listFuture = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]))
				.thenApply(ignoredResult -> completableFutures.stream().map(CompletableFuture::join).collect(Collectors.toList()));

		listFuture.thenAccept(results -> System.out.println(String.join(", ", results)));

		//Approach for returning raw result
		final CompletableFuture<Void> arrayFuture = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));

		listFuture.join();

		//Won't print cause already completed
		arrayFuture.join();

		System.out.println("Completable futures finished");

		executorService.shutdown();

	}

	private static CompletableFuture<String> getCompletableFuture(ExecutorService executorService) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			final String threadName = Thread.currentThread().getName();
			System.out.println(threadName + " Supply async finished");
			return threadName + " result";
		}, executorService).thenApplyAsync(result -> {
			System.out.println(Thread.currentThread().getName() + " Then accept async finished: " + result);
			return "Updated " + result;
		}, executorService).thenApply(x -> x);
	}

}
