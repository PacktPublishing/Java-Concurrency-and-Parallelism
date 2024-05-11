package com.example;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
    UserService userService = new UserService();

    // Version 1 of the API
    @GetMapping(value = "/users", headers = "X-API-Version=1")
    public List<User> getUsersV1() {

        return userService.findAllUsers();
    }

    // Version 2 of the API
    @GetMapping(value = "/users", headers = "X-API-Version=2")
    public List<UserDto> getUsersV2() {
        return userService.findAllUsersV2();
    }
}
