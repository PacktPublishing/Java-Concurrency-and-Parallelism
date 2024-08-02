
package com.example;

public class MatrixMultiplication {
    public static float[][] multiplyMatricesCPU(float[][] a, float[][] b) {
        int m = a.length;
        int n = a[0].length;
        int p = b[0].length;
        float[][] result = new float[m][p];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < p; j++) {
                for (int k = 0; k < n; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }
}
