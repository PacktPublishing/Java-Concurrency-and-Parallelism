package com.example;

import java.util.HashMap;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

@ApplicationScoped
public class ProductRepository {

    @Inject
    DynamoDbClient dynamoDbClient;

    private static final String TABLE_NAME = "Products";

    public void persist(Product product) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.builder().s(product.getId()).build());
        item.put("name", AttributeValue.builder().s(product.getName()).build());
        // Add other attributes

        PutItemRequest request = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();

        dynamoDbClient.putItem(request);
    }

    public Product findById(String id) {
        GetItemRequest request = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of("id", AttributeValue.builder().s(id).build()))
                .build();

        GetItemResponse response = dynamoDbClient.getItem(request);
        if (response.hasItem()) {
            return mapToProduct(response.item());
        }
        return null;
    }

    // Add other methods for update, delete, list all

    private Product mapToProduct(Map<String, AttributeValue> item) {
        Product product = new Product();
        product.setId(item.get("id").s());
        product.setName(item.get("name").s());
        // Map other attributes
        return product;
    }
}
