package com.budaev.synchronization;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class FieldSynchronization extends ClassSynchronization {

	private int firstMethodCounter;
	private int secondMethodCounter;

	private final Object firstMethodMonitor = new Object();
	private final Object secondMethodMonitor = new Object();

	public void firstMethod() {
		synchronized (firstMethodMonitor) {
			firstMethodCounter++;
			executeSynchronizedMethod();
		}
	}

	public void secondMethod() {
		synchronized (secondMethodMonitor) {
			secondMethodCounter++;
			executeSynchronizedMethod();
		}
	}

	public int getFirstMethodCounter() {
		return firstMethodCounter;
	}

	public int getSecondMethodCounter() {
		return secondMethodCounter;
	}

    public int getCounter() {
		return counter;
	}
}
