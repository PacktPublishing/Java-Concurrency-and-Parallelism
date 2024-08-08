package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class PrepVeggiesDemo {
    static interface KitchenTask {
        int getTaskId();

        String performTask();
    }

    static class PrepVeggiesTask implements KitchenTask {
        protected int taskId;

        public PrepVeggiesTask(int taskId) {
            this.taskId = taskId;
        }

        public String performTask() {
            String message = String.format("[Task-%d] Prepped Veggies", this.taskId);
            System.out.println(message);
            return message;
        }

        public int getTaskId() {
            return this.taskId;
        }
    }

    static class CookVeggiesTask implements KitchenTask {
        protected int taskId;

        public CookVeggiesTask(int taskId) {
            this.taskId = taskId;
        }

        public String performTask() {
            String message = String.format("[Task-%d] Cooked Veggies", this.taskId);
            System.out.println(message);
            return message;
        }

        public int getTaskId() {
            return this.taskId;
        }
    }

    static class ChefTask extends RecursiveTask<String> {
        protected KitchenTask task;
        protected List<ChefTask> dependencies;

        public ChefTask(KitchenTask task, List<ChefTask> dependencies) {
            this.task = task;
            this.dependencies = dependencies;
        }

        // Method to wait for dependencies to complete
        protected void awaitDependencies() {
            if (dependencies == null || dependencies.isEmpty())
                return;
            ChefTask.invokeAll(dependencies);
        }

        @Override
        protected String compute() {
            awaitDependencies(); // Ensure all prerequisites are met
            return task.performTask(); // Carry out the specific task
        }
    }

    public static void main(String[] args) {
        // Example dataset
        int DEPENDENCY_SIZE = 10;
        ArrayList<ChefTask> dependencies = new ArrayList<ChefTask>();
        for (int i = 0; i < DEPENDENCY_SIZE; i++) {
            dependencies.add(new ChefTask(new PrepVeggiesTask(i), null));
        }
        ForkJoinPool pool = new ForkJoinPool();
        ChefTask cookTask = new ChefTask(new CookVeggiesTask(100), dependencies);
        pool.invoke(cookTask);
        pool.shutdown(); // Stop accepting new tasks
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // Wait for existing tasks to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}