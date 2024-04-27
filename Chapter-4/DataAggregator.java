package com.example;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DataAggregator {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    public DataAggregator() {
        scheduleDataAggregation();
    }

    private void scheduleDataAggregation() {
        Runnable dataAggregationTask = () -> {
            System.out.println("Aggregating data from sources...");
            // Implement data aggregation logic here
        };

        // Schedule the task to run every hour
        scheduler.scheduleAtFixedRate(dataAggregationTask, 0, 1, TimeUnit.HOURS);
    }
}
