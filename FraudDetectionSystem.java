package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FraudDetectionSystem {
    private ExecutorService executorService;

    public FraudDetectionSystem(int numThreads) {
        executorService = Executors.newFixedThreadPool(numThreads);
    }

    public Future<Boolean> analyzeTransaction(Transaction transaction) {
        return executorService.submit(() -> {
            // Here, add the logic to determine if the transaction is fraudulent
            boolean isFraudulent = false; // This should be replaced with actual fraud detection logic

            // Assuming a simple condition for demonstration, e.g., high amount indicates
            // potential fraud
            if (transaction.getAmount() > 10000) {
                isFraudulent = true;
            }
            return isFraudulent;
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public static void main(String[] args) {
        FraudDetectionSystem fraudDetectionSystem = new FraudDetectionSystem(10);

        // Create a sample transaction with a specific amount
        Transaction transaction = new Transaction("T123", 15000, System.currentTimeMillis());

        // Submit the transaction for analysis
        Future<Boolean> resultFuture = fraudDetectionSystem.analyzeTransaction(transaction);
        try {
            // Perform other tasks while the analysis is being performed asynchronously
            // Retrieve the analysis result
            boolean isFraudulent = resultFuture.get();

            // Process the result
            System.out.println("Is transaction fraudulent? " + isFraudulent);
            // Shutdown the fraud detection system when no longer needed
            fraudDetectionSystem.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
