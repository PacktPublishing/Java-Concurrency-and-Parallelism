package com.example;

import org.springframework.stereotype.Service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;

@Service
public class DataPersistenceService {
    private final AmazonSNS snsClient;
    private final DataRepository dataRepository;

    public DataPersistenceService(DataRepository dataRepository) {
        // Initialize the AmazonSNS client
        this.snsClient = AmazonSNSClientBuilder.standard().build();
        this.dataRepository = dataRepository;
    }

    public void persistData(String data) {
        // Assume 'data' is the processed data received
        // Store the processed data in a database
        Data dataEntity = new Data();
        dataEntity.setProcessedData(data);
        dataRepository.save(dataEntity);

        // Send notification via SNS after successful persistence
        sendNotification("Data has been successfully persisted with the following content: " + data);
    }

    private void sendNotification(String message) {
        // Define the ARN of the SNS topic to send notification to
        String topicArn = "arn:aws:sns:region:account-id:your-topic-name";

        // Create the publish request
        PublishRequest publishRequest = new PublishRequest()
                .withTopicArn(topicArn)
                .withMessage(message);

        // Publish the message to the SNS topic
        snsClient.publish(publishRequest);
    }
}
