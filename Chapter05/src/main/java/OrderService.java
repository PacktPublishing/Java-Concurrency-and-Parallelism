import java.util.List;
import java.util.function.Supplier;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadFullException;

public class OrderService {

    Bulkhead bulkhead = Bulkhead.of("recommendationServiceBulkhead",
            BulkheadConfig.custom().maxConcurrentCalls(10).build());

    // Existing order processing logic...

    public void processOrder(Order order) {
        // Order processing logic...

        Supplier<List<Product>> recommendationCall = Bulkhead
                .decorateSupplier(bulkhead, () -> recommendationEngine.getRecommendations(order.getItems()));

        try {
            List<Product> recommendations = recommendationCall.get();
            // Display recommendations
            System.out.println("Recommended products: " + recommendations);
        } catch (BulkheadFullException e) {
            // Handle scenario where recommendation service is unavailable (show defaults)
            System.out.println("Recommendation service is currently unavailable. Showing default recommendations.");
        }
    }

    // Placeholder for a recommendation engine service
    private RecommendationEngine recommendationEngine = new RecommendationEngine();

    // Inner class to simulate a recommendation engine
    private class RecommendationEngine {
        public List<Product> getRecommendations(List<String> items) {
            // Simulated recommendation logic
            return List.of(new Product("Default Product", 0.0));
        }
    }
}

// Class representing a product
class Product {
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    // Getters and setters...

    @Override
    public String toString() {
        return "Product{name='" + name + "', price=" + price + '}';
    }
}

// Class representing an order
class Order {
    private List<String> items;

    public Order(List<String> items) {
        this.items = items;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
