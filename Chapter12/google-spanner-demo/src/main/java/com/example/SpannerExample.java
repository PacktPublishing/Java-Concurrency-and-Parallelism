package com.example;

import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.ResultSet;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spanner.Statement;

public class SpannerExample {

    private final SpannerOptions spannerOptions;

    public SpannerExample(SpannerOptions spannerOptions) {
        this.spannerOptions = spannerOptions;
    }

    public void run(String projectId, String instanceId, String databaseId) {
        try (Spanner spanner = spannerOptions.getService()) {
            DatabaseClient dbClient = spanner.getDatabaseClient(DatabaseId.of(projectId, instanceId, databaseId));
            try (ResultSet resultSet = dbClient
                    .singleUse() // Create a single-use read-only transaction
                    .executeQuery(Statement.of("SELECT * FROM Users"))) {
                while (resultSet.next()) {
                    System.out.printf("User ID: %d, Name: %s\n",
                            resultSet.getLong("UserId"),
                            resultSet.getString("Name"));
                }
            }
        }
    }

    public static void main(String[] args) {
        String projectId = args.length > 0 ? args[0] : "mock-project-id";
        String instanceId = args.length > 1 ? args[1] : "mock-instance-id";
        String databaseId = args.length > 2 ? args[2] : "mock-database-id";
        SpannerOptions options = SpannerOptions.newBuilder().setProjectId(projectId).build();
        new SpannerExample(options).run(projectId, instanceId, databaseId);
    }
}
