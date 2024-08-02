package com.example;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class MicroservicesTaskParallelism {
    private static final Logger logger = LoggerFactory.getLogger(MicroservicesTaskParallelism.class);

    public static void main(String[] args) {
        SpringApplication.run(MicroservicesTaskParallelism.class, args);
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Set thread pool size
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("OrderTask-");
        executor.initialize();
        return executor;
    }

    @Async
    public void processOrder(String order) {
        logger.info("Processing " + order); // Use logger for consistent output
        // Additional order processing logic here
    }

    @Scheduled(fixedRate = 5000)
    public void scheduleTasks() {
        processOrder("Order 1");
        processOrder("Order 2");
        processOrder("Order 3");
    }
}
