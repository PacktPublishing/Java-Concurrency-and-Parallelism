package com.example;

public class Product {
    private String id;
    private String name;
    private String description;
    private double price;
    private String category;

    // Constructor
    public Product(String id, String name, String description, double price, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // toString method for debugging or display purposes
    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", description=" + description
                + ", price=" + price + ", category=" + category + "]";
    }
}