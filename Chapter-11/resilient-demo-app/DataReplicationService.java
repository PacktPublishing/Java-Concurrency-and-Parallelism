package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

@Service
public class DataReplicationService {

    private final AmazonS3 s3Client;
    private final DynamoDB dynamoDB;
    private final Table table;

    public DataReplicationService() {
        this.s3Client = AmazonS3ClientBuilder.defaultClient();
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        this.dynamoDB = new DynamoDB(client);
        this.table = dynamoDB.getTable("MyTable");
    }

    public void replicateToS3(String key, String content) {
        s3Client.putObject("my-bucket", key, content);
    }

    public void replicateToDynamoDB(String key, String value) {
        boolean success = false;
        int retryCount = 0;
        while (!success && retryCount < 3) {
            try {
                table.putItem(new Item().withPrimaryKey("Key", key).withString("Value", value));
                success = true;
            } catch (Exception e) {
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
        Item item = table.getItem("Key", key);
        return Optional.ofNullable(item).map(i -> i.getString("Value"));
    }

    public void resolveConflict(String key, String newValue) {
        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey("Key", key)
                .withUpdateExpression("set #v = :val")
                .withNameMap(new NameMap().with("#v", "Value"))
                .withValueMap(new ValueMap().with(":val", newValue));

        try {
            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve conflict in DynamoDB", e);
        }
    }

    public void backupDynamoDBToS3(String key) {
        Optional<String> value = retrieveFromDynamoDB(key);
        value.ifPresent(val -> s3Client.putObject("my-bucket-backup", key, val));
    }

    public void restoreFromS3(String key) {
        S3Object s3Object = s3Client.getObject("my-bucket-backup", key);
        try (S3ObjectInputStream s3is = s3Object.getObjectContent()) {
            String value = new BufferedReader(new InputStreamReader(s3is)).lines().collect(Collectors.joining("\n"));
            replicateToDynamoDB(key, value);
        } catch (IOException e) {
            throw new RuntimeException("Failed to restore from S3", e);
        }
    }
}
