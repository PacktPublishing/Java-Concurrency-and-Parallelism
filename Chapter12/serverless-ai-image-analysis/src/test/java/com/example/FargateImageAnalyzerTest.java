package com.example;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectFacesRequest;
import software.amazon.awssdk.services.rekognition.model.DetectFacesResponse;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class FargateImageAnalyzerTest {

    private S3Client mockS3Client;
    private RekognitionClient mockRekognitionClient;
    private FargateImageAnalyzer analyzer;

    @BeforeEach
    public void setup() {
        mockS3Client = Mockito.mock(S3Client.class);
        mockRekognitionClient = Mockito.mock(RekognitionClient.class);
        analyzer = new FargateImageAnalyzer();
        analyzer.s3Client = mockS3Client;
        analyzer.rekognitionClient = mockRekognitionClient;

        // Set system properties
        System.setProperty("IMAGE_BUCKET", "test-bucket");
        System.setProperty("IMAGE_KEY", "test-key");

        // Mock responses from rekognitionClient
        when(mockRekognitionClient.detectLabels(any(DetectLabelsRequest.class)))
                .thenReturn(DetectLabelsResponse.builder().build());
        when(mockRekognitionClient.detectFaces(any(DetectFacesRequest.class)))
                .thenReturn(DetectFacesResponse.builder().build());
    }

    @Test
    public void testPutObject() {
        // Call the method that uses s3Client.putObject(...)
        try {
            analyzer.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Verify that the putObject method was called with the correct parameters
        verify(mockS3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }
}
