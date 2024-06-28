package com.example;

public class MatrixMultiplicationComparison {
    public static void main(String[] args) {
        int size = 1000; // Size of the square matrices
        float[][] a = generateRandomMatrix(size, size);
        float[][] b = generateRandomMatrix(size, size);

        // CPU multiplication
        long startTimeCPU = System.currentTimeMillis();
        float[][] resultCPU = MatrixMultiplication.multiplyMatricesCPU(a, b);
        long endTimeCPU = System.currentTimeMillis();
        System.out.println("CPU time: " + (endTimeCPU - startTimeCPU) + " ms");

        // GPU multiplication
        long startTimeGPU = System.currentTimeMillis();
        float[][] resultGPU = MatrixMultiplicationGPU.multiplyMatricesGPU(a, b);
        long endTimeGPU = System.currentTimeMillis();
        System.out.println("GPU time: " + (endTimeGPU - startTimeGPU) + " ms");

        // Verify results
        boolean correct = verifyResults(resultCPU, resultGPU);
        System.out.println("Results are correct: " + correct);
    }

    private static float[][] generateRandomMatrix(int rows, int cols) {
        float[][] matrix = new float[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = (float) Math.random();
            }
        }
        return matrix;
    }

    private static boolean verifyResults(float[][] a, float[][] b) {
        if (a.length != b.length || a[0].length != b[0].length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                if (Math.abs(a[i][j] - b[i][j]) > 1e-5) {
                    return false;
                }
            }
        }
        return true;
    }
}
