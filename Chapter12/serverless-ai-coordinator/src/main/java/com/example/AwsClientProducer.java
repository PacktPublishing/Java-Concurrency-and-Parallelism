package com.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.s3.S3Client;

@ApplicationScoped
public class AwsClientProducer {
    @Produces
    @ApplicationScoped
    public DynamoDbClient produceDynamoDbClient() {
        return DynamoDbClient.builder().build();
    }

    @Produces
    @ApplicationScoped
    public EcsClient produceEcsClient() {
        return EcsClient.builder().build();
    }

    @Produces
    @ApplicationScoped
    public S3Client produceS3Client() {
        return S3Client.builder().build();
    }
}
