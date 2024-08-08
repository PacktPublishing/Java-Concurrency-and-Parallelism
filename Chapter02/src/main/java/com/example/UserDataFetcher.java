package com.example;

import java.util.concurrent.CompletableFuture;

public class UserDataFetcher {
    public static void main(String[] args) {
        CompletableFuture.supplyAsync(() -> fetchUserData())
                .thenAccept(userData -> processUserData(userData));
    }

    private static String fetchUserData() {
        // Simulate fetching data from a remote service
        return "Fetched user data";
    }

    private static void processUserData(String data) {
        System.out.println("Processing data: " + data);
    }
}
