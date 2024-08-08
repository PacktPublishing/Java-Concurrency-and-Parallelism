package com.example;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OrderProcessingSystem {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final ConcurrentLinkedQueue<Order> orderQueue = new ConcurrentLinkedQueue<>();
    private final CopyOnWriteArrayList<Order> processedOrders = new CopyOnWriteArrayList<>();
    private final ConcurrentHashMap<Integer, String> orderStatus = new ConcurrentHashMap<>();
    private final Lock paymentLock = new ReentrantLock();
    private final Semaphore validationSemaphore = new Semaphore(5);
    private final AtomicInteger processedCount = new AtomicInteger(0);

    public void startProcessing() {
        while (!orderQueue.isEmpty()) {
            Order order = orderQueue.poll();
            executorService.submit(() -> processOrder(order));
        }
        executorService.shutdown(); // Initiates an orderly shutdown
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow(); // Forcefully shutdown if tasks are not finished
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void processOrder(Order order) {
        try {
            validateOrder(order);
            paymentLock.lock();
            try {
                processPayment(order);
            } finally {
                paymentLock.unlock();
            }
            shipOrder(order);
            processedOrders.add(order);
            processedCount.incrementAndGet();
            orderStatus.put(order.getId(), "Completed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void validateOrder(Order order) throws InterruptedException {
        validationSemaphore.acquire();
        try {
            Thread.sleep(100);
        } finally {
            validationSemaphore.release();
        }
    }

    private void processPayment(Order order) {
        System.out.println("Payment Processed for Order " + order.getId());
    }

    private void shipOrder(Order order) {
        System.out.println("Shipped Order " + order.getId());
    }

    public void placeOrder(Order order) {

        orderQueue.add(order);
        orderStatus.put(order.getId(), "Received");
        System.out.println("Order " + order.getId() + " placed.");
    }

    public static void main(String[] args) {

        OrderProcessingSystem system = new OrderProcessingSystem();

        for (int i = 0; i < 20; i++) {
            system.placeOrder(new Order(i));
        }
        system.startProcessing();
        System.out.println("All Orders Processed!");
    }

    static class Order {
        private final int id;

        public Order(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

}
