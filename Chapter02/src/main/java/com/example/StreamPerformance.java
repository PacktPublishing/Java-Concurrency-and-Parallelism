package com.example;

import java.util.Arrays;
import java.util.Random;

public class StreamPerformance {
    private static final int SIZE = 100_000_000;
    private static final int[] NUMBERS = new int[SIZE];

    static {
        Random random = new Random();
        for (int i = 0; i < SIZE; i++) {
            NUMBERS[i] = random.nextInt(100);
        }
    }

    public static void main(String[] args) {
        long sequentialStartTime = System.currentTimeMillis();
        Arrays.stream(NUMBERS).sum();
        long sequentialEndTime = System.currentTimeMillis();

        long parallelStartTime = System.currentTimeMillis();
        Arrays.stream(NUMBERS).parallel().sum();
        long parallelEndTime = System.currentTimeMillis();

        System.out.println("Sequential Time: " + (sequentialEndTime - sequentialStartTime) + "ms");
        System.out.println("Parallel Time: " + (parallelEndTime - parallelStartTime) + "ms");
    }
}