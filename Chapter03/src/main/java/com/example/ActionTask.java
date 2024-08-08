package com.example;

import java.util.concurrent.RecursiveAction;

public class ActionTask extends RecursiveAction {
    private final int[] data;
    private final int start, end;
    private static final int THRESHOLD = 50;

    public ActionTask(int[] data, int start, int end) {
        this.data = data;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        int length = end - start;
        if (length <= THRESHOLD) {
            // Base case: perform the action sequentially
            for (int i = start; i < end; i++) {
                // Perform action on data[i]
            }
        } else {
            // Recursive case: split the task into two subtasks
            int mid = start + (length / 2);
            ActionTask left = new ActionTask(data, start, mid);
            ActionTask right = new ActionTask(data, mid, end);
            invokeAll(left, right);
        }
    }
}