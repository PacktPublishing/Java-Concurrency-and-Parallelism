package com.example;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class UserActor extends AbstractActor {
  public static Props props() {
    return Props.create(UserActor.class);
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(GetUserRequest.class, this::handleGetUserRequest)
        .build();
  }

  private void handleGetUserRequest(GetUserRequest request) throws InterruptedException {
    // Simulate fetching user data
    Thread.sleep(600);
    User user = new User(request.getUserId(), "Jack Smith");
    getSender().tell(new GetUserResponse(user), getSelf());
  }
}

class PrintActor extends AbstractActor {
  public static Props props() {
    return Props.create(PrintActor.class);
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(GetUserResponse.class, this::handleGetUserResponse)
        .build();
  }

  private void handleGetUserResponse(GetUserResponse response) {
    System.out.println("User: " + response.getUser().getName());
  }
}

class GetUserRequest {
  private String userId;

  public GetUserRequest(String userId) {
    this.userId = userId;
  }

  public String getUserId() {
    return userId;
  }
}

class GetUserResponse {
  private User user;

  public GetUserResponse(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }
}