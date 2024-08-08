package com.example;

import java.util.ArrayList;
import java.util.List;

public class ParallelDataProcessing {
    public static void main(String[] args) {
        List<Integer> data = generateData(200000000);

        // Sequential processing
        long start = System.currentTimeMillis();
        int sum = data.stream().mapToInt(Integer::intValue).sum();
        long end = System.currentTimeMillis();
        System.out.println("Sequential sum: " + sum + ", time: " + (end - start) + " ms");

        // Parallel processing
        start = System.currentTimeMillis();
        sum = data.parallelStream().mapToInt(Integer::intValue).sum();
        end = System.currentTimeMillis();
        System.out.println("Parallel sum: " + sum + ", time: " + (end - start) + " ms");
    }

    private static List<Integer> generateData(int size) {
        List<Integer> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add(i);
        }
        return data;
    }
}
