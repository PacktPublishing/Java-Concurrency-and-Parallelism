
// Import statements (assuming these classes exist in the project or relevant packages)
import java.util.concurrent.BlockingQueue;

public class OrderConsumer implements Runnable {
    private BlockingQueue<Order> queue;
    private CircuitBreaker paymentCircuitBreaker;

    public OrderConsumer(BlockingQueue<Order> queue, CircuitBreaker paymentCircuitBreaker) {
        this.queue = queue;
        this.paymentCircuitBreaker = paymentCircuitBreaker;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Order order = queue.take(); // Blocking call, waits for an order to be available
                if (paymentCircuitBreaker.isClosed()) {
                    try {
                        processPayment(order);
                    } catch (ServiceException e) {
                        paymentCircuitBreaker.trip();
                        handlePaymentFailure(order);
                    }
                } else {
                    // Handle the case when the circuit breaker is open
                    retryOrderLater(order);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore the interrupted status
                break; // Exit the loop if the thread is interrupted
            }
        }
    }

    private void processPayment(Order order) throws ServiceException {
        // Implement payment processing logic here
        // Throw ServiceException if the payment service fails
        System.out.println("Processing payment for order: " + order.getId());
        // Example: PaymentService.process(order);
    }

    private void handlePaymentFailure(Order order) {
        // Implement logic for handling payment failure
        System.out.println("Payment failed for order: " + order.getId());
        // Example: Notify customer, log error, etc.
    }

    private void retryOrderLater(Order order) {
        // Implement logic to retry the order later or requeue it
        System.out.println("Retrying order later due to open circuit breaker: " + order.getId());
        // Example: Requeue the order, schedule retry, etc.
    }
}

// Assume Order, CircuitBreaker, and ServiceException are defined somewhere in
// the project
class Order {
    private String id;
    // Other order details...

    public String getId() {
        return id;
    }

    // Other getters and setters...
}

class CircuitBreaker {
    public boolean isClosed() {
        // Implement logic to check if the circuit breaker is closed
        return true; // Placeholder implementation
    }

    public void trip() {
        // Implement logic to trip the circuit breaker
        System.out.println("Circuit breaker tripped");
    }
}

class ServiceException extends Exception {
    public ServiceException(String message) {
        super(message);
    }
}
