package com.example;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadFullException;

public class OrderService {
    class RecommendationEngine {
        public List<Product> getRecommendations(List<Product> products) {
            // Implement your logic to fetch product recommendations based on the input
            // products
            // This could involve querying a database, calling an external recommendation
            // service, etc.
            // For now, we'll just return an empty list as a placeholder
            return Collections.emptyList();
        }
    }

    // Properly instantiate the RecommendationEngine
    RecommendationEngine recommendationEngine = new RecommendationEngine();

    Bulkhead bulkhead = Bulkhead.of("recommendationServiceBulkhead", BulkheadConfig.custom()
            .maxConcurrentCalls(10)
            .build());

    public void processOrder(Order order) {
        Supplier<List<Product>> recommendationCall = Bulkhead.decorateSupplier(bulkhead,
                () -> recommendationEngine.getRecommendations(order.getItems()));

        try {
            List<Product> recommendations = recommendationCall.get();
            if (!recommendations.isEmpty()) {
                displayRecommendations(recommendations);
            } else {
                System.out.println("No recommendations available at this time.");
            }
        } catch (BulkheadFullException e) {
            System.out.println("Recommendation service is currently unavailable, showing default recommendations.");
            // Handle scenario where recommendation service is unavailable (show defaults)
            // Example: Log the error or use default recommendations
        }
    }

    private void displayRecommendations(List<Product> recommendations) {
        System.out.println("Recommended products:");
        for (Product product : recommendations) {
            System.out.println(product.getName()); // Assuming Product class has a getName method
        }
    }
}
