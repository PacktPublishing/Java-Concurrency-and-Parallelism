package com.example;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ImageRenderingApp {

    public static void main(String[] args) {
        try (ForkJoinPool pool = new ForkJoinPool()) {
            try {
                RenderingTask downloadTextures = new RenderingTask("Download Textures", 2000);
                RenderingTask buildPrimitives = new RenderingTask("Build Geometric Primitives", 3000);
                RenderingTask applyLighting = new RenderingTask("Apply Lighting and Shadows", 1500);
                RenderingTask renderImage = new RenderingTask("Render Final Image", 2500);

                pool.execute(downloadTextures);
                downloadTextures.join(); // Wait for the first task to complete

                pool.execute(buildPrimitives);
                buildPrimitives.join(); // Wait for the second task to complete

                pool.execute(applyLighting);
                applyLighting.join(); // Wait for the third task to complete

                pool.execute(renderImage);
                renderImage.join(); // Wait for the final task to complete
            } finally {
                pool.shutdown();
            }
        }
    }

    // Define RenderingTask as a nested static class
    public static class RenderingTask extends RecursiveAction {
        private final String taskName;
        private final long duration;

        public RenderingTask(String taskName, long duration) {
            this.taskName = taskName;
            this.duration = duration;
        }

        @Override
        protected void compute() {
            try {
                System.out.println(taskName + " starting...");
                Thread.sleep(duration);
                System.out.println(taskName + " completed.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
