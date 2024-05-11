package com.example;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Problem {
    private final String description; // Example property for a problem description

    public Problem(String description) {
        this.description = description; // Initialize with a specific description
    }

    public String getDescription() {
        return description;
    }

    // Method to determine if the problem is simple
    public boolean isSimple() {
        // Placeholder logic to determine simplicity, modify as needed
        return description.length() < 10; // Example logic: simple if description is short
    }

    // Method to decompose a complex problem into smaller problems
    public Stream<Problem> decompose() {
        // Example logic for decomposition
        List<Problem> subproblems = Arrays.asList(
                new Problem(description + " part 1"),
                new Problem(description + " part 2"));

        return subproblems.stream();
    }
}
