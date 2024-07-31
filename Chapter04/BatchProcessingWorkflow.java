package com.example;

import java.util.concurrent.CyclicBarrier;

public class BatchProcessingWorkflow {
    private final CyclicBarrier barrier;
    private final int batchSize = 5; // Number of parts in each batch

    public BatchProcessingWorkflow() {
        // Action to take when all threads reach the barrier
        Runnable barrierAction = () -> System.out.println("Batch stage completed. Proceeding to next stage.");
        this.barrier = new CyclicBarrier(batchSize, barrierAction);
    }

    public void processBatchPart(int partId) {
        try {
            System.out.println("Processing part " + partId);
            // Simulating time taken to process part of the batch
            Thread.sleep((long) (Math.random() * 1000));
            System.out.println("Part " + partId + " processed. Waiting at barrier.");

            // Wait for other parts to reach this point
            barrier.await();
            // After all parts reach the barrier, proceed with the next stage
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        BatchProcessingWorkflow workflow = new BatchProcessingWorkflow();
        // Simulating concurrent processing of batch parts
        for (int i = 0; i < workflow.batchSize; i++) {
            final int partId = i;
            new Thread(() -> workflow.processBatchPart(partId)).start();
        }
    }
}
