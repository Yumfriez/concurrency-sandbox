package com.budaev.synchronization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class Launcher {

	private static final Function<List<ClassSynchronization>, List<Thread>> DEFAULT_OBJECT_SYNC_MAPPER = syncObjects -> syncObjects.stream()
			.map(syncObject -> new Thread(syncObject::execute))
			.collect(Collectors.toList());

	private static final Function<List<FieldSynchronization>, List<Thread>> FIELD_OBJECT_SYNC_MAPPER = syncObjects -> syncObjects.stream()
			.map(syncObject -> {
				List<Thread> threads = new ArrayList<>();
				threads.add(new Thread(syncObject::firstMethod));
				threads.add(new Thread(syncObject::secondMethod));
				return threads;
			})
			.flatMap(List::stream)
			.collect(Collectors.toList());

	public static void main(String[] args) throws InterruptedException {

		Predicate<Integer> classSyncPredicate = activeThreads -> activeThreads == 1;
		executeSynchronization(DEFAULT_OBJECT_SYNC_MAPPER, classSyncPredicate, new ClassSynchronization(), new ClassSynchronization());

		Predicate<Integer> objectSyncPredicate = activeThreads -> activeThreads == 2;
		executeSynchronization(DEFAULT_OBJECT_SYNC_MAPPER, objectSyncPredicate, new ObjectSynchronization(), new ObjectSynchronization());

		FieldSynchronization fieldSynchronization = new FieldSynchronization();
		Predicate<Integer> filedSyncPredicate = activeThreads -> fieldSynchronization.getFirstMethodCounter() == 1
				&& fieldSynchronization.getSecondMethodCounter() == 1 && fieldSynchronization.getCounter() == 2;

		executeSynchronization(FIELD_OBJECT_SYNC_MAPPER, filedSyncPredicate, fieldSynchronization);

	}

	private static <T extends ClassSynchronization> void executeSynchronization(Function<List<T>, List<Thread>> syncObjectMapper,
			Predicate<Integer> concurrentThreadsPredicate, T... syncObjects) throws InterruptedException {

		List<Thread> threads = syncObjectMapper.apply(Arrays.asList(syncObjects));

		threads.forEach(Thread::start);

		TimeUnit.MILLISECONDS.sleep(500);
		if (!concurrentThreadsPredicate.test(ClassSynchronization.activeThreads.size())) {
			throw new RuntimeException();
		}

		for (Thread thread : threads) {
			thread.join();
		}
	}
}
