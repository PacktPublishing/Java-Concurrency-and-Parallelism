package com.example;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

public class ScatterGatherAWS {
    private static final AWSLambda lambdaClient = AWSLambdaClientBuilder.defaultClient();

    public static void main(String[] args) {
        // Assuming 'tasks' is a List<String> containing Lambda function parameters
        List<String> tasks = Arrays.asList("task1", "task2", "task3"); // Example tasks

        // Scatter phase
        ExecutorService executor = Executors.newFixedThreadPool(tasks.size());
        List<Future<InvokeResult>> futures = tasks.stream()
                .map(task -> (Callable<InvokeResult>) () -> invokeLambda(task))
                .map(executor::submit)
                .collect(Collectors.toList());
        executor.shutdown();

        // Ensure termination of executor
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Gather phase
        List<String> results = futures.stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (InterruptedException | ExecutionException e) {
                        // Handle error more gracefully in production code
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(ScatterGatherAWS::processLambdaResult)
                .collect(Collectors.toList());

        // Example usage of results
        results.forEach(System.out::println);
    }

    private static InvokeResult invokeLambda(String task) {
        // Dummy implementation for the invokeLambda method
        InvokeRequest invokeRequest = new InvokeRequest()
                .withFunctionName("YourLambdaFunctionName")
                .withPayload(task);
        return lambdaClient.invoke(invokeRequest);
    }

    private static String processLambdaResult(InvokeResult result) {
        // Assume the result.getPayload() is ByteBuffer which is non-null
        return new String(result.getPayload().array(), StandardCharsets.UTF_8);
    }
}
