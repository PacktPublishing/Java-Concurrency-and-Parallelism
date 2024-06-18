package com.example;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class App {

    private static final int initialPoolSize = 10;
    private static final int monitoringInterval = 5; // in seconds

    public static void main(String[] args) {
        // Initialize ExecutorService with a fixed thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(initialPoolSize);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule load monitoring and adjustment task
        scheduler.scheduleAtFixedRate(() -> {
            int currentLoad = getCurrentLoad();
            adjustThreadPoolSize((ThreadPoolExecutor) executorService, currentLoad);
        }, 0, monitoringInterval, TimeUnit.SECONDS);

        // Simulating task submission
        for (int i = 0; i < 100; i++) {
            CompletableFuture.runAsync(() -> performTask(), executorService);
        }
    }

    // Simulate getting current load
    private static int getCurrentLoad() {
        return (int) (Math.random() * 100);
    }

    // Adjust thread pool size based on load
    private static void adjustThreadPoolSize(ThreadPoolExecutor executorService, int load) {
        int newPoolSize = calculateOptimalPoolSize(load);
        executorService.setCorePoolSize(newPoolSize);
        executorService.setMaximumPoolSize(newPoolSize);
    }

    // Calculate optimal pool size
    private static int calculateOptimalPoolSize(int load) {
        return Math.max(1, load / 10);
    }

    // Simulate performing a task
    private static void performTask() {
        try {
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
