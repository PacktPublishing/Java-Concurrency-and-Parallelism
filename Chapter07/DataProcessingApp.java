// Main class using DataNormalizer
package com.example;

import java.util.Arrays;

public class DataProcessingApp {
    public static void main(String[] args) {
        // Example 2D array of raw data
        double[][] rawData = new double[][] {
                { 2.5, 3.0 },
                { 1.0, 5.0 },
                { 4.0, 2.5 }
        };

        // Create an instance of DataNormalizer
        DataNormalizer normalizer = new DataNormalizer(rawData);

        // Normalize the data
        double[][] normalizedData = normalizer.normalizeData();

        // Further processing or output of normalized data
        for (int i = 0; i < normalizedData.length; i++) {
            System.out.println("Normalized Row " + (i + 1) + ": " + Arrays.toString(normalizedData[i]));
        }
    }
}
