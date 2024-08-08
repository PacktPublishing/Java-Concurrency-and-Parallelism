package com.example;

/**
 * This class demonstrates how to avoid deadlock in a multi-threaded
 * environment.
 * 
 * Note: This implementation prevents deadlock by ensuring that both lambda
 * functions
 * acquire locks in the same order (Item1Lock, then Item2Lock). This approach is
 * known
 * as "lock ordering" or "lock hierarchy".
 * 
 * Key points:
 * 1. Both threads always acquire Item1Lock first, then Item2Lock.
 * 2. Consistent lock ordering prevents the circular wait condition necessary
 * for a deadlock.
 * 3. While this approach prevents deadlock, it may lead to thread contention on
 * Item1Lock.
 * 4. In real-world scenarios, consider using higher-level concurrency utilities
 * or
 * database-specific features for managing concurrent access to resources.
 * 
 * Contrast this with the previous deadlock-prone version where Lambda Function
 * 1 acquired
 * locks in the order (Item1Lock, Item2Lock) while Lambda Function 2 acquired
 * them in the
 * reverse order (Item2Lock, Item1Lock), which could lead to a deadlock.
 */
public class DynamoDBNoDeadlockDemo {

    private static final Object Item1Lock = new Object();
    private static final Object Item2Lock = new Object();

    public static void main(String[] args) {
        Thread lambdaFunction1 = new Thread(() -> {
            synchronized (Item1Lock) {
                System.out.println("Lambda Function 1 locked Item 1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Lambda Function 1 interrupted: " + e.getMessage());
                }
                System.out.println("Lambda Function 1 waiting to lock Item 2");
                synchronized (Item2Lock) {
                    System.out.println("Lambda Function 1 locked Item 1 & 2");
                }
            }
        });

        Thread lambdaFunction2 = new Thread(() -> {
            synchronized (Item1Lock) {
                System.out.println("Lambda Function 2 locked Item 1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Lambda Function 2 interrupted: " + e.getMessage());
                }
                System.out.println("Lambda Function 2 waiting to lock Item 2");
                // Then, attempt to lock Item2
                synchronized (Item2Lock) {
                    System.out.println("Lambda Function 2 locked Item 1 & 2");
                }
            }
        });
        lambdaFunction1.start();
        lambdaFunction2.start();

    }

}