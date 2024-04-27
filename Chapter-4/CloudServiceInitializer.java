package com.example;

import java.util.concurrent.CountDownLatch;

public class CloudServiceInitializer {
    private static final int TOTAL_SERVICES = 3;
    private final CountDownLatch latch = new CountDownLatch(TOTAL_SERVICES);

    public CloudServiceInitializer() {
        // Initialization tasks for three separate services
        for (int i = 0; i < TOTAL_SERVICES; i++) {
            new Thread(new ServiceInitializer(i, latch)).start();
        }
    }

    public void awaitServicesInitialization() throws InterruptedException {
        // Wait for all services to be initialized
        latch.await();
        System.out.println("All services initialized. System is ready to accept requests.");
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
                // Simulate service initialization with varying time delays
                System.out.println("Initializing service " + serviceId);
                Thread.sleep((long) (Math.random() * 1000) + 500);
                System.out.println("Service " + serviceId + " initialized.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                // Signal that this service has been initialized
                latch.countDown();
            }
        }
    }

    public static void main(String[] args) {
        CloudServiceInitializer initializer = new CloudServiceInitializer();
        try {
            initializer.awaitServicesInitialization();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Service initialization was interrupted.");
        }
    }
}
