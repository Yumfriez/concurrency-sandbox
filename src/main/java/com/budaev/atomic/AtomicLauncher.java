package com.budaev.atomic;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class AtomicLauncher {

	public static void main(String[] args) {
		runAtomicBoolean();
		runAtomicInteger();
		runAtomicLong();
		runAtomicReference();
	}

	private static void runAtomicBoolean() {

		System.out.println("ATOMIC BOOLEAN");

		AtomicBoolean atomicBoolean = new AtomicBoolean(true);
		boolean value = atomicBoolean.get();
		System.out.println(value);

		atomicBoolean = new AtomicBoolean(false);
		atomicBoolean.set(true);
		System.out.println(atomicBoolean.get());

		atomicBoolean = new AtomicBoolean(true);
		boolean oldValue = atomicBoolean.getAndSet(false);
		System.out.println(oldValue != atomicBoolean.get());

		atomicBoolean = new AtomicBoolean(true);

		boolean wasNewValueSet = atomicBoolean.compareAndSet(true, false);
		System.out.println(wasNewValueSet);
	}

	private static void runAtomicInteger() {

		System.out.println("ATOMIC INTEGER");

		AtomicInteger atomicInteger = new AtomicInteger(123);

		System.out.println(atomicInteger.get());

		atomicInteger = new AtomicInteger(123);

		atomicInteger.set(234);

		System.out.println(atomicInteger);

		atomicInteger = new AtomicInteger(123);

		int expectedValue = 123;
		int newValue = 234;
		atomicInteger.compareAndSet(expectedValue, newValue);

		System.out.println(atomicInteger);

		atomicInteger = new AtomicInteger();

		System.out.println(atomicInteger.getAndAdd(10));
		System.out.println(atomicInteger.addAndGet(10));
	}

	private static void runAtomicLong() {

		System.out.println("ATOMIC LONG");

		AtomicLong atomicLong = new AtomicLong(123);

		System.out.println(atomicLong.get());

		atomicLong = new AtomicLong(123);

		atomicLong.set(234);

		System.out.println(atomicLong);

		atomicLong = new AtomicLong(123);

		int expectedValue = 123;
		int newValue = 234;
		atomicLong.compareAndSet(expectedValue, newValue);

		System.out.println(atomicLong);

		atomicLong = new AtomicLong();

		System.out.println(atomicLong.getAndAdd(10));
		System.out.println(atomicLong.addAndGet(10));
	}

	private static void runAtomicReference() {

		System.out.println("ATOMIC REFERENCE");

		String initialReference = "initial value";

		AtomicReference<String> atomicStringReference = new AtomicReference<>(initialReference);

		String newReference = "new value";
		boolean exchanged = atomicStringReference.compareAndSet(initialReference, newReference);
		System.out.println("First exchange: " + exchanged + " value: " + atomicStringReference.get());

		exchanged = atomicStringReference.compareAndSet(initialReference, newReference);
		System.out.println("Second exchange: " + exchanged + " value: " + atomicStringReference.get());
	}
}
