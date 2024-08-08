package com.example;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelQuickSort extends RecursiveAction {
    private int[] array;
    private int low;
    private int high;

    public ParallelQuickSort(int[] array, int low, int high) {
        this.array = array;
        this.low = low;
        this.high = high;
    }

    @Override
    protected void compute() {
        if (low < high) {
            int pivotIndex = partition(array, low, high);
            invokeAll(new ParallelQuickSort(array, low, pivotIndex - 1),
                    new ParallelQuickSort(array, pivotIndex + 1, high));
        }
    }

    private int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (array[j] < pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        return i + 1;
    }

    public static void main(String[] args) {
        int[] array = { 9, 3, 1, 5, 13, 12 };
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new ParallelQuickSort(array, 0, array.length - 1));
        System.out.println("Sorted Array: " + Arrays.toString(array));
    }
}
