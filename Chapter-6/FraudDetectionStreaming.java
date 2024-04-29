package com.example;

import java.util.concurrent.TimeoutException;

import org.apache.spark.ml.PipelineModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.StructType;

public class FraudDetectionStreaming {
        public static void main(String[] args) throws StreamingQueryException {
                SparkSession spark = SparkSession.builder()
                                .appName("FraudDetectionStreaming")
                                .getOrCreate();

                PipelineModel model = PipelineModel.load("path/to/trained/model");

                StructType schema = new StructType()
                                .add("transactionId", "string")
                                .add("amount", "double")
                                .add("accountNumber", "string")
                                .add("transactionTime", "timestamp")
                                .add("merchantId", "string");

                Dataset<Row> transactionsStream = spark
                                .readStream()
                                .format("csv")
                                .option("header", "true")
                                .schema(schema)
                                .load("path/to/transaction/data");

                Dataset<Row> predictionStream = model.transform(transactionsStream);

                predictionStream = predictionStream
                                .select("transactionId", "amount", "accountNumber", "transactionTime", "merchantId",
                                                "prediction", "probability");

                StreamingQuery query = null;
                try {
                        query = predictionStream
                                        .writeStream()
                                        .outputMode("append")
                                        .format("console")
                                        .start();
                } catch (TimeoutException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

                query.awaitTermination();
        }
}
