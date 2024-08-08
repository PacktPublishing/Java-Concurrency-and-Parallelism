package com.example;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

public class S3CustomSpliteratorExample {

    public static void main(String[] args) {
        S3Client s3Client = S3Client.builder()
                .region(Region.US_WEST_2) // Specify your region
                .build();
        String bucketName = "your-bucket-name";
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();
        ListObjectsV2Response result = s3Client.listObjectsV2(listRequest);
        List<S3Object> objects = result.contents();

        Spliterator<S3Object> spliterator = new S3ObjectSpliterator(objects);

        StreamSupport.stream(spliterator, true)
                .forEach(S3CustomSpliteratorExample::processS3Object);
    }

    private static class S3ObjectSpliterator implements Spliterator<S3Object> {
        private final List<S3Object> s3Objects;
        private int current = 0;

        S3ObjectSpliterator(List<S3Object> s3Objects) {
            this.s3Objects = s3Objects;
        }

        @Override
        public boolean tryAdvance(Consumer<? super S3Object> action) {
            if (current < s3Objects.size()) {
                action.accept(s3Objects.get(current++));
                return true;
            }
            return false;
        }

        @Override
        public Spliterator<S3Object> trySplit() {
            int remaining = s3Objects.size() - current;
            int splitSize = remaining / 2;
            if (splitSize <= 1) {
                return null;
            }
            List<S3Object> splitPart = s3Objects.subList(current, current + splitSize);
            current += splitSize;
            return new S3ObjectSpliterator(splitPart);
        }

        @Override
        public long estimateSize() {
            return s3Objects.size() - current;
        }

        @Override
        public int characteristics() {
            return IMMUTABLE | SIZED | SUBSIZED;
        }
    }

    private static void processS3Object(S3Object object) {
        // Implement your processing logic for each S3 object here
        System.out.println("Processing object: " + object.key());
    }
}