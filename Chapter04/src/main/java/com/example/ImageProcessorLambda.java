package com.example;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class ImageProcessorLambda implements RequestHandler<Map<String, Object>, String> {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final S3Client s3Client = S3Client.builder().build();

    @SuppressWarnings("unchecked")
    @Override
    public String handleRequest(Map<String, Object> event, Context context) {
        // Extract relevant data from the raw event map
        // This example assumes the structure of the event and should be adapted to your
        // use case

        List<Map<String, Object>> records = (List<Map<String, Object>>) event.get("Records");

        if (records != null) {
            for (Map<String, Object> record : records) {
                Map<String, Object> s3 = (Map<String, Object>) record.get("s3");
                Map<String, Object> object = (Map<String, Object>) s3.get("object");
                String key = (String) object.get("key");

                // Asynchronously resize and optimize image
                CompletableFuture.runAsync(() -> {
                    // Placeholder for image resizing and optimization logic
                    System.out.println("Resizing and optimizing image: " + key);
                    // Simulate image processing
                    try {
                        Thread.sleep(1000); // Simulate processing delay
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    // Upload the processed image to a different bucket or prefix
                    s3Client.putObject(PutObjectRequest.builder()
                            .bucket("processed-bucket")
                            .key(key)
                            .build(),
                            software.amazon.awssdk.core.sync.RequestBody.fromString("processed-image-content"));
                }, executorService);

                // Asynchronously call external APIs or fetch user preferences from DynamoDB
                CompletableFuture.supplyAsync(() -> {
                    // Placeholder for external API call or fetching user preferences
                    System.out.println("Fetching additional data for image: " + key);
                    return "additional-data"; // Simulated return value
                }, executorService).thenAccept(additionalData -> {
                    // Process additional data (e.g., tagging based on content)
                    System.out.println("Processing additional data: " + additionalData);
                });
            }
        }

        // Shutdown the executor to allow the Lambda function to complete
        executorService.shutdown();

        return "Image processing initiated";
    }
}
