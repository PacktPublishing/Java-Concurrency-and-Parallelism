package com.example;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AutoScalingExample {

    private static final int initialPoolSize = 10;
    private static final int monitoringInterval = 5; // in seconds

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(initialPoolSize);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            int currentLoad = getCurrentLoad();
            adjustThreadPoolSize((ThreadPoolExecutor) executorService, currentLoad);
        }, 0, monitoringInterval, TimeUnit.SECONDS);

        // Simulating task submission
        for (int i = 0; i < 100; i++) {
            CompletableFuture.runAsync(() -> performTask(), executorService);
        }
    }

    private static int getCurrentLoad() {
        // Simulate load measurement
        return (int) (Math.random() * 100);
    }

    private static void adjustThreadPoolSize(ThreadPoolExecutor executorService, int load) {
        int newPoolSize = calculateOptimalPoolSize(load);
        executorService.setCorePoolSize(newPoolSize);
        executorService.setMaximumPoolSize(newPoolSize);
    }

    private static int calculateOptimalPoolSize(int load) {
        // Simple heuristic: 1 thread per 10 units of load
        return Math.max(1, load / 10);
    }

    private static void performTask() {
        // Simulate task work
        try {
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
