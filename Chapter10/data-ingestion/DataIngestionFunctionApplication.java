package com.example;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DataIngestionFunctionApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataIngestionFunctionApplication.class, args);
    }

    @Bean
    public Function<Map<String, Object>, Map<String, Object>> dataIngestion() {
        return input -> {
            Map<String, Object> response = new HashMap<>();
            if (validateData(input)) {
                storeData(input);
                response.put("status", "Data Ingested Successfully");
            } else {
                response.put("status", "Invalid Data");
            }
            return response;
        };
    }

    private boolean validateData(Map<String, Object> input) {
        return input.containsKey("timestamp") && input.containsKey("value");
    }

    private void storeData(Map<String, Object> input) {
        // Code to store data in DynamoDB
    }
}
