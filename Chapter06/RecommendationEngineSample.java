package com.example;

import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class RecommendationEngineSample {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("Recommendation Engine Sample")
                .master("local")
                .getOrCreate();

        // Read ratings data from a CSV file
        Dataset<Row> ratings = spark.read()
                .option("header", "true")
                .option("inferSchema", "true")
                .csv("path/to/ratings/data.csv");

        // Split the data into training and testing sets
        Dataset<Row>[] splits = ratings.randomSplit(new double[] { 0.8, 0.2 });
        Dataset<Row> trainingData = splits[0];
        Dataset<Row> testingData = splits[1];

        // Create an ALS model
        ALS als = new ALS()
                .setMaxIter(10)
                .setRegParam(0.01)
                .setUserCol("userId")
                .setItemCol("itemId")
                .setRatingCol("rating");

        // Train the model
        ALSModel model = als.fit(trainingData);

        // Generate predictions on the testing data
        Dataset<Row> predictions = model.transform(testingData);

        // Evaluate the model
        RegressionEvaluator evaluator = new RegressionEvaluator()
                .setMetricName("rmse")
                .setLabelCol("rating")
                .setPredictionCol("prediction");
        double rmse = evaluator.evaluate(predictions);
        System.out.println("Root-mean-square error = " + rmse);

        // Generate top 10 movie recommendations for each user
        Dataset<Row> userRecs = model.recommendForAllUsers(10);
        userRecs.show();

        spark.stop();
    }
}
