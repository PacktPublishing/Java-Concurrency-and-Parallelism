package com.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class CustomerRecord {
    private String name;
    private int age;
    private String city;

    public CustomerRecord(String name, int age, String city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }
}

class CustomerRecordSpliterator implements Spliterator<CustomerRecord> {
    private final Spliterator<String> lineSpliterator;

    public CustomerRecordSpliterator(Spliterator<String> lineSpliterator) {
        this.lineSpliterator = lineSpliterator;
    }

    @Override
    public boolean tryAdvance(Consumer<? super CustomerRecord> action) {
        return lineSpliterator.tryAdvance(line -> {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                action.accept(new CustomerRecord(parts[0], Integer.parseInt(parts[1]), parts[2]));
            }
        });
    }

    @Override
    public Spliterator<CustomerRecord> trySplit() {
        Spliterator<String> split = lineSpliterator.trySplit();
        return split == null ? null : new CustomerRecordSpliterator(split);
    }

    @Override
    public long estimateSize() {
        return lineSpliterator.estimateSize();
    }

    @Override
    public int characteristics() {
        return lineSpliterator.characteristics();
    }
}

public class CustomerAnalysis {
    public static void main(String[] args) throws URISyntaxException {
        Path currentPath = Paths
                .get(CustomerAnalysis.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        System.out.println("currentPath: " + currentPath);
        Path filePath = currentPath.resolveSibling("customers.txt");
        try (Stream<String> lines = Files.lines(filePath)) {
            Spliterator<String> lineSpliterator = lines.spliterator();
            Spliterator<CustomerRecord> spliterator = new CustomerRecordSpliterator(lineSpliterator);
            double averageAge = StreamSupport.stream(spliterator, true)
                    .filter(c -> "New York".equals(c.getCity()))
                    .collect(Collectors.averagingInt(CustomerRecord::getAge));

            System.out.println("Average age of New York customers: " + averageAge);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
