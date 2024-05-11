package com.example;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    // Simulate a database of users
    private List<User> users = Arrays.asList(
            new User(1, "John Doe", "john@example.com"),
            new User(2, "Jane Smith", "jane@example.com"));

    // Method for API version 1 - returns user entities
    public List<User> findAllUsers() {
        return users;
    }

    // Method for API version 2 - returns user DTOs
    public List<UserDto> findAllUsersV2() {
        return users.stream()
                .map(user -> new UserDto(user.getId(), user.getName()))
                .collect(Collectors.toList());
    }
}
