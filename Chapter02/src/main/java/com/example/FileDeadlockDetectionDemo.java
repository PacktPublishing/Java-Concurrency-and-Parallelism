package com.example;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class FileDeadlockDetectionDemo {
    private static final ReentrantLock fileLock1 = new ReentrantLock();
    private static final ReentrantLock fileLock2 = new ReentrantLock();

    public static void main(String[] args) {
        Thread process1 = new Thread(() -> {
            try {
                acquireFileLocksWithTimeout(fileLock1, fileLock2);
            } catch (InterruptedException e) {
                if (fileLock1.isHeldByCurrentThread())
                    fileLock1.unlock();
                if (fileLock2.isHeldByCurrentThread())
                    fileLock2.unlock();
            }
        });
        Thread process2 = new Thread(() -> {
            try {
                acquireFileLocksWithTimeout(fileLock2, fileLock1);
            } catch (InterruptedException e) {
                if (fileLock1.isHeldByCurrentThread())
                    fileLock1.unlock();
                if (fileLock2.isHeldByCurrentThread())
                    fileLock2.unlock();
            }
        });
        process1.start();
        process2.start();
        try {
            Thread.sleep(2000);
            if (process1.isAlive() && process2.isAlive()) {
                System.out.println("Deadlock suspected, interrupting process 2");
                process2.interrupt();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void acquireFileLocksWithTimeout(ReentrantLock firstFileLock, ReentrantLock secondFileLock)
            throws InterruptedException {
        if (!firstFileLock.tryLock(1000, TimeUnit.MILLISECONDS)) {
            throw new InterruptedException("Failed to acquire first file lock");
        }
        try {
            if (!secondFileLock.tryLock(1000, TimeUnit.MILLISECONDS)) {
                throw new InterruptedException("Failed to acquire second file lock");
            }
            System.out.println(Thread.currentThread().getName() + " acquired both file locks");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        } finally {
            if (secondFileLock.isHeldByCurrentThread())
                secondFileLock.unlock();
            if (firstFileLock.isHeldByCurrentThread())
                firstFileLock.unlock();
        }
    }
}
