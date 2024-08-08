package com.example;

import reactor.core.publisher.Mono;

public class UserServiceImpl implements UserService {

  @Override
  public Mono<User> getUserById(String userId) {
    return Mono.fromCallable(() -> {
      // Simulate fetching user data from a database
      Thread.sleep(600);
      return new User(userId, "Jack Smith");
    });
  }

  // Example usage
  public static void main(String[] args) {
    UserServiceImpl userService = new UserServiceImpl();
    Mono<User> userMono = userService.getUserById("99888");
    userMono.subscribe(user -> {
      System.out.println("User ID: " + user.getUserId());
      System.out.println("User Name: " + user.getName());
    });
  }
}
