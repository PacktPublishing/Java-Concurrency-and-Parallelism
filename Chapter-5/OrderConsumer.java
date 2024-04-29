package com.example;

// Assuming imports based on the use case
import java.util.NoSuchElementException;

interface OrderQueue {
    Order getNextOrder() throws NoSuchElementException;
}

interface CircuitBreaker {
    boolean isClosed();

    void trip();
}

class ServiceException extends Exception {
    public ServiceException(String message) {
        super(message);
    }
}

public class OrderConsumer implements Runnable {
    private OrderQueue queue;
    private CircuitBreaker paymentCircuitBreaker;

    public OrderConsumer(OrderQueue queue, CircuitBreaker paymentCircuitBreaker) {
        this.queue = queue;
        this.paymentCircuitBreaker = paymentCircuitBreaker;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Order order = queue.getNextOrder(); // Potentially throws NoSuchElementException
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
            } catch (NoSuchElementException e) {
                // Handle the case when no more orders are in the queue or queue handling
                break; // Or handle differently, like sleep and retry
            }
        }
    }

    private void processPayment(Order order) throws ServiceException {
        // Simulate payment processing logic
        // Throw ServiceException if payment fails
    }

    private void handlePaymentFailure(Order order) {
        // Handle failed payment case, log or retry logic
    }

    private void retryOrderLater(Order order) {
        // Implement retry logic
    }
}
