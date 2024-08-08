import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

// Event Class
class StockPriceEvent {
    String symbol;
    long timestamp;
    double price;

    // Getters and setters (optional)
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

// Sample Calculation Consumer (Moving Average)
class MovingAverageCalculator implements EventHandler<StockPriceEvent> {
    private double average; // Maintain moving average state

    @Override
    public void onEvent(StockPriceEvent event, long sequence, boolean endOfBatch) {
        average = (average * (sequence) + event.getPrice()) / (sequence + 1);
        // Perform additional calculations or store the average
        System.out.println("Moving average for " + event.getSymbol() + ": " + average);
    }
}

public class DisruptorExample {

    public static void main(String[] args) {
        // Disruptor configuration
        int bufferSize = 1024; // Adjust based on expected event volume
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        ProducerType producerType = ProducerType.MULTI; // Allow multiple producers
        BlockingWaitStrategy waitStrategy = new BlockingWaitStrategy(); // Blocking wait for full buffers

        // Event factory to create events
        EventFactory<StockPriceEvent> eventFactory = StockPriceEvent::new;

        // Create Disruptor
        Disruptor<StockPriceEvent> disruptor = new Disruptor<>(eventFactory, bufferSize, threadFactory, producerType,
                waitStrategy);

        // Add consumer (MovingAverageCalculator)
        disruptor.handleEventsWith(new MovingAverageCalculator());

        // Start Disruptor
        disruptor.start();

        // Simulate producers publishing events (replace with your actual data source)
        for (int i = 0; i < 100; i++) {
            disruptor.publishEvent((event, sequence) -> {
                event.setSymbol("AAPL");
                event.setTimestamp(System.currentTimeMillis());
                event.setPrice(100.0 + Math.random() * 10); // Simulate random price fluctuations
            });
        }

        // Shutdown Disruptor (optional)
        disruptor.shutdown();
    }
}
