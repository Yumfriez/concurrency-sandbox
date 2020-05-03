package com.budaev.threads;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class ThreadExtension extends Thread {

	private final Thread startedBy;
	private final LocalDateTime startedAt;

	public ThreadExtension(Thread startedBy, LocalDateTime startedAt) {
		this.startedBy = startedBy;
		this.startedAt = startedAt;
	}

	@Override
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(2);
			System.out.println(this.getClass().getName() + " Started by " + startedBy.getName() + " at " + startedAt);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
