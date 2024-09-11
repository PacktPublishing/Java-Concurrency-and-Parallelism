package com.example;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class RequestHandlerActor extends AbstractBehavior<RequestHandlerActor.HandleRequest> {

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

    private final S3Client s3Client;

    private RequestHandlerActor(ActorContext<HandleRequest> context, S3Client s3Client) {
        super(context);
        this.s3Client = s3Client;
    }

    public static Behavior<HandleRequest> create(S3Client s3Client) {
        return Behaviors.setup(context -> new RequestHandlerActor(context, s3Client));
    }

    @Override
    public Receive<HandleRequest> createReceive() {
        return newReceiveBuilder()
                .onMessage(HandleRequest.class, this::onHandleRequest)
                .build();
    }

    private Behavior<HandleRequest> onHandleRequest(HandleRequest request) {
        getContext().getLog().info("Handling request for bucket: {}, key: {}", request.bucket, request.key);
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket("Your Bucket Name") // Replace with your actual bucket name
                    .key(request.key)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromString(request.content));
            getContext().getLog().info("Successfully uploaded object to S3");
        } catch (Exception e) {
            getContext().getLog().error("Error uploading to S3", e);
        }
        return this;
    }
}
