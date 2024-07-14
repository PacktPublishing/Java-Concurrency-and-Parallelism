package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.inject.Inject;

@QuarkusTest
public class ProductRepositoryTest {
    private static final Logger LOG = Logger.getLogger(ProductRepositoryTest.class);

    @BeforeEach
    public void setUp() {
        // Ensure RestAssured uses the random port assigned by Quarkus
        RestAssured.port = RestAssured.DEFAULT_PORT;
        LOG.info("RestAssured.port set to: " + RestAssured.port);
    }

    @Inject
    ProductRepository repository;

    @Test
    public void testCountAllProducts() {
        assertEquals(2, repository.countAllProducts()); // Assuming 2 products in repository
    }
}
