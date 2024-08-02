package com.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.s3.S3Client;

@ApplicationScoped
public class AwsClientsProducer {

    @Produces
    public RekognitionClient createRekognitionClient() {
        return RekognitionClient.builder().build();
    }

    @Produces
    public S3Client createS3Client() {
        return S3Client.builder().build();
    }
}
