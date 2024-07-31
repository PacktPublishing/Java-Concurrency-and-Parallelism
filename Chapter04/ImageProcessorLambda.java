package com.example;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class ImageProcessorLambda implements RequestHandler<S3Event, String> {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
    private static final String PROCESSED_BUCKET = "processed-bucket";

    @Override
    public String handleRequest(S3Event event, Context context) {
        event.getRecords().forEach(record -> {
            String bucketName = record.getS3().getBucket().getName();
            String key = record.getS3().getObject().getKey();

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
                s3Client.putObject(new PutObjectRequest(PROCESSED_BUCKET, key, "processed-image-content"));
            }, executorService);

            // Asynchronously call external APIs or fetch user preferences from DynamoDB
            CompletableFuture.supplyAsync(() -> {
                // Placeholder for external API call or fetching user preferences
                System.out.println("Fetching additional data for image: " + key + " from bucket: " + bucketName);
                return "additional-data"; // Simulated return value
            }, executorService).thenAccept(additionalData -> {
                // Process additional data (e.g., tagging based on content)
                System.out.println("Processing additional data: " + additionalData);
            });
        });

        // Shutdown the executor to allow the Lambda function to complete
        // Note: In a real-world scenario, consider carefully when to shut down the
        // executor,
        // as it may be more efficient to keep it alive across multiple invocations if
        // possible
        executorService.shutdown();

        return "Image processing initiated";
    }
}
