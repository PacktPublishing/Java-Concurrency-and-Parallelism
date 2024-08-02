package com.example;

import java.util.Arrays;
import java.util.List;

public class TextProcessingApp {
    public static void main(String[] args) {
        // Example documents
        List<Document> documents = Arrays.asList(
                new Document("Java concurrency and parallelism are vital for cloud-native development."),
                new Document(
                        "Concurrency in Java focuses on task management, while parallelism emphasizes simultaneous execution."),
                new Document(
                        "Java's concurrency is ideal for handling multiple tasks, while parallelism suits multi-core processing."));

        // Create an instance of FeatureExtractor
        FeatureExtractor extractor = new FeatureExtractor(documents);

        // Extract TF-IDF features
        List<Double[]> tfidfFeatures = extractor.extractTfIdfFeatures();

        // Output features for each document
        for (int i = 0; i < tfidfFeatures.size(); i++) {
            System.out.println("Document " + (i + 1) + " TF-IDF Features: " + Arrays.toString(tfidfFeatures.get(i)));
        }
    }
}
