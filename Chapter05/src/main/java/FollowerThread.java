public class FollowerThread implements Runnable {
    private volatile boolean available = true; // Flag to indicate availability
    private Task currentTask;

    public boolean isAvailable() {
        return available;
    }

    public void assignTask(Task task) {
        this.available = false;
        this.currentTask = task;
        // Implement logic to execute the task (e.g., call task.execute())
    }

    @Override
    public void run() {
        while (true) {
            // Implement logic to check for assigned task and execute it
            if (currentTask != null) {
                currentTask.execute();
                currentTask = null;
                available = true; // Mark available after completing task
            }
            // Implement logic for idle state (e.g., wait for task assignment)
        }
    }
}
