package com.example;

import java.util.concurrent.RecursiveTask;

public class SumTask extends RecursiveTask<Integer> {

  private final int[] data;
  private final int start;
  private final int end;
  private static final int THRESHOLD = 50;

  public SumTask(int[] data, int start, int end) {
    this.data = data;
    this.start = start;
    this.end = end;
  }

  @Override
  protected Integer compute() {
    int length = end - start;
    if (length <= THRESHOLD) {
      // Base case: compute the sum sequentially
      int sum = 0;
      for (int i = start; i < end; i++) {
        sum += data[i];
      }
      return sum;
    } else {
      // Recursive case: split the task into two subtasks
      int mid = start + (length >> 1);
      SumTask left = new SumTask(data, start, mid);
      SumTask right = new SumTask(data, mid, end);

      // Execute subtasks concurrently using fork()
      left.fork();
      right.fork();

      // Join the subtasks and combine results
      return right.join() + left.join();
    }
  }
}
