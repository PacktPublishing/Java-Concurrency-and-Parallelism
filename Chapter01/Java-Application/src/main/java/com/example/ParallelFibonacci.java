package com.example;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelFibonacci extends RecursiveAction {
    private static final long THRESHOLD = 10;
    private final long n;

    public ParallelFibonacci(long n) {
        this.n = n;
    }

    @Override
    protected void compute() {
        if (n <= THRESHOLD) {
            // Compute Fibonacci number sequentially
            int fib = fibonacci(n);
            System.out.println("Fibonacci(" + n + ") = " + fib);
        } else {
            // Split the task into subtasks
            ParallelFibonacci leftTask = new ParallelFibonacci(n - 1);
            ParallelFibonacci rightTask = new ParallelFibonacci(n - 2);

            // Fork the subtasks for parallel execution
            leftTask.fork();
            rightTask.fork();

            // Join the results
            leftTask.join();
            rightTask.join();
        }
    }

    public static int fibonacci(long n) {
        if (n <= 1)
            return (int) n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    public static void main(String[] args) {
        long n = 40;
        ForkJoinPool pool = new ForkJoinPool();
        ParallelFibonacci task = new ParallelFibonacci(n);
        pool.invoke(task);
    }
}
