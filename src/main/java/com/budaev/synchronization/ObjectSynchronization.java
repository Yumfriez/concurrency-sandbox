package com.budaev.synchronization;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class ObjectSynchronization extends ClassSynchronization {

	@Override
	public void execute() {
		synchronized (this) {
			executeSynchronizedMethod();
		}
	}
}
