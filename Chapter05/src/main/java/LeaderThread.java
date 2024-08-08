
public class LeaderThread implements Runnable {
    private final TaskQueue taskQueue;
    private final FollowerThread[] followers;

    public LeaderThread(TaskQueue taskQueue, FollowerThread[] followers) {
        this.taskQueue = taskQueue;
        this.followers = followers;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Task task = taskQueue.getTask();
                FollowerThread availableFollower = findAvailableFollower();
                if (availableFollower != null) {
                    availableFollower.assignTask(task);
                } else {
                    // Handle case where no follower is available (optional)
                    // You can implement strategies like retrying, logging, or waiting for a
                    // follower to become available.
                }
            } catch (InterruptedException e) {
                // Handle interruption gracefully (e.g., stop the thread)
            } finally {
                // elect a new leader in case the current leader fails (optional)
                // This ensures continuous operation even if the leader thread crashes.
            }
        }
    }

    private FollowerThread findAvailableFollower() {
        for (FollowerThread follower : followers) {
            if (follower.isAvailable()) {
                return follower;
            }
        }
        return null;
    }
}
