package com.example;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

@Readiness
@ApplicationScoped
public class ProductHealthCheck implements HealthCheck {

    @Inject
    DynamoDbClient dynamoDbClient;

    private static final String TABLE_NAME = "Products";

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Product service health check");

        try {
            // Attempt to describe the Products table
            dynamoDbClient.describeTable(DescribeTableRequest.builder()
                    .tableName(TABLE_NAME)
                    .build());

            // If no exception is thrown, the table exists and is accessible
            return responseBuilder.up()
                    .withData("table", TABLE_NAME)
                    .withData("status", "accessible")
                    .build();
        } catch (DynamoDbException e) {
            // If an exception is thrown, there's an issue with the DynamoDB table
            return responseBuilder.down()
                    .withData("table", TABLE_NAME)
                    .withData("status", "inaccessible")
                    .withData("error", e.getMessage())
                    .build();
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            return responseBuilder.down()
                    .withData("error", "Unexpected error: " + e.getMessage())
                    .build();
        }
    }
}
