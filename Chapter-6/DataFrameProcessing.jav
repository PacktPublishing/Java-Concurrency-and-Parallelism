package com.example;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;

public class DataFrameProcessing {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
            .appName("DataFrame Processing")
            .master("local")
            .getOrCreate();

        // Sample dataframes creation (df1 and df2)
        Dataset<Row> df1 = spark.read()
            .option("header", "true")
            .option("inferSchema", "true")
            .csv("path/to/data1.csv");

        Dataset<Row> df2 = spark.read()
            .option("header", "true")
            .option("inferSchema", "true")
            .csv("path/to/data2.csv");

        // Creating a temporary view for SQL operations
        df1.createOrReplaceTempView("people");

        // SQL query to filter rows
        Dataset<Row> sqlResult = spark.sql("SELECT * FROM people WHERE age > 25");

        sqlResult.show(); // Example output to see results

        // Joining df1 and df2 on a specific key
        Dataset<Row> joinedDf = df1.join(df2, df1.col("id").equalTo(df2.col("personId")));

        joinedDf.show(); // Example output to see joined results

        spark.stop();
    }
}
