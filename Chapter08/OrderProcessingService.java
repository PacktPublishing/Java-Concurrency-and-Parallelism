package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class OrderProcessingService {

    @Autowired
    private OrderValidationService orderValidationService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ExecutorService executorService; // Injected ExecutorService bean

    @Async
    public void processOrder(Order order) throws Exception {
        System.out.println("Processing order: " + order.getId());

        if (!orderValidationService.validateOrder(order)) {
            throw new InvalidOrderException("Order validation failed");
        }

        // Concurrent processing tasks
        List<Future<?>> tasks = new ArrayList<>();

        tasks.add(executorService.submit(() -> inventoryService.deductProductQuantity(order)));
        tasks.add(executorService.submit(() -> invoiceService.generateInvoice(order)));
        tasks.add(executorService.submit(() -> emailService.sendOrderConfirmation(order)));

        // Wait for all tasks to complete
        for (Future<?> task : tasks) {
            try {
                task.get(); // Wait for each task to finish
            } catch (Exception e) {
                System.err.println("Error processing order: " + order.getId());
                throw e; // Rethrow exception after logging
            }
        }

        System.out.println("Order processed successfully: " + order.getId());
    }

    @Bean
    public ExecutorService executorService() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.initialize();
        return executor.getThreadPoolExecutor();
    }

    public static void main(String[] args) {
        SpringApplication.run(OrderProcessingService.class, args);
    }
}
