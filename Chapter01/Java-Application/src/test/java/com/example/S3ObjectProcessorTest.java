package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;

public class S3ObjectProcessorTest {
    private S3ObjectProcessor s3ObjectProcessor;
    private Context context;

    @BeforeEach
    public void setUp() {
        s3ObjectProcessor = new S3ObjectProcessor();
        context = Mockito.mock(Context.class);
    }

    @Test
    public void testHandleRequest() {
        S3EventNotification.S3BucketEntity bucketEntity = new S3EventNotification.S3BucketEntity(
                "example-bucket", null, "arn:aws:s3:::example-bucket");

        S3EventNotification.S3ObjectEntity objectEntity = new S3EventNotification.S3ObjectEntity(
                "example-key", 1024L, null, null, null);

        S3EventNotification.S3Entity s3Entity = new S3EventNotification.S3Entity(
                "configurationId", bucketEntity, objectEntity, "1.0");

        S3EventNotification.UserIdentityEntity userIdentityEntity = new S3EventNotification.UserIdentityEntity(
                "AWS:123456789012");

        S3EventNotification.S3EventNotificationRecord record = new S3EventNotification.S3EventNotificationRecord(
                "us-east-1", "ObjectCreated:Put", "aws:s3", "2023-07-25T16:54:18.123Z", null,
                new S3EventNotification.RequestParametersEntity("127.0.0.1"),
                new S3EventNotification.ResponseElementsEntity("request-id", "id2"),
                s3Entity, userIdentityEntity);

        S3Event s3Event = new S3Event(Collections.singletonList(record));

        String result = s3ObjectProcessor.handleRequest(s3Event, context);
        assertEquals("Processing complete", result);
    }
}
