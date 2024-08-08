package com.example;

import java.util.List;
import java.util.concurrent.RecursiveTask;

// Abstract class representing a general cooking task with dependencies
abstract class ChefTask<T> extends RecursiveTask<T> {
    protected List<ChefTask<?>> dependencies;

    public ChefTask(List<ChefTask<?>> dependencies) {
        this.dependencies = dependencies;
    }

    // Method to wait for dependencies to complete
    protected void awaitDependencies() {
        for (ChefTask<?> task : dependencies) {
            task.join(); // Wait for each dependent task
        }
    }

    @Override
    protected T compute() {
        if (!dependencies.isEmpty()) {
            awaitDependencies(); // Ensure all prerequisites are met
        }
        return performTask(); // Carry out the specific task
    }

    // Abstract method to be implemented by specific tasks
    protected abstract T performTask();
}

// Specific task for preparing vegetables
class PrepVeggiesTask extends ChefTask<String> {
    public PrepVeggiesTask(List<ChefTask<?>> dependencies) {
        super(dependencies);
    }

    @Override
    protected String performTask() {
        // Implementation of chopping and washing vegetables
        // Ensure thread-safe access to shared resources if needed
        return "Prepped Veggies";
    }
}
