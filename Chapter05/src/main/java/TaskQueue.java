import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskQueue {
  private final BlockingQueue<Task> tasks;

  public TaskQueue() {
    this.tasks = new LinkedBlockingQueue<>(); // Example using LinkedBlockingQueue
  }

  public void addTask(Task task) throws InterruptedException {
    tasks.put(task);
  }

  public Task getTask() throws InterruptedException {
    return tasks.take();
  }
}