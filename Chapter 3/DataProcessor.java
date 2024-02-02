import java.util.concurrent.RecursiveTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class DataProcessor {

    public static void main(String[] args) {
        // Example dataset
        int[] data = { /* large dataset */ };

        ForkJoinPool pool = new ForkJoinPool();

        // RecursiveTask for summing data
        SumTask sumTask = new SumTask(data, 0, data.length);
        int result = pool.invoke(sumTask);
        System.out.println("Total sum: " + result);

        // RecursiveAction for processing data
        ActionTask actionTask = new ActionTask(data, 0, data.length);
        pool.invoke(actionTask);

        pool.shutdown();
    }

    // Splitting task for parallel execution
    static class SumTask extends RecursiveTask<Integer> {
        private final int[] data;
        private final int start, end;
        private static final int THRESHOLD = 100;

        SumTask(int[] data, int start, int end) {
            this.data = data;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            int length = end - start;
            if (length <= THRESHOLD) {
                // Simple computation
                int sum = 0;
                for (int i = start; i < end; i++) {
                    sum += data[i];
                }
                return sum;
            } else {
                // Split task
                int mid = start + (length / 2);
                SumTask left = new SumTask(data, start, mid);
                SumTask right = new SumTask(data, mid, end);
                left.fork();
                return right.compute() + left.join();
            }
        }
    }

    static class ActionTask extends RecursiveAction {
        private final int[] data;
        private final int start, end;
        private static final int THRESHOLD = 100;

        ActionTask(int[] data, int start, int end) {
            this.data = data;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            int length = end - start;
            if (length <= THRESHOLD) {
                // Simple processing
                for (int i = start; i < end; i++) {
                    // Process data[i] - e.g., apply a transformation
                }
            } else {
                // Split task
                int mid = start + (length / 2);
                ActionTask left = new ActionTask(data, start, mid);
                ActionTask right = new ActionTask(data, mid, end);
                invokeAll(left, right);
            }
        }
    }
}
