package com.example;

import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository {

  private List<Product> products = new ArrayList<>();

  public ProductRepository() {
    // Initialize with some dummy data
    Product p1 = new Product();
    p1.setId(1);
    p1.setName("Product1");
    p1.setPrice(100.0);

    Product p2 = new Product();
    p2.setId(2);
    p2.setName("Product2");
    p2.setPrice(150.0);

    products.add(p1);
    products.add(p2);
  }

  public List<Product> getAllProducts() {
    return products;
  }

  public long countAllProducts() {
    return products.size();
  }

  public Product getProduct(int id) {
    return products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
  }
}
