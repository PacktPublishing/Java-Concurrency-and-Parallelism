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
public class SecurityCheckFunctionApplication {

    private final AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.defaultClient();
    private final String tableName = System.getenv("DYNAMODB_TABLE_NAME");

    public static void main(String[] args) {
        SpringApplication.run(SecurityCheckFunctionApplication.class, args);
    }

    @Bean
    public Function<Map<String, Object>, Map<String, Object>> securityCheck() {
        return input -> {
            boolean isAuthenticated = authenticateUser(input);
            Map<String, Object> response = new HashMap<>();
            if (isAuthenticated) {
                updateDynamoDB(input);
                response.put("status", "Security Check Passed");
            } else {
                response.put("status", "Security Check Failed");
            }
            return response;
        };
    }

    private boolean authenticateUser(Map<String, Object> input) {
        // Implement authentication logic using Cognito
        // Placeholder for actual Cognito authentication logic
        // For example: call cognitoClient.adminInitiateAuth(), etc.

        // Assume authentication is successful for this example
        return true;
    }

    private void updateDynamoDB(Map<String, Object> input) {
        // Implement DynamoDB update logic
        String bookingId = (String) input.get("bookingId");

        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#status", "status");

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":status", new AttributeValue().withS("Security Check Passed"));

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("BookingId", new AttributeValue().withS(bookingId));

        UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                .withTableName(tableName)
                .withKey(key)
                .withUpdateExpression("SET #status = :status")
                .withExpressionAttributeNames(expressionAttributeNames)
                .withExpressionAttributeValues(expressionAttributeValues);

        dynamoDBClient.updateItem(updateItemRequest);
    }
}
