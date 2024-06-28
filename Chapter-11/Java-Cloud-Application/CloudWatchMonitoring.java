package com.example;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;

public class CloudWatchMonitoring {

    private final AmazonCloudWatch cloudWatch;

    public CloudWatchMonitoring(String accessKey, String secretKey) {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        this.cloudWatch = AmazonCloudWatchClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public void publishCustomMetric(String metricName, double value) {
        MetricDatum datum = new MetricDatum()
                .withMetricName(metricName)
                .withUnit(StandardUnit.Count)
                .withValue(value);

        PutMetricDataRequest request = new PutMetricDataRequest()
                .withNamespace("MyAppNamespace")
                .withMetricData(datum);

        cloudWatch.putMetricData(request);
        System.out.println("Metric '" + metricName + "' published to CloudWatch.");
    }
}