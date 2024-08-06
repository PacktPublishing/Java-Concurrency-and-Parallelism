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
public class PaymentProcessingFunctionApplication {

    private final AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.defaultClient();
    private final String tableName = System.getenv("DYNAMODB_TABLE_NAME");

    public static void main(String[] args) {
        SpringApplication.run(PaymentProcessingFunctionApplication.class, args);
    }

    @Bean
    public Function<Map<String, Object>, Map<String, Object>> paymentProcessing() {
        return input -> {
            boolean paymentProcessed = processPayment(input);
            Map<String, Object> response = new HashMap<>();
            if (paymentProcessed) {
                boolean updateSuccess = updateDynamoDB(input);
                response.put("status",
                        updateSuccess ? "Payment Processed" : "Payment Processed, but failed to update DynamoDB");
            } else {
                response.put("status", "Payment Failed");
            }
            return response;
        };
    }

    private boolean processPayment(Map<String, Object> input) {
        // Extract payment details
        String paymentToken = (String) input.get("paymentToken");
        String amount = (String) input.get("amount");

        // Placeholder for actual payment processing logic
        // For example, call to a payment gateway API with paymentToken and amount
        boolean isPaymentValid = validatePayment(paymentToken, amount);

        // Assume payment is successful if valid
        return isPaymentValid;
    }

    private boolean validatePayment(String paymentToken, String amount) {
        // Implement actual payment validation logic here
        // For this example, assume the payment is valid if the token is not null and
        // amount is greater than zero
        return paymentToken != null && !paymentToken.isEmpty() && Double.parseDouble(amount) > 0;
    }

    private boolean updateDynamoDB(Map<String, Object> input) {
        // Implement DynamoDB update logic
        String bookingId = (String) input.get("bookingId");

        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#status", "status");
        expressionAttributeNames.put("#paymentDetails", "paymentDetails");

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":status", new AttributeValue().withS("Payment Processed"));
        expressionAttributeValues.put(":paymentDetails", new AttributeValue().withS(input.toString()));

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("BookingId", new AttributeValue().withS(bookingId));

        UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                .withTableName(tableName)
                .withKey(key)
                .withUpdateExpression("SET #status = :status, #paymentDetails = :paymentDetails")
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
}
