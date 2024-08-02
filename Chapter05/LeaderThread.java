package com.example;

public class LeaderThread implements Runnable {
    private final TaskQueue taskQueue;
    private final FollowerThread[] followers;

    public LeaderThread(TaskQueue taskQueue, FollowerThread[] followers) {
        this.taskQueue = taskQueue;
        this.followers = followers;
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            try {
                Task task = taskQueue.getTask();
                FollowerThread availableFollower = findAvailableFollower();
                if (availableFollower != null) {
                    availableFollower.assignTask(task);
                }
            } catch (InterruptedException e) {
                running = false; // Stop the thread gracefully
            } finally {
                // Optional cleanup or leader election logic
            }
        }
    }

    private synchronized FollowerThread findAvailableFollower() {
        while (true) {
            for (FollowerThread follower : followers) {
                if (follower.isAvailable()) {
                    return follower;
                }
            }
            try {
                wait(); // Wait until a follower becomes available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
    }

    public synchronized void notifyFollowerAvailable() {
        notifyAll(); // Called by followers when they become available
    }
}
