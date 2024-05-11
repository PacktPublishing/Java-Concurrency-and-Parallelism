package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class DataProcessingLambda implements RequestHandler<SQSEvent, Void> {
    private final AmazonSQS sqsClient;

    public DataProcessingLambda() {
        this.sqsClient = AmazonSQSClientBuilder.defaultClient();
    }

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage message : event.getRecords()) {
            String data = message.getBody();

            // Transform the data within the Lambda function
            String transformedData = transformData(data);

            // Publish the transformed data to another Amazon SQS for persistence or further
            // processing
            sqsClient.sendMessage(new SendMessageRequest()
                    .withQueueUrl("processed-data-queue-url")
                    .withMessageBody(transformedData));
        }
        return null;
    }

    /**
     * Simulate data transformation.
     * In a real scenario, this method would contain logic to transform data based
     * on specific rules or operations.
     *
     * @param data the original data from the SQS message
     * @return transformed data as a String
     */
    private String transformData(String data) {
        // Example transformation: append a timestamp or modify the string in some way
        return "Transformed: " + data + " at " + System.currentTimeMillis();
    }
}
