package com.example;

import java.util.concurrent.Future;

public class FinancialProcess {
    public class Main {
        public static void main(String[] args) {
            FraudDetectionSystem fraudDetectionSystem = new FraudDetectionSystem(10);

            // Create a sample transaction with a specific amount
            Transaction transaction = new Transaction("T123", 15000, System.currentTimeMillis());

            // Submit the transaction for analysis
            Future<Boolean> resultFuture = fraudDetectionSystem.analyzeTransaction(transaction);

            try {
                // Perform other tasks while the analysis is being performed asynchronously
                // ...

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
}
