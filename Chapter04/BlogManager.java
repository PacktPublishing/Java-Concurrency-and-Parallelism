package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

public class BlogManager {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final StampedLock stampedLock = new StampedLock();
    private List<Map<String, Object>> comments = new ArrayList<>();

    // Method to read comments using ReadWriteLock for concurrent access
    public List<Map<String, Object>> getComments() {
        readWriteLock.readLock().lock();
        try {
            return Collections.unmodifiableList(comments);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    // Method to add a comment with StampedLock for efficient locking
    public void addComment(String author, String content, long timestamp) {
        long stamp = stampedLock.writeLock();
        try {
            Map<String, Object> comment = new HashMap<>();
            comment.put("author", author);
            comment.put("content", content);
            comment.put("timestamp", timestamp);
            comments.add(comment);
        } finally {
            stampedLock.unlock(stamp);
        }
    }
}
