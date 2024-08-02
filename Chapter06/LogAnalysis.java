package com.example;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;

public class LogAnalysis {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("Log Analysis")
                .master("local")
                .getOrCreate();

        try {
            // Read log data from a file into a DataFrame
            Dataset<Row> logData = spark.read()
                    .option("header", "true")
                    .option("inferSchema", "true")
                    .csv("path/to/log/data.csv");

            // Filter log entries based on a specific condition
            Dataset<Row> filteredLogs = logData.filter(functions.col("status").geq(400));

            // Group log entries by URL and count the occurrences
            Dataset<Row> urlCounts = filteredLogs.groupBy("url").count();

            // Calculate average response time for each URL
            Dataset<Row> avgResponseTimes = logData
                    .groupBy("url")
                    .agg(functions.avg("responseTime").alias("avgResponseTime"));

            // Join the URL counts with average response times
            Dataset<Row> joinedResults = urlCounts.join(avgResponseTimes, "url");

            // Display the results
            joinedResults.show();
        } catch (Exception e) {
            System.err.println("An error occurred in the Log Analysis process: " + e.getMessage());
            e.printStackTrace();
        } finally {
            spark.stop();
        }
    }
}
