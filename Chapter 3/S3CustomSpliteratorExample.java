// Assume s3Client is an initialized AmazonS3 client
public class S3CustomSpliteratorExample {

    public static void main(String[] args) {
        String bucketName = "your-bucket-name";
        ListObjectsV2Result result = s3Client.listObjectsV2(bucketName);
        List<S3ObjectSummary> objects = result.getObjectSummaries();

        Spliterator<S3ObjectSummary> spliterator = new S3ObjectSpliterator(objects);

        StreamSupport.stream(spliterator, true)
                .forEach(S3CustomSpliteratorExample::processS3Object);
    }

    private static class S3ObjectSpliterator implements Spliterator<S3ObjectSummary> {
        private final List<S3ObjectSummary> s3Objects;
        private int current = 0;

        S3ObjectSpliterator(List<S3ObjectSummary> s3Objects) {
            this.s3Objects = s3Objects;
        }

        @Override
        public boolean tryAdvance(Consumer<? super S3ObjectSummary> action) {
            if (current < s3Objects.size()) {
                action.accept(s3Objects.get(current++));
                return true;
            }
            return false;
        }

        @Override
        public Spliterator<S3ObjectSummary> trySplit() {
            int remaining = s3Objects.size() - current;
            int splitSize = remaining / 2;
            if (splitSize <= 1) {
                return null;
            }
            List<S3ObjectSummary> splitPart = s3Objects.subList(current, current + splitSize);
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

    private static void processS3Object(S3ObjectSummary objectSummary) {
        // Processing logic for each S3 object
    }
}
