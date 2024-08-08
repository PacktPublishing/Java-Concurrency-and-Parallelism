package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogProcessor {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4); // Pool with 4 threads

        for (int i = 1; i <= 10; i++) {
            int fileId = i;
            executor.submit(() -> processLogFile(fileId));
        }

        executor.shutdown();
    }

    private static void processLogFile(int fileId) {
        System.out.println("Processing file #" + fileId + " by " + Thread.currentThread().getName());
        // Add file processing logic here
    }
}
