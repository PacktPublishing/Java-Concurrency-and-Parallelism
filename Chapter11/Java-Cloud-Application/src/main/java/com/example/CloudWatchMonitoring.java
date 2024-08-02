package com.example;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.MetricDatum;
import software.amazon.awssdk.services.cloudwatch.model.PutMetricDataRequest;
import software.amazon.awssdk.services.cloudwatch.model.StandardUnit;

public class CloudWatchMonitoring {

    private final CloudWatchClient cloudWatch;

    public CloudWatchMonitoring(String accessKey, String secretKey) {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        this.cloudWatch = CloudWatchClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

    public void publishCustomMetric(String metricName, double value) {
        MetricDatum datum = MetricDatum.builder()
                .metricName(metricName)
                .unit(StandardUnit.COUNT)
                .value(value)
                .build();

        PutMetricDataRequest request = PutMetricDataRequest.builder()
                .namespace("MyAppNamespace")
                .metricData(datum)
                .build();

        cloudWatch.putMetricData(request);
        System.out.println("Metric '" + metricName + "' published to CloudWatch.");
    }
}
