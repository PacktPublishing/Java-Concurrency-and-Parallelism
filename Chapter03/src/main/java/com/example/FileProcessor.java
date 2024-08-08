package com.example;

import java.io.File;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class FileProcessor extends RecursiveAction {

  private final File file;

  public FileProcessor(File file) {
    this.file = file;
  }

  @Override
  protected void compute() {
    if (file.isDirectory()) {
      File[] files = file.listFiles();
      if (files != null) {
        for (File f : files) {
          FileProcessor processor = new FileProcessor(f);
          processor.fork(); // Submit subtask for parallel processing
        }
      }
    } else {
      // Base Case 2: File - Simulate processing the file (e.g., printing its name)
      System.out.println("Processing file: " + file.getName());
    }
  }

  public static void main(String[] args) {
    // Replace with the path to your directory
    String rootDir = "/path/to/your/directory";
    ForkJoinPool pool = new ForkJoinPool();
    try {
      FileProcessor processor = new FileProcessor(new File(rootDir));
      pool.invoke(processor);
    } finally {
      pool.shutdown();
    }
  }
}