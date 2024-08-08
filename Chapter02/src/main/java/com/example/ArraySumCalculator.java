package com.example;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ArraySumCalculator extends RecursiveTask<Long> {
    private static final int THRESHOLD = 1000;
    private final int[] array;
    private final int start;
    private final int end;

    public ArraySumCalculator(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD) {
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        } else {
            int mid = (start + end) / 2;
            ArraySumCalculator leftTask = new ArraySumCalculator(array, start, mid);
            ArraySumCalculator rightTask = new ArraySumCalculator(array, mid, end);

            leftTask.fork();
            long rightResult = rightTask.compute();
            long leftResult = leftTask.join();

            return leftResult + rightResult;
        }
    }

    public static void main(String[] args) {
        int[] largeArray = new int[1000000];
        for (int i = 0; i < largeArray.length; i++) {
            largeArray[i] = i + 1;
        }

        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        ArraySumCalculator calculator = new ArraySumCalculator(largeArray, 0, largeArray.length);
        long sum = forkJoinPool.invoke(calculator);

        System.out.println("Sum: " + sum);
    }
}