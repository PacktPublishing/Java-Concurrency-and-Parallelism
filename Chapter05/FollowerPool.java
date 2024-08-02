package com.example;

import java.util.ArrayList;
import java.util.List;

public class FollowerPool {
  private List<FollowerThread> followers;

  public FollowerPool(int numberOfFollowers) {
    followers = new ArrayList<>();
    // Initialize the followers collection with FollowerThread instances
    for (int i = 0; i < numberOfFollowers; i++) {
      FollowerThread follower = new FollowerThread();
      followers.add(follower);
      new Thread(follower).start(); // Start each follower thread upon creation
    }
  }

  public FollowerThread getAvailableFollower() {
    for (FollowerThread follower : followers) {
      if (follower.isAvailable()) {
        return follower;
      }
    }
    return null; // Optionally, handle the case where no followers are available
  }

  class FollowerThread implements Runnable {
    private volatile boolean available = true; // Flag to indicate availability
    private Task currentTask;

    public synchronized boolean isAvailable() {
      return available;
    }

    public synchronized void assignTask(Task task) {
      if (available) {
        this.currentTask = task;
        this.available = false;
        notifyAll(); // Notify the thread that a new task has been assigned
      }
    }

    @Override
    public void run() {
      while (true) {
        synchronized (this) {
          while (currentTask == null) {
            try {
              wait(); // Wait until a task is assigned
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              return; // Optionally handle thread interruption
            }
          }

          // Execute the task
          try {
            currentTask.execute();
          } catch (Exception e) {
            e.printStackTrace(); // Exception handling logic can be enhanced
          } finally {
            currentTask = null;
            available = true;
          }
        }
      }
    }
  }
}
