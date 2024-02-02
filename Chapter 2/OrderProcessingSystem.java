import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OrderProcessingSystem {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final ConcurrentLinkedQueue<Order> orderQueue = new ConcurrentLinkedQueue<>();
    private final CopyOnWriteArrayList<Order> processedOrders = new CopyOnWriteArrayList<>();
    private final ConcurrentHashMap<Integer, String> orderStatus = new ConcurrentHashMap<>();
    private final Lock paymentLock = new ReentrantLock();
    private final Semaphore validationSemaphore = new Semaphore(5);
    private final AtomicInteger processedCount = new AtomicInteger(0);

    public void startProcessing() {
        while (!orderQueue.isEmpty()) {
            Order order = orderQueue.poll();
            executorService.submit(() -> processOrder(order));
        }
    }

    private void processOrder(Order order) {
        try {
            validateOrder(order);
            paymentLock.lock();
            try {
                processPayment(order);
            } finally {
                paymentLock.unlock();
            }
            shipOrder(order);
            processedOrders.add(order);
            processedCount.incrementAndGet();
            orderStatus.put(order.getId(), "Completed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void validateOrder(Order order) throws InterruptedException {
        validationSemaphore.acquire();
        try {
            Thread.sleep(100);
        } finally {
            validationSemaphore.release();
        }
    }

    private void processPayment(Order order) {
    }

    private void shipOrder(Order order) {
    }

    public void placeOrder(Order order) {
        orderQueue.add(order);
        orderStatus.put(order.getId(), "Received");
        System.out.println("Order " + order.getId() + " placed.");
    }

    public static void main(String[] args) {
        OrderProcessingSystem system = new OrderProcessingSystem();
        for (int i = 0; i < 20; i++) {
            system.placeOrder(new Order(i));
        }
        system.startProcessing();
    }

    static class Order {
        private final int id;

        public Order(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
