package com.example;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcurrentKitchen {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<?> task1 = executor.submit(() -> {
            System.out.println("Chopping vegetables...");
            // Simulate task
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Future<?> task2 = executor.submit(() -> {
            System.out.println("Grilling meat...");
            // Simulate task
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Wait for both tasks to complete
        try {
            task1.get();
            task2.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }
}
