package com.example;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

public class RequestHandlerActor {

    public static Behavior<HandleRequest> create(S3Client s3Client) {
        return Behaviors.setup(context -> Behaviors.receiveMessage(message -> {
            processRequest(s3Client, message.bucket, message.key, message.content);
            return Behaviors.same();
        }));
    }

    private static void processRequest(S3Client s3Client, String bucket, String key, String content) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromString(content));

        System.out.println("PutObjectResponse: " + response);
    }

    public static class HandleRequest {
        public final String bucket;
        public final String key;
        public final String content;

        public HandleRequest(String bucket, String key, String content) {
            this.bucket = bucket;
            this.key = key;
            this.content = content;
        }
    }
}
