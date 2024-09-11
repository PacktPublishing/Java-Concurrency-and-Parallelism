package com.example;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

public class HighConcurrencyService {

    public static void main(String[] args) {
        S3Client s3Client = S3Client.builder()
                .region(Region.US_WEST_2) // Ensure this matches your bucket's region
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("Your Access Key", "Your Secret Access Key")))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();

        ActorSystem<Void> actorSystem = ActorSystem.create(Behaviors.setup(context -> {
            for (int i = 0; i < 1000; i++) {
                Behavior<RequestHandlerActor.HandleRequest> behavior = RequestHandlerActor.create(s3Client);
                var requestHandlerActor = context.spawn(behavior, "request-handler-" + i);
                requestHandlerActor.tell(
                        new RequestHandlerActor.HandleRequest("example-bucket", "example-key-" + i, "example-content"));
            }
            return Behaviors.empty();
        }), "high-concurrency-system");

        // Wait for a while to allow actors to process messages
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Clean up
        actorSystem.terminate();
    }
}
