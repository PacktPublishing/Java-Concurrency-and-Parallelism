package com.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class DirectThreadPoolExample {

	public static void main(String[] args) {

		int corePoolSize = 2;
		int maxPoolSize = 4;
		long keepAliveTime = 5000;
		TimeUnit unit = TimeUnit.MILLISECONDS;
		int taskCount = 15; // Make this 4, 10, 12, 14, and finally 15 and observe the output.

		ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(20);

		try (ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, unit,
				workQueue)) {
			IntStream.range(0, taskCount).forEach(
					i -> executor.execute(
							() -> System.out.println(
									String.format("Task %d executed. Pool size = %d. Queue size = %d.", i,
											executor.getPoolSize(), executor.getQueue().size()))));

			executor.shutdown();
			try {
				if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
					executor.shutdownNow();
				}
			} catch (InterruptedException e) {
				executor.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}
	}
}