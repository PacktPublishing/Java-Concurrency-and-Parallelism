package com.example;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class DataProcessor {

    public static void main(String[] args) {
        // Example dataset
        int DATASET_SIZE = 500;
        ArrayList<Integer> data = new ArrayList<>(DATASET_SIZE);

        // Create a ForkJoinPool with the number of available processors
        ForkJoinPool pool = new ForkJoinPool();

        // RecursiveAction for generating a large dataset
        ActionTask actionTask = new ActionTask(data, 0, DATASET_SIZE);
        pool.invoke(actionTask);

        // RecursiveTask for summing the large dataset
        SumTask sumTask = new SumTask(data, 0, DATASET_SIZE);
        int result = pool.invoke(sumTask);
        System.out.println("Total sum: " + result);

        // Shutdown the ForkJoinPool after completing the tasks
        pool.shutdown();
        try {
            // Await termination of the pool
            pool.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Splitting task for parallel execution
    static class SumTask extends RecursiveTask<Integer> {
        private final ArrayList<Integer> data;
        private final int start, end;
        private static final int THRESHOLD = 50;

        SumTask(ArrayList<Integer> data, int start, int end) {
            this.data = data;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            int length = end - start;
            System.out.println(String.format("RecursiveTask.compute() called for %d elements from index %d to %d",
                    length, start, end));

            // Base case: if the data size is small enough, compute the sum sequentially
            if (length <= THRESHOLD) {
                System.out.println(
                        String.format("Calculating sum of %d elements from index %d to %d", length, start, end));
                int sum = 0;
                for (int i = start; i < end; i++) {
                    sum += data.get(i);
                }
                return sum;
            } else {
                // Recursive case: split the task into two subtasks
                int mid = start + (length / 2);
                SumTask left = new SumTask(data, start, mid);
                SumTask right = new SumTask(data, mid, end);

                // Fork the subtasks and wait for them to complete
                left.fork();
                right.fork();

                // Join the results from the subtasks
                return right.join() + left.join();
            }
        }
    }

    static class ActionTask extends RecursiveAction {
        private final ArrayList<Integer> data;
        private final int start, end;
        private static final int THRESHOLD = 50;

        ActionTask(ArrayList<Integer> data, int start, int end) {
            this.data = data;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            int length = end - start;
            System.out.println(String.format("RecursiveAction.compute() called for %d elements from index %d to %d",
                    length, start, end));

            // Base case: if the data size is small enough, perform the action sequentially
            if (length <= THRESHOLD) {
                for (int i = start; i < end; i++) {
                    this.data.add((int) Math.round(Math.random() * 100));
                }
            } else {
                // Recursive case: split the task into two subtasks
                int mid = start + (length / 2);
                ActionTask left = new ActionTask(data, start, mid);
                ActionTask right = new ActionTask(data, mid, end);

                // Fork the subtasks and wait for them to complete
                invokeAll(left, right);
            }
        }
    }
}