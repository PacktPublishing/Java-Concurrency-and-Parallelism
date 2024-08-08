package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MicroserviceExample {
    private static final int NUM_THREADS = 10;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                // Simulate processing a request
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Request processed by " + Thread.currentThread().getName());
            });
        }
        executorService.shutdown();
    }
}
