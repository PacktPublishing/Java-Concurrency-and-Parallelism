package com.example;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

// Event Class
class StockPriceEvent {
    String symbol;
    long timestamp;
    double price;

    public void setPrice(double price) {
        this.price = price;
    }

    // Getters and setters (optional)
}

// Sample Calculation Consumer (Moving Average)
class MovingAverageCalculator implements EventHandler<StockPriceEvent> {
    private double average; // Maintain moving average state

    @Override
    public void onEvent(StockPriceEvent event, long sequence, boolean endOfBatch) throws Exception {
        average = (average * sequence + event.price) / (sequence + 1);
        // Perform additional calculations or store the average
        System.out.println("Moving average for " + event.symbol + ": " + average);
    }
}

public class DisruptorExample {
    public static void main(String[] args) {
        // Disruptor configuration
        int bufferSize = 1024; // Adjust based on expected event volume
        ProducerType producerType = ProducerType.MULTI; // Allow multiple producers
        WaitStrategy waitStrategy = new BlockingWaitStrategy(); // Blocking wait for full buffers

        // Create Disruptor
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        Disruptor<StockPriceEvent> disruptor = new Disruptor<>(
                StockPriceEvent::new,
                bufferSize,
                threadFactory,
                producerType,
                waitStrategy);

        // Add consumer (MovingAverageCalculator)
        disruptor.handleEventsWith(new MovingAverageCalculator());

        // Start Disruptor
        disruptor.start();

        // Simulate producers publishing events (replace with your actual data source)
        for (int i = 0; i < 100; i++) {
            StockPriceEvent newEvent = new StockPriceEvent();
            newEvent.symbol = "AAPL";
            newEvent.timestamp = System.currentTimeMillis();
            newEvent.price = 100.0 + Math.random() * 10; // Simulate random price fluctuations

            disruptor.publishEvent((event, sequence) -> {
                event.setPrice(newEvent.price);
                event.symbol = newEvent.symbol;
                event.timestamp = newEvent.timestamp;
            });
        }

        // Shutdown Disruptor (optional)
        disruptor.shutdown();
    }
}