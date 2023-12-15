
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcurrentKitchen {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<?> task1 = executor.submit(() -> {
            System.out.println("Chopping vegetables...");
            // Simulate task
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        try {
            task1.get(); // Add this line to ensure the task is executed
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            // Handle the exception appropriately
        }

        Future<?> task2 = executor.submit(() -> {
            System.out.println("Grilling meat...");
            // Simulate task
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        try {
            task2.get(); // Add this line to ensure the task is executed
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            // Handle the exception appropriately
        }

        executor.shutdown();
    }
}

