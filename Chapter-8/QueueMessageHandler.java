package com.example;

import org.springframework.stereotype.Service;

import io.awspring.cloud.messaging.listener.annotation.SqsListener;

@Service
public class QueueMessageHandler {

    @SqsListener("processed-data-queue-url")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
        // Process the message here
    }
}
