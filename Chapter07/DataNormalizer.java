package com.example;

import java.util.Arrays;
import java.util.stream.IntStream;

public class DataNormalizer {
    private double[][] data;

    public DataNormalizer(double[][] data) {
        this.data = data;
    }

    public double[][] normalizeData() {
        int numFeatures = data[0].length;
        return IntStream.range(0, numFeatures)
                .parallel()
                .mapToObj(featureIndex -> {
                    double[] featureValues = getFeatureValues(featureIndex);
                    double minValue = Arrays.stream(featureValues).min().orElse(0.0);
                    double maxValue = Arrays.stream(featureValues).max().orElse(1.0);
                    return normalize(featureValues, minValue, maxValue);
                })
                .toArray(double[][]::new);
    }

    private double[] getFeatureValues(int featureIndex) {
        return Arrays.stream(data)
                .mapToDouble(row -> row[featureIndex])
                .toArray();
    }

    private double[] normalize(double[] values, double minValue, double maxValue) {
        return Arrays.stream(values)
                .map(value -> (value - minValue) / (maxValue - minValue))
                .toArray();
    }
}
