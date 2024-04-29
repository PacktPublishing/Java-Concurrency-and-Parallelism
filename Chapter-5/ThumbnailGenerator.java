package com.example;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;

public class ThumbnailGenerator implements RequestHandler<SQSEvent, Void> {
    private static final AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
    private static final String bucketName = "your-bucket-name";
    private static final String thumbnailBucket = "your-thumbnail-bucket-name";

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        String imageKey = extractImageKey(event); // Assume this method extracts the image key from the SQSEvent
        byte[] thumbnailBytes = null; // Declare outside the try block to be accessible in the catch block
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Download from S3
            S3Object s3Object = s3Client.getObject(bucketName, imageKey);
            InputStream objectData = s3Object.getObjectContent();
            // Load image
            BufferedImage image = ImageIO.read(objectData);
            // Resize (Maintain aspect ratio example)
            int targetWidth = 100;
            int targetHeight = (int) (image.getHeight() * targetWidth / (double) image.getWidth());
            BufferedImage resized = getScaledImage(image, targetWidth, targetHeight);

            // Save as JPEG
            ImageIO.write(resized, "jpg", outputStream);
            thumbnailBytes = outputStream.toByteArray();
            // Upload thumbnail to S3
            s3Client.putObject(thumbnailBucket, imageKey + "-thumbnail.jpg", new ByteArrayInputStream(thumbnailBytes),
                    null);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper method for resizing
    private BufferedImage getScaledImage(BufferedImage src, int w, int h) {
        BufferedImage result = new BufferedImage(w, h, src.getType());
        Graphics2D g2d = result.createGraphics();
        g2d.drawImage(src, 0, 0, w, h, null);
        g2d.dispose();
        return result;
    }

    private String extractImageKey(SQSEvent event) {
        // Implementation to extract the image key from the SQSEvent
        return "image-key";
    }

    public static void main(String[] args) {
        // For local testing
        SQSEvent event = new SQSEvent();
        new ThumbnailGenerator().handleRequest(event, null);
    }
}
