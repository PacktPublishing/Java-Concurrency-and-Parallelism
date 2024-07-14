package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.Props;
import akka.actor.typed.javadsl.Behaviors;
import software.amazon.awssdk.services.s3.S3Client;

public class HighConcurrencyService {

    public static void main(String[] args) {
        ActorSystem<Void> actorSystem = ActorSystem.create(Behaviors.empty(), "high-concurrency-system");
        S3Client s3Client = S3Client.create();
        ExecutorService executorService = Executors.newCachedThreadPool(); // Use a compatible thread pool

        for (int i = 0; i < 1000; i++) {
            final int index = i;
            executorService.submit(() -> {
                // Create and start the actor
                Behavior<RequestHandlerActor.HandleRequest> behavior = RequestHandlerActor.create(s3Client);
                var requestHandlerActor = actorSystem.systemActorOf(behavior, "request-handler-" + index,
                        Props.empty());

                // Send a request to the actor
                requestHandlerActor.tell(new RequestHandlerActor.HandleRequest("example-bucket", "example-key-" + index,
                        "example-content"));
            });
        }

        // Clean up
        executorService.shutdown();
        actorSystem.terminate();
    }
}
