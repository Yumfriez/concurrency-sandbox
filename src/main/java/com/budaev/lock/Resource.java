package com.budaev.lock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class Resource {

	private volatile boolean closed = false;
	private String message;
	private AtomicInteger activeWriters = new AtomicInteger(0);

	public Resource(String message) {
		this.message = message;
	}

	public boolean isClosed() {
		return closed;
	}

	public String getMessage() {
		return message;
	}

	public void appendMessage(String message) {
		if (!closed) {
			this.message += message;
		}
	}

	public synchronized void close() {
		if (!closed) {
			closed = true;
		}
	}

	public int getActiveWriters() {
		return activeWriters.get();
	}

	public void addWriter() {
		this.activeWriters.incrementAndGet();
	}

	public void removeWriter() {
		this.activeWriters.decrementAndGet();
	}
}
