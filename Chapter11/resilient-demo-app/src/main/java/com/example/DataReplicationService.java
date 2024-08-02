package com.example;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class DataReplicationService {
    private final S3Client s3Client;
    private final DynamoDbClient dynamoDbClient;
    private final String tableName = "MyTable";

    public DataReplicationService() {
        this.s3Client = S3Client.builder().build();
        this.dynamoDbClient = DynamoDbClient.builder().build();
    }

    public void replicateToS3(String key, String content, String val) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket("my-bucket")
                .key(key)
                .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromString(content));
    }

    public void replicateToDynamoDB(String key, String value) {
        boolean success = false;
        int retryCount = 0;
        while (!success && retryCount < 3) {
            try {
                PutItemRequest putItemRequest = PutItemRequest.builder()
                        .tableName(tableName)
                        .item(Map.of("Key", AttributeValue.builder().s(key).build(),
                                "Value", AttributeValue.builder().s(value).build()))
                        .build();
                dynamoDbClient.putItem(putItemRequest);
                success = true;
            } catch (DynamoDbException e) {
                retryCount++;
                try {
                    Thread.sleep(1000); // Wait before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        if (!success) {
            throw new RuntimeException("Failed to replicate to DynamoDB after retries");
        }
    }

    public Optional<String> retrieveFromDynamoDB(String key) {
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("Key", AttributeValue.builder().s(key).build()))
                .build();
        try {
            GetItemResponse response = dynamoDbClient.getItem(getItemRequest);
            return Optional.ofNullable(response.item().get("Value")).map(AttributeValue::s);
        } catch (DynamoDbException e) {
            throw new RuntimeException("Failed to retrieve item from DynamoDB", e);
        }
    }

    public void resolveConflict(String key, String newValue) {
        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("Key", AttributeValue.builder().s(key).build()))
                .updateExpression("set #v = :val")
                .expressionAttributeNames(Map.of("#v", "Value"))
                .expressionAttributeValues(Map.of(":val", AttributeValue.builder().s(newValue).build()))
                .build();
        try {
            dynamoDbClient.updateItem(updateItemRequest);
        } catch (DynamoDbException e) {
            throw new RuntimeException("Failed to resolve conflict in DynamoDB", e);
        }
    }

    public void backupDynamoDBToS3(String key) {
        Optional<String> value = retrieveFromDynamoDB(key);
        value.ifPresent(val -> replicateToS3("my-bucket-backup", key, val));
    }

    public void restoreFromS3(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket("my-bucket-backup")
                .key(key)
                .build();
        try {
            String value = s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes()).asUtf8String();
            replicateToDynamoDB(key, value);
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to restore from S3", e);
        }
    }
}
