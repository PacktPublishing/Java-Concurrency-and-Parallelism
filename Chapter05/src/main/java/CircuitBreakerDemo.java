import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class CircuitBreakerDemo {
    private enum State {
        CLOSED, OPEN, HALF_OPEN
    }

    private final int maxFailures;
    private final Duration openDuration;
    private final Duration retryDuration;
    private final Supplier<Boolean> service;
    private State state;
    private AtomicInteger failureCount;
    private Instant lastFailureTime;
    private Instant lastRetryTime;

    public CircuitBreakerDemo(int maxFailures, Duration openDuration, Duration retryDuration,
            Supplier<Boolean> service) {
        this.maxFailures = maxFailures;
        this.openDuration = openDuration;
        this.retryDuration = retryDuration;
        this.service = service;
        this.state = State.CLOSED;
        this.failureCount = new AtomicInteger(0);
    }

    public boolean call() {
        switch (state) {
            case CLOSED:
                return callService();
            case OPEN:
                if (lastFailureTime.plus(openDuration).isBefore(Instant.now())) {
                    lastRetryTime = Instant.now();
                    state = State.HALF_OPEN;
                }
                return false;
            case HALF_OPEN:
                if (lastRetryTime.plus(retryDuration).isBefore(Instant.now())) {
                    boolean result = callService();
                    if (result) {
                        state = State.CLOSED;
                        failureCount.set(0);
                    } else {
                        state = State.OPEN;
                        lastFailureTime = Instant.now();
                    }
                    return result;
                }
                return false;
            default:
                throw new IllegalStateException("Unexpected state: " + state);
        }
    }

    private boolean callService() {
        try {
            boolean result = service.get();
            if (!result) {
                handleFailure();
            } else {
                failureCount.set(0);
            }
            return result;
        } catch (Exception e) {
            handleFailure();
            return false;
        }
    }

    private void handleFailure() {
        int currentFailures = failureCount.incrementAndGet();
        if (currentFailures >= maxFailures) {
            state = State.OPEN;
            lastFailureTime = Instant.now();
        }
    }

    public static void main(String[] args) {
        // Simulated service that randomly fails
        Supplier<Boolean> service = () -> new Random().nextBoolean();

        // Create a CircuitBreaker instance
        CircuitBreakerDemo circuitBreaker = new CircuitBreakerDemo(3, Duration.ofSeconds(10), Duration.ofSeconds(5),
                service);

        // Simulate calls to the service
        for (int i = 0; i < 20; i++) {
            boolean result = circuitBreaker.call();
            System.out.println("Call " + i + ": " + result);
            try {
                Thread.sleep(1000); // Simulate some delay between calls
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
