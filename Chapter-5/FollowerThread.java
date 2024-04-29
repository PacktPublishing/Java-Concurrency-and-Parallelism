package com.example;

public class FollowerThread implements Runnable {
    private volatile boolean available = true; // Flag to indicate availability
    private Task currentTask;

    public boolean isAvailable() {
        return available;
    }

    public synchronized void assignTask(Task task) {
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Handle the exception, e.g., log it or rethrow it
                return;
            }
        }
        this.currentTask = task;
        this.available = false;
        notifyAll();
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (this) {
                    while (currentTask == null) {
                        wait();
                    }
                }
                try {
                    currentTask.execute();
                } catch (Exception e) {
                    // Log or handle the exception here instead of just printing stack trace
                    e.printStackTrace();
                    // Consider using logging framework here
                    // You can rethrow the exception as a RuntimeException if needed
                    throw new RuntimeException("Failed to execute task", e);
                }
                synchronized (this) {
                    currentTask = null;
                    available = true;
                    notifyAll(); // Notify any thread waiting to assign a task
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Proper handling of interrupt status
        }
    }
}