package com.example;

import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServer;

public class MonitoringApplication {
    private static JMXMonitoring.CustomMBean customMBean;
    private static CloudWatchMonitoring cloudWatchMonitor;

    public static void main(String[] args) {
        // Initialize CloudWatch monitoring
        cloudWatchMonitor = new CloudWatchMonitoring("your-access-key", "your-secret-key");

        // Initialize JMX monitoring
        setupJMXMonitoring();

        // Start periodic monitoring
        startPeriodicMonitoring();

        System.out.println("Monitoring systems initialized. Application running...");

        // Keep the application running
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void setupJMXMonitoring() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            customMBean = JMXMonitoring.createAndRegisterMBean(mbs);
            customMBean.setMetric(0); // Set initial metric value
            System.out.println("JMX Monitoring setup complete. Initial metric value: " + customMBean.getMetric());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startPeriodicMonitoring() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            try {
                // Simulate metric change
                int currentMetric = customMBean.getMetric();
                int newMetric = currentMetric + 1;
                customMBean.setMetric(newMetric);

                // Publish to CloudWatch
                cloudWatchMonitor.publishCustomMetric("JMXMetric", newMetric);

                System.out.println("Updated JMX metric: " + newMetric);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 60, TimeUnit.SECONDS); // Run every 60 seconds
    }
}
