package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification.S3EventNotificationRecord;

public class S3ObjectProcessor implements RequestHandler<S3Event, String> {

  @Override
  public String handleRequest(S3Event event, Context context) {
    // Get the first S3 record from the event
    S3EventNotificationRecord record = event.getRecords().get(0);

    // Extract the S3 object key from the record
    String objectKey = record.getS3().getObject().getKey();

    // Log the object key
    context.getLogger().log("S3 Object uploaded: " + objectKey);

    return "Object processed successfully: " + objectKey;
  }
}
