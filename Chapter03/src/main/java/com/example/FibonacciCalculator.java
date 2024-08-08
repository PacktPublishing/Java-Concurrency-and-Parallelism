package com.example;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class FibonacciCalculator extends RecursiveTask<Integer> {
    private final int n;

    public FibonacciCalculator(int n) {
        this.n = n;
    }

    @Override
    protected Integer compute() {
        if (n <= 1) {
            return n;
        }
        // Calculate f1 (n-1)th Fibonacci number synchronously
        FibonacciCalculator f1 = new FibonacciCalculator(n - 1);
        int f1Result = f1.compute();

        // Fork f2 (n-2)th Fibonacci number asynchronously
        FibonacciCalculator f2 = new FibonacciCalculator(n - 2);
        f2.fork();

        // Combine results: f2 (asynchronously calculated) + f1 (already calculated)
        return f2.join() + f1Result;
    }

    public static void main(String[] args) {
        @SuppressWarnings("resource")
        ForkJoinPool pool = new ForkJoinPool();
        int position = 20; // Example: Calculate the 20th Fibonacci number
        FibonacciCalculator calculator = new FibonacciCalculator(position);
        int result = pool.invoke(calculator);
        System.out.println("Fibonacci number at position " + position + " is: " + result);
        pool.shutdown();
    }
}
