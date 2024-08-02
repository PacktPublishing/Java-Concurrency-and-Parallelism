import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FeatureExtractor {
    private List<Document> documents;

    public FeatureExtractor(List<Document> documents) {
        this.documents = documents;
    }

    public List<Double[]> extractTfIdfFeatures() {
        return documents.parallelStream()
                .map(document -> {
                    String[] words = document.getContent().toLowerCase().split("\\s+");
                    return Arrays.stream(words)
                            .distinct()
                            .mapToDouble(word -> calculateTfIdf(word, document))
                            .boxed()
                            .toArray(Double[]::new);
                })
                .collect(Collectors.toList());
    }

    private double calculateTfIdf(String word, Document document) {
        double tf = calculateTermFrequency(word, document);
        double idf = calculateInverseDocumentFrequency(word);
        return tf * idf;
    }

    private double calculateTermFrequency(String word, Document document) {
        String[] words = document.getContent().toLowerCase().split("\\s+");
        long termCount = Arrays.stream(words)
                .filter(w -> w.equals(word))
                .count();
        return (double) termCount / words.length;
    }

    private double calculateInverseDocumentFrequency(String word) {
        long documentCount = documents.stream()
                .filter(document -> document.getContent().toLowerCase().contains(word))
                .count();
        return Math.log((double) documents.size() / (documentCount + 1));
    }
}

class Document {
    private String content;

    public Document(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
