package com.budaev.collections.map;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class ConcurrentHashMapLauncher {

	private static Random random = new Random(System.currentTimeMillis());

	public static void main(String[] args) throws InterruptedException {
		runPerfTest(new Hashtable<>());
		runPerfTest(Collections.synchronizedMap(new HashMap<>()));
		runPerfTest(new ConcurrentHashMap<>());
		runPerfTest(new ConcurrentSkipListMap<>());
	}

	private static void runPerfTest(Map<Integer, String> map) throws InterruptedException {
		fillMap(map);
		ExecutorService executorService = Executors.newFixedThreadPool(4);
		long startMillis = System.currentTimeMillis();
		for (int i = 0; i < 4; i++) {
			executorService.execute(() -> {
				for (int j = 0; j < 1000000; j++) {
					int randomInt = random.nextInt(1000);
					map.get(randomInt);
					randomInt = random.nextInt(1000);
					map.put(randomInt, String.valueOf(randomInt));
				}
			});
		}
		executorService.shutdown();
		executorService.awaitTermination(1, TimeUnit.MINUTES);
		System.out.println(map.getClass().getSimpleName() + " took " + (System.currentTimeMillis() - startMillis) + " ms");
	}

	private static void fillMap(Map<Integer, String> map) {
		for (int i = 0; i < 1000; i++) {
			map.put(i, String.valueOf(i));
		}
	}

}
