package com.example;

import java.util.stream.IntStream;

public class ParallelKitchen {
    public static void main(String[] args) {
        IntStream.range(0, 10).parallel().forEach(i -> {
            System.out.println("Cooking dish #" + i + " in parallel...");
            // Simulate task
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
