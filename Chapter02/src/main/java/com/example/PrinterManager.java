package com.example;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PrinterManager {
    private final ReentrantLock printerLock = new ReentrantLock();
    private final Condition readyCondition = printerLock.newCondition();
    private boolean isPrinterReady = false;

    public void makePrinterReady() {
        printerLock.lock();
        try {
            isPrinterReady = true;
            readyCondition.signal(); // Signal one waiting thread that the printer is ready
        } finally {
            printerLock.unlock();
        }
    }

    public void printDocument(String document) {
        printerLock.lock();
        try {
            // Wait until the printer is ready
            while (!isPrinterReady) {
                System.out.println(Thread.currentThread().getName() + " waiting for the printer to be ready.");
                if (!readyCondition.await(2000, TimeUnit.MILLISECONDS)) {
                    System.out.println(Thread.currentThread().getName()
                            + " could not print. Timeout while waiting for the printer to be ready.");
                    return;
                }
            }
            // Printer is ready. Proceed to print the document
            System.out.println(Thread.currentThread().getName() + " is printing: " + document);
            Thread.sleep(1000); // Simulates printing time

            // Reset the printer readiness for demonstration purposes
            isPrinterReady = false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            printerLock.unlock();
        }
    }

    public static void main(String[] args) {
        PrinterManager printerManager = new PrinterManager();
        // Simulating multiple threads (office workers) trying to use the printer
        Thread worker1 = new Thread(() -> printerManager.printDocument("Document1"), "Worker1");
        Thread worker2 = new Thread(() -> printerManager.printDocument("Document2"), "Worker2");
        Thread worker3 = new Thread(() -> printerManager.printDocument("Document3"), "Worker3");
        worker1.start();
        worker2.start();
        worker3.start();
        // Simulate making the printer ready after a delay
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulate some delay
                printerManager.makePrinterReady();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
