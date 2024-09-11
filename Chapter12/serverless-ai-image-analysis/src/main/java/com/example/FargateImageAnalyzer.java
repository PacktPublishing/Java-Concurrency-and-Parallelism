package com.example;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.Attribute;
import software.amazon.awssdk.services.rekognition.model.DetectFacesRequest;
import software.amazon.awssdk.services.rekognition.model.DetectFacesResponse;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.S3Object;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@QuarkusMain
@ApplicationScoped
public class FargateImageAnalyzer implements QuarkusApplication {

    @Inject
    S3Client s3Client;

    @Inject
    RekognitionClient rekognitionClient;

    @Override
    public int run(String... args) throws Exception {
        String bucket = System.getProperty("IMAGE_BUCKET");
        String key = System.getProperty("IMAGE_KEY");

        try {
            DetectLabelsRequest labelsRequest = DetectLabelsRequest.builder()
                    .image(Image.builder().s3Object(S3Object.builder().bucket(bucket).name(key).build()).build())
                    .maxLabels(10)
                    .minConfidence(75F)
                    .build();

            DetectLabelsResponse labelsResult = rekognitionClient.detectLabels(labelsRequest);

            DetectFacesRequest facesRequest = DetectFacesRequest.builder()
                    .image(Image.builder().s3Object(S3Object.builder().bucket(bucket).name(key).build()).build())
                    .attributes(Attribute.ALL)
                    .build();

            DetectFacesResponse facesResult = rekognitionClient.detectFaces(facesRequest);

            String analysisResult = generateAnalysisResult(labelsResult, facesResult);

            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key + "_detailed_analysis.json")
                    .build(),
                    RequestBody.fromString(analysisResult));

        } catch (Exception e) {
            System.err.println("Error processing image: " + e.getMessage());
            return 1;
        }

        return 0;
    }

    private String generateAnalysisResult(DetectLabelsResponse labelsResult, DetectFacesResponse facesResult) {
        // Implement result generation logic
        return "Analysis result";
    }
}