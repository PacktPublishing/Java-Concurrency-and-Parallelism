import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class ThumbnailGenerator implements RequestHandler<SQSEvent, Void> {
    private static final S3Client s3Client = S3Client.builder()
            .region(Region.US_EAST_1) // Specify your region
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();
    private static final String bucketName = "your-bucket-name";
    private static final String thumbnailBucket = "your-thumbnail-bucket-name";

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        String imageKey = extractImageKey(event); // Assume this method extracts the image key from the SQSEvent

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Download from S3
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(imageKey)
                    .build();
            InputStream objectData = s3Client.getObject(getObjectRequest);

            // Load image
            BufferedImage image = ImageIO.read(objectData);

            // Resize (Maintain aspect ratio example)
            int targetWidth = 100;
            int targetHeight = (int) (image.getHeight() * targetWidth / (double) image.getWidth());
            BufferedImage resized = getScaledImage(image, targetWidth, targetHeight);

            // Save as JPEG
            ImageIO.write(resized, "jpg", outputStream);
            byte[] thumbnailBytes = outputStream.toByteArray();

            // Upload thumbnail to S3
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(thumbnailBucket)
                    .key(imageKey + "-thumbnail.jpg")
                    .build();
            s3Client.putObject(putObjectRequest,
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(thumbnailBytes));
        } catch (IOException e) {
            // Handle image processing errors
            e.printStackTrace();
        }

        return null;
    }

    // Helper method for resizing
    private BufferedImage getScaledImage(BufferedImage src, int w, int h) {
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = result.createGraphics();
        g2d.drawImage(src, 0, 0, w, h, null);
        g2d.dispose();
        return result;
    }

    private String extractImageKey(SQSEvent event) {
        // Implementation to extract the image key from the SQSEvent
        // Assuming only one record per event for simplicity
        return event.getRecords().get(0).getBody();
    }
}
