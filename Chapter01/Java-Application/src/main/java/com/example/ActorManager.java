package com.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class ActorManager {
    private ActorSystem system;
    private ActorRef userActor;
    private ActorRef printActor;

    public ActorManager() {
        system = ActorSystem.create("my-system");
        userActor = system.actorOf(UserActor.props(), "user-actor");
        printActor = system.actorOf(PrintActor.props(), "print-actor");
    }

    public void start() {
        // Send request to UserActor and expect PrintActor to handle the response
        userActor.tell(new GetUserRequest("9986"), printActor);
    }

    public void shutdown() {
        system.terminate();
    }

    public static void runActorSystem() {
        ActorManager manager = new ActorManager();
        manager.start();

        // Ensure system doesn't shutdown immediately
        try {
            // Wait some time before shutdown to ensure the response is processed
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        manager.shutdown();
    }

    public static void main(String[] args) {
        // Start the actor system
        runActorSystem();
    }
}
