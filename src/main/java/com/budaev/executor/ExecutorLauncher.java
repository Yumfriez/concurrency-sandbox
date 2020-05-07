package com.budaev.executor;

import java.util.concurrent.*;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 *
 * <a href="https://riptutorial.com/java/example/20199/use-cases-for-different-types-of-executorservice"></a>
 */
public class ExecutorLauncher {

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

		execute(threadPoolExecutor);
		execute(fixedThreadPool);
		execute(singleThreadExecutor);

		ExecutorService executorService = Executors.newFixedThreadPool(2);
		CompletableFuture.supplyAsync(() -> {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return "Supplier result";
		}, executorService).thenAccept(System.out::println);

		executorService.shutdown();

	}

	private static void execute(ExecutorService executorService) throws ExecutionException, InterruptedException {

		executorService.execute(() -> {
			try {
				TimeUnit.SECONDS.sleep(1);
				System.out.println(executorService.getClass().getName() + " Executed task");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		Future<String> submit = executorService.submit(() -> {
			System.out.println(executorService.getClass().getName() + " Submitted task");
			return "OK";
		});

		System.out.println(submit.get());

		executorService.shutdown();
	}
}
