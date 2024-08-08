package com.example;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZookeeperExample {
    private static CuratorFramework zkClient;
    private static final String zkConnectionString = "localhost:2181"; // Replace with your ZooKeeper server address
    private static final String zkNodePath = "/processed-requests"; // Define the path as a static variable

    public static void main(String[] args) {
        // Connect to ZooKeeper server
        zkClient = CuratorFrameworkFactory.newClient(
                zkConnectionString, new ExponentialBackoffRetry(1000, 3));
        zkClient.start();

        // Create a persistent node to store the latest processed request ID
        try {
            if (zkClient.checkExists().forPath(zkNodePath) == null) {
                zkClient.create().creatingParentsIfNeeded().forPath(zkNodePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Example request processing
        processRequest("request-12345");
    }

    // Implement request processing logic
    public static void processRequest(String requestId) {
        String requestPath = zkNodePath + "/" + requestId;
        try {
            // Check if the request has already been processed
            if (zkClient.checkExists().forPath(requestPath) != null) {
                System.out.println("Request already processed: " + requestId);
                return;
            }
            // Process the request
            // ...

            // Mark the request as processed in ZooKeeper
            zkClient.create().forPath(requestPath);
            System.out.println("Processed request: " + requestId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
