package com.example;

import java.util.concurrent.CountDownLatch;

public class CloudServiceInitializer {
    private static final int TASKS = 5;
    private final CountDownLatch latch = new CountDownLatch(TASKS);

    public void initializeServices() {
        // Simulating initialization of three separate services
        for (int i = 0; i < TASKS; i++) {
            new Thread(new ServiceInitializer(i, latch)).start();
        }

        try {
            // Wait for all services to be initialized
            latch.await();
            System.out.println("All services initialized. System is ready to accept requests.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static class ServiceInitializer implements Runnable {
        private final int serviceId;
        private final CountDownLatch latch;

        ServiceInitializer(int serviceId, CountDownLatch latch) {
            this.serviceId = serviceId;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                // Simulate service initialization with sleep
                Thread.sleep((long) (Math.random() * 1000));
                System.out.println("Service " + serviceId + " initialized.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                latch.countDown();
            }
        }
    }

    public static void main(String[] args) {
        new CloudServiceInitializer().initializeServices();
        DataAccessCoordinator coordinator = new DataAccessCoordinator(5);
        // Simulate multiple services accessing data concurrently
        for (int i = 0; i < 10; i++) {
            new Thread(coordinator::accessData,
                    "Service-" + i).start();
        }
    }
}
