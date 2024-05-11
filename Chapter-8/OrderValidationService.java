package com.example;

import org.springframework.stereotype.Service;

// OrderValidationService.java
@Service
public class OrderValidationService {
    public boolean validateOrder(Order order) {
        // Logic for validating order details, e.g., checking for valid customer info,
        // available products, etc.
        return true; // Example logic for valid order
    }
}

// InvalidOrderException.java
// Exception class for handling invalid order scenarios
class InvalidOrderException extends Exception {
    public InvalidOrderException(String message) {
        super(message);
    }
}
