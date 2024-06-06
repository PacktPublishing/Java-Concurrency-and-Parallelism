package com.example;

import java.util.Map;
import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

@SpringBootApplication
public class SendNotificationFunctionApplication {

    private final AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();
    private final String topicArn = System.getenv("BOOKING_NOTIFICATION_TOPIC_ARN");

    public static void main(String[] args) {
        SpringApplication.run(SendNotificationFunctionApplication.class, args);
    }

    @Bean
    public Function<Map<String, Object>, String> sendNotification() {
        return input -> {
            String message = "Booking notification: " + input.toString();
            return sendNotification(message);
        };
    }

    private String sendNotification(String message) {
        PublishRequest publishRequest = new PublishRequest(topicArn, message);
        PublishResult publishResult = snsClient.publish(publishRequest);
        return publishResult.getMessageId();
    }
}
