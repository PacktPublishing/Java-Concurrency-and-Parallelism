package com.example;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @PostMapping("/order")
    public String placeOrder(@RequestBody Order order) {
        // Simulate CPU-intensive processing
        processOrder(order);
        return "Order for " + order.getItem() + " placed successfully!";
    }

    private void processOrder(Order order) {
        // Simulate some CPU work
        for (int i = 0; i < 1000000; i++) {
            Math.sqrt(order.getQuantity() * Math.random());
        }
    }
}

class Order {
    private String item;
    private int quantity;

    // Getters and setters
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
