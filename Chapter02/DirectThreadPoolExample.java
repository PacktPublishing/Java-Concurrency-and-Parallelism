import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DirectThreadPoolExample {
	public static void main(String[] args) {
		int corePoolSize = 2;
		int maxPoolSize = 4;
		long keepAliveTime = 5000;
		TimeUnit unit = TimeUnit.MILLISECONDS;
		ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(10);
		ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, unit, workQueue);
		executor.execute(() -> System.out.println("Task 1 executed"));
		executor.execute(() -> System.out.println("Task 2 executed"));
		executor.shutdown();
	}
}
