package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;

public class S3ObjectProcessor implements RequestHandler<S3Event, String> {
    private static final Logger logger = LoggerFactory.getLogger(S3ObjectProcessor.class);

    @Override
    public String handleRequest(S3Event event, Context context) {
        for (S3EventNotification.S3EventNotificationRecord record : event.getRecords()) {
            String bucketName = record.getS3().getBucket().getName();
            String objectKey = record.getS3().getObject().getKey();
            // Log the bucket name and object key
            logger.info("Bucket Name: {}", bucketName);
            logger.info("Object Key: {}", objectKey);
            // Process the uploaded object (add your processing logic here)
            processS3Object(bucketName, objectKey);
        }
        return "Processing complete";
    }

    private void processS3Object(String bucketName, String objectKey) {
        // Add your logic to process the S3 object here
        logger.info("Processing object {} from bucket {}", objectKey, bucketName);
        // For example, you could download the object, read its contents, etc.
    }
}
