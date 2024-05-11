package com.example;

import org.springframework.stereotype.Service;

// Additional class representing an order
@Service
public class Order {
    private String id;
    private double amount;

    // Constructors, getters, setters, and other properties
    public Order(String id) {
        this.id = id;
    }

    /**
     * @return double return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @return String return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

}
