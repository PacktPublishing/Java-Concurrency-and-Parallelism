package com.example;

import java.util.concurrent.Semaphore;

public class DataAccessCoordinator {
    private final Semaphore semaphore;

    public DataAccessCoordinator(int permits) {
        this.semaphore = new Semaphore(permits);
    }

    public void accessData() {
        try {
            semaphore.acquire();
            // Access shared data resource
            System.out.println("Data accessed by " + Thread.currentThread().getName());
            // Simulate data access
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }

    public static void main(String[] args) {
        DataAccessCoordinator coordinator = new DataAccessCoordinator(5);

        // Simulate multiple services accessing data concurrently
        for (int i = 0; i < 10; i++) {
            new Thread(coordinator::accessData, "Service-" + i).start();
        }
    }
}
