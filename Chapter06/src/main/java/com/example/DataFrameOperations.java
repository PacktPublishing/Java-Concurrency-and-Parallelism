package com.example;

import static org.apache.spark.sql.functions.*; // Importing Spark SQL functions

import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

public class DataFrameOperations {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("DataFrame Operations")
                .master("local")
                .getOrCreate();

        // Create an RDD from a text file
        JavaRDD<String> textRDD = spark.sparkContext().textFile("path/to/data.txt", 1).toJavaRDD();

        // Convert the RDD of strings to an RDD of Rows
        JavaRDD<Row> rowRDD = textRDD.map(new Function<String, Row>() {
            @Override
            public Row call(String line) {
                String[] parts = line.split(",");
                return RowFactory.create(parts[0], Integer.parseInt(parts[1]));
            }
        });

        // Define the schema for the DataFrame
        StructType schema = new StructType()
                .add("name", DataTypes.StringType)
                .add("age", DataTypes.IntegerType);

        // Create the DataFrame from the RDD and the schema
        Dataset<Row> df = spark.createDataFrame(rowRDD, schema);

        // Filtering, selecting, and manipulating the DataFrame
        Dataset<Row> filteredDf = df.filter(col("age").gt(25));
        Dataset<Row> selectedDf = df.select("name", "age");
        Dataset<Row> newDf = df.withColumn("doubledAge", col("age").multiply(2));
        Dataset<Row> aggregatedDf = df.groupBy("age").agg(count("*").as("count"));

        // Output filtered, selected, and aggregated dataframes
        filteredDf.show();
        selectedDf.show();
        newDf.show();
        aggregatedDf.show();

        // Collecting data and writing to output
        List<Row> collectedData = df.collectAsList();
        long count = df.count();

        System.out.println("Collected Data: " + collectedData);
        System.out.println("Total Count: " + count);

        df.write().format("parquet").save("path/to/output");

        spark.stop();
    }
}