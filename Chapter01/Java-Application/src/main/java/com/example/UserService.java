package com.example;

import reactor.core.publisher.Mono;

public interface UserService {
  Mono<User> getUserById(String userId);
}
