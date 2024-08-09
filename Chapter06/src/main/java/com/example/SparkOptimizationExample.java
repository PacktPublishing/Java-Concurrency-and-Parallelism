package com.example;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;

public class SparkOptimizationExample {

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("Advanced Spark Optimization")
                .master("local")
                .getOrCreate();

        // Load and cache data
        Dataset<Row> df = spark.read().json("path/to/data.json").cache();

        // Example transformation with explicit caching
        Dataset<Row> processedDf = df
                .filter("age > 25")
                .groupBy("occupation")
                .count();

        // Persist the processed DataFrame with a specific storage level
        processedDf.persist(StorageLevel.MEMORY_AND_DISK());

        // Action to trigger execution
        processedDf.show();

        // Example of fault tolerance: re-computation from cache after failure
        try {
            // Simulate data processing that might fail
            processedDf.filter("count > 5").show();
        } catch (Exception e) {
            System.out.println("Error during processing, retrying...");
            processedDf.filter("count > 5").show();
        }

        spark.stop();
    }
}
