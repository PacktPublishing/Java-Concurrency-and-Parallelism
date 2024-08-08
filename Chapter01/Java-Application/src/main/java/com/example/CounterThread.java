package com.example;

class SynchronizedCounter {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }
}

public class CounterThread extends Thread {
    private SynchronizedCounter counter;

    public CounterThread(SynchronizedCounter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            counter.increment();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedCounter counter = new SynchronizedCounter();
        CounterThread thread1 = new CounterThread(counter);
        CounterThread thread2 = new CounterThread(counter);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("Final count: " + counter.getCount());
    }
}
