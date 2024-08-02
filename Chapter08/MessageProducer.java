package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MessageProducer {

    private final AmazonSQS sqsClient;
    private final String queueUrl;
    private final ObjectMapper objectMapper; // ObjectMapper to serialize messages

    /**
     * Constructor to initialize the SQS client and queue URL using Spring's @Value
     * to inject properties.
     * 
     * @param queueUrl The URL of the SQS queue where messages will be sent,
     *                 injected from application properties.
     */
    public MessageProducer(@Value("${aws.sqs.queueUrl}") String queueUrl) {
        this.sqsClient = AmazonSQSClientBuilder.standard().build();
        this.queueUrl = queueUrl;
        this.objectMapper = new ObjectMapper(); // Initialize ObjectMapper
    }

    /**
     * Sends a serialized message to the SQS queue.
     * 
     * @param string The message object to be sent.
     * @return The message ID of the sent message.
     */
    public String sendMessage(String string) {
        try {
            String messageBody = objectMapper.writeValueAsString(string); // Serialize message to JSON
            SendMessageRequest sendMsgRequest = new SendMessageRequest()
                    .withQueueUrl(queueUrl)
                    .withMessageBody(messageBody);

            SendMessageResult result = sqsClient.sendMessage(sendMsgRequest);
            return result.getMessageId(); // Return the message ID on successful send
        } catch (Exception e) {
            System.err.println("Error sending message to SQS: " + e.getMessage());
            throw new RuntimeException("Failed to send message to SQS", e);
        }
    }

}
