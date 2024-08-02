package com.example;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class BookingValidationFunctionApplication {

    private static final Logger logger = LoggerFactory.getLogger(BookingValidationFunctionApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BookingValidationFunctionApplication.class, args);
    }

    @Bean
    public Function<String, String> bookingValidation() {
        ObjectMapper objectMapper = new ObjectMapper();
        return input -> {
            long startTime = System.currentTimeMillis();
            logger.info("Start processing input");

            Map<String, Object> inputMap;
            try {
                inputMap = objectMapper.readValue(input, new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                logger.error("Error parsing input JSON", e);
                return "{\"status\":\"Error: Invalid JSON input\"}";
            }

            Map<String, Object> response = new HashMap<>();
            if (validateBooking(inputMap)) {
                if (verifyAvailability(inputMap)) {
                    response.put("status", "Booking Validated");
                } else {
                    response.put("status", "Booking Not Available");
                }
            } else {
                response.put("status", "Invalid Booking Data");
            }

            try {
                String jsonResponse = objectMapper.writeValueAsString(response);
                logger.info("Finished processing in {} ms", System.currentTimeMillis() - startTime);
                return jsonResponse;
            } catch (JsonProcessingException e) {
                logger.error("Error creating response JSON", e);
                return "{\"status\":\"Error: Failed to create response JSON\"}";
            }
        };
    }

    private boolean validateBooking(Map<String, Object> input) {
        return input.containsKey("bookingDate") && input.containsKey("itemId");
    }

    private boolean verifyAvailability(Map<String, Object> input) {
        String bookingDate = (String) input.get("bookingDate");
        String itemId = (String) input.get("itemId");
        logger.info("bookingDat: " + bookingDate + " itemId:" + itemId);
        return true; // Simulated availability check
    }
}
