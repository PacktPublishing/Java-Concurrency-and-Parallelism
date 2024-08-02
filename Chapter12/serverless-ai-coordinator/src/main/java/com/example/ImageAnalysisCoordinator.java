package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.ContainerOverride;
import software.amazon.awssdk.services.ecs.model.KeyValuePair;
import software.amazon.awssdk.services.ecs.model.RunTaskRequest;
import software.amazon.awssdk.services.ecs.model.TaskOverride;
import software.amazon.awssdk.services.s3.S3Client;

@ApplicationScoped
public class ImageAnalysisCoordinator implements RequestHandler<S3Event, String> {

        @Inject
        EcsClient ecsClient;

        @Inject
        S3Client s3Client;

        @Override
        public String handleRequest(S3Event s3Event, Context context) {
                String bucket = s3Event.getRecords().get(0).getS3().getBucket().getName();
                String key = s3Event.getRecords().get(0).getS3().getObject().getKey();

                RunTaskRequest runTaskRequest = RunTaskRequest.builder()
                                .cluster("your-fargate-cluster")
                                .taskDefinition("your-task-definition")
                                .launchType("FARGATE")
                                .overrides(TaskOverride.builder()
                                                .containerOverrides(
                                                                ContainerOverride.builder()
                                                                                .name("your-container-name")
                                                                                .environment(
                                                                                                KeyValuePair.builder()
                                                                                                                .name("BUCKET")
                                                                                                                .value(bucket)
                                                                                                                .build(),
                                                                                                KeyValuePair.builder()
                                                                                                                .name("KEY")
                                                                                                                .value(key)
                                                                                                                .build())
                                                                                .build())
                                                .build())
                                .build();

                try {
                        ecsClient.runTask(runTaskRequest);
                        return "Fargate task launched for image analysis: " + bucket + "/" + key;
                } catch (Exception e) {
                        context.getLogger().log("Error launching Fargate task: " + e.getMessage());
                        return "Error launching Fargate task";
                }
        }
}
