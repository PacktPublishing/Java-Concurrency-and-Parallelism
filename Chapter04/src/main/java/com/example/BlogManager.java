package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

public class BlogManager {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final StampedLock stampedLock = new StampedLock();
    private List<Map<String, Object>> comments = new ArrayList<>();

    // Default constructor
    public BlogManager() {
    }

    // Parameterized constructor
    public BlogManager(List<Map<String, Object>> comments) {
        this.comments = comments;
    }

    // Getters and setters
    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }

    public StampedLock getStampedLock() {
        return this.stampedLock;
    }

    public List<Map<String, Object>> getComments() {
        readWriteLock.readLock().lock();
        try {
            return Collections.unmodifiableList(comments);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void setComments(List<Map<String, Object>> comments) {
        this.comments = comments;
    }

    public BlogManager comments(List<Map<String, Object>> comments) {
        setComments(comments);
        return this;
    }

    // Add a comment with StampedLock for efficient locking
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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof BlogManager)) {
            return false;
        }
        BlogManager blogManager = (BlogManager) o;
        return Objects.equals(readWriteLock, blogManager.readWriteLock) &&
                Objects.equals(stampedLock, blogManager.stampedLock) &&
                Objects.equals(comments, blogManager.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(readWriteLock, stampedLock, comments);
    }

    @Override
    public String toString() {
        return "{" +
                " readWriteLock='" + getReadWriteLock() + "'" +
                ", stampedLock='" + getStampedLock() + "'" +
                ", comments='" + getComments() + "'" +
                "}";
    }
}
