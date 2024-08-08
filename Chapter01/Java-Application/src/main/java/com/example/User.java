package com.example;

public class User {
    private String userId;
    private String name;

    public User(String id, String name) {
        this.userId = id;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}