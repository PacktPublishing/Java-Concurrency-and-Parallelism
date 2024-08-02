package com.example;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class ImageFeatureExtractionTask extends RecursiveTask<Void> {
    private static final int THRESHOLD = 100; // Define THRESHOLD here
    private List<Image> imageBatch;

    public ImageFeatureExtractionTask(List<Image> imageBatch) {
        this.imageBatch = imageBatch;
    }

    @Override
    protected Void compute() {
        if (imageBatch.size() > THRESHOLD) {
            List<ImageFeatureExtractionTask> subtasks = createSubtasks();
            for (ImageFeatureExtractionTask subtask : subtasks) {
                subtask.fork();
            }
        } else {
            processBatch(imageBatch);
        }
        return null;
    }

    private List<ImageFeatureExtractionTask> createSubtasks() {
        List<ImageFeatureExtractionTask> subtasks = new ArrayList<>();

        // Assume we divide the imageBatch into two equal parts
        int mid = imageBatch.size() / 2;

        // Create new tasks for each half of the imageBatch
        ImageFeatureExtractionTask task1 = new ImageFeatureExtractionTask(imageBatch.subList(0, mid));
        ImageFeatureExtractionTask task2 = new ImageFeatureExtractionTask(imageBatch.subList(mid, imageBatch.size()));

        // Add the new tasks to the list of subtasks
        subtasks.add(task1);
        subtasks.add(task2);

        return subtasks;
    }

    private void processBatch(List<Image> batch) {
        // Perform feature extraction on the batch of images
    }
}
