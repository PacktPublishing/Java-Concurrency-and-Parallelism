import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

public class ScatterGatherAWS {

    private static final LambdaClient lambdaClient = LambdaClient.builder()
            .region(Region.US_EAST_1) // Specify your region
            .credentialsProvider(DefaultCredentialsProvider.create())
            .overrideConfiguration(ClientOverrideConfiguration.builder().build())
            .build();

    private static final List<String> tasks = List.of("task1", "task2", "task3"); // Example tasks
    private static final String functionName = "your-lambda-function-name"; // Replace with your Lambda function name

    public static void main(String[] args) {
        // Scatter phase
        ExecutorService executor = Executors.newFixedThreadPool(tasks.size());
        List<Future<InvokeResponse>> futures = tasks.stream()
                .map(task -> (Callable<InvokeResponse>) () -> invokeLambda(task))
                .map(executor::submit)
                .collect(Collectors.toList());
        executor.shutdown();

        // Gather phase
        List<String> results = futures.stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (Exception e) {
                        // Handle error
                        System.err.println("Error invoking Lambda: " + e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(ScatterGatherAWS::processLambdaResult)
                .collect(Collectors.toList());

        // Store or process aggregated results
        results.forEach(System.out::println); // Example - print results
    }

    // Helper method to invoke a Lambda function
    private static InvokeResponse invokeLambda(String task) {
        String payload = "{ \"task\": \"" + task + "\" }"; // Example payload
        InvokeRequest invokeRequest = InvokeRequest.builder()
                .functionName(functionName)
                .payload(SdkBytes.fromUtf8String(payload))
                .build();
        return lambdaClient.invoke(invokeRequest);
    }

    // Helper method to process the result from a Lambda function
    private static String processLambdaResult(InvokeResponse result) {
        return result.payload().asUtf8String();
    }
}
