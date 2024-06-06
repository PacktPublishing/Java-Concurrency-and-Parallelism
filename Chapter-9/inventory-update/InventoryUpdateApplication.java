package com.example;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;

@SpringBootApplication
public class InventoryUpdateApplication {

    private final AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.defaultClient();
    private final String inventoryTableName = System.getenv("INVENTORY_TABLE_NAME");
    private final String bookingTableName = System.getenv("BOOKING_TABLE_NAME");

    public static void main(String[] args) {
        SpringApplication.run(InventoryUpdateApplication.class, args);
    }

    @Bean
    public Function<Map<String, Object>, Map<String, Object>> inventoryUpdate() {
        return input -> {
            boolean inventoryUpdated = updateInventory(input);
            Map<String, Object> response = new HashMap<>();
            if (inventoryUpdated) {
                updateBookingTable(input);
                response.put("status", "Inventory Updated");
            } else {
                response.put("status", "Inventory Update Failed");
            }
            return response;
        };
    }

    private boolean updateInventory(Map<String, Object> input) {
        // Extract inventory details
        String itemId = (String) input.get("itemId");
        int quantity = (int) input.get("quantity");

        // Placeholder for actual inventory update logic
        // For example, decrement the quantity in the InventoryTable

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("itemId", new AttributeValue().withS(itemId));

        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#quantity", "quantity");

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":decrement", new AttributeValue().withN(String.valueOf(-quantity)));

        UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                .withTableName(inventoryTableName)
                .withKey(key)
                .withUpdateExpression("SET #quantity = #quantity + :decrement")
                .withExpressionAttributeNames(expressionAttributeNames)
                .withExpressionAttributeValues(expressionAttributeValues);

        try {
            dynamoDBClient.updateItem(updateItemRequest);
            return true;
        } catch (Exception e) {
            // Log exception
            e.printStackTrace();
            return false;
        }
    }

    private void updateBookingTable(Map<String, Object> input) {
        // Extract booking details
        String bookingId = (String) input.get("bookingId");

        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#bookingDetails", "bookingDetails");

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":bookingDetails", new AttributeValue().withS(input.toString()));

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("bookingId", new AttributeValue().withS(bookingId));

        UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                .withTableName(bookingTableName)
                .withKey(key)
                .withUpdateExpression("SET #bookingDetails = :bookingDetails")
                .withExpressionAttributeNames(expressionAttributeNames)
                .withExpressionAttributeValues(expressionAttributeValues);

        dynamoDBClient.updateItem(updateItemRequest);
    }
}
