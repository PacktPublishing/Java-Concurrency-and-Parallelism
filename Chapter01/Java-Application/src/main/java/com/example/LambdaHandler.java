package com.example;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;

public class LambdaHandler {
  public String handleRequest(Map<String, Object> event, Context context) {
    // Get data from event
    String message = (String) event.get("message");

    // Process data
    String result = "Processed message: " + message;

    // Return result
    return result;
  }
}
