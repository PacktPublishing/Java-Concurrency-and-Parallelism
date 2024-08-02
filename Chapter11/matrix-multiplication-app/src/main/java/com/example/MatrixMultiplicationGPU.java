package com.example;

import jcuda.*;
import jcuda.jcublas.*;

public class MatrixMultiplicationGPU {
    public static float[][] multiplyMatricesGPU(float[][] a, float[][] b) {
        int m = a.length;
        int n = a[0].length;
        int p = b[0].length;

        // Initialize JCublas
        JCublas.cublasInit();

        // Allocate memory on GPU
        Pointer d_A = new Pointer();
        Pointer d_B = new Pointer();
        Pointer d_C = new Pointer();

        JCublas.cublasAlloc(m * n, Sizeof.FLOAT, d_A);
        JCublas.cublasAlloc(n * p, Sizeof.FLOAT, d_B);
        JCublas.cublasAlloc(m * p, Sizeof.FLOAT, d_C);

        // Copy data to GPU
        JCublas.cublasSetVector(m * n, Sizeof.FLOAT, Pointer.to(flattenMatrix(a)), 1, d_A, 1);
        JCublas.cublasSetVector(n * p, Sizeof.FLOAT, Pointer.to(flattenMatrix(b)), 1, d_B, 1);

        // Perform matrix multiplication
        JCublas.cublasSgemm('n', 'n', m, p, n, 1.0f,
                d_A, m, d_B, n, 0.0f, d_C, m);

        // Copy result back to CPU
        float[] resultFlat = new float[m * p];
        JCublas.cublasGetVector(m * p, Sizeof.FLOAT, d_C, 1, Pointer.to(resultFlat), 1);

        // Free GPU memory
        JCublas.cublasFree(d_A);
        JCublas.cublasFree(d_B);
        JCublas.cublasFree(d_C);

        // Shutdown JCublas
        JCublas.cublasShutdown();

        return unflattenMatrix(resultFlat, m, p);
    }

    private static float[] flattenMatrix(float[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        float[] flattened = new float[m * n];
        for (int i = 0; i < m; i++) {
            System.arraycopy(matrix[i], 0, flattened, i * n, n);
        }
        return flattened;
    }

    private static float[][] unflattenMatrix(float[] flattened, int m, int p) {
        float[][] result = new float[m][p];
        for (int i = 0; i < m; i++) {
            System.arraycopy(flattened, i * p, result[i], 0, p);
        }
        return result;
    }
}
