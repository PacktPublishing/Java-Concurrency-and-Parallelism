package com.example;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class StreamOptimizationDemo {
    public static void main(String[] args) {
        // Example data
        List<Integer> data = Collections.unmodifiableList(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        // Custom Thread Pool for parallel streams
        ForkJoinPool customThreadPool = new ForkJoinPool(4); // Customizing the number of threads
        try {
            List<Integer> processedData = customThreadPool.submit(() -> data.parallelStream()
                    .filter(n -> n % 2 == 0) // Filtering even numbers
                    .map(n -> n * n) // Squaring them
                    .collect(Collectors.toList()) // Collecting results
            ).get();

            System.out.println("Processed Data: " + processedData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            customThreadPool.shutdown(); // Always shutdown your thread pool!
        }

        // Using ConcurrentHashMap for better performance in parallel streams
        ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
        data.parallelStream().forEach(n -> map.put(n, n * n));

        System.out.println("ConcurrentHashMap contents: " + map);
    }
}