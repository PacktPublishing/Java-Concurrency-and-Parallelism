package com.example;

public class Transaction {
    private String transactionId;
    private double amount;
    private long timestamp;

    public Transaction(String transactionId, double amount, long timestamp) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.timestamp = timestamp; // set the timestamp
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
