package com.budaev.threads;

import java.time.LocalDateTime;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class RunnableImpl implements Runnable {

	private final Thread startedBy;
	private final LocalDateTime startedAt;

	public RunnableImpl(Thread startedBy, LocalDateTime startedAt) {
		this.startedBy = startedBy;
		this.startedAt = startedAt;
	}

	@Override
	public void run() {
		System.out.println(this.getClass().getName() + " Started by " + startedBy.getName() + " at " + startedAt);
	}
}
