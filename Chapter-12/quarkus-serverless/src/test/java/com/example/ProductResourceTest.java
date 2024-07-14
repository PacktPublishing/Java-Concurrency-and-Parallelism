package com.example;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;

@QuarkusTest
public class ProductResourceTest {
  private static final Logger LOG = Logger.getLogger(ProductResourceTest.class);

  @BeforeEach
  public void setUp() {
    int port = Integer.getInteger("quarkus.http.test-port", RestAssured.DEFAULT_PORT);
    RestAssured.port = port;
    LOG.info("RestAssured.port set to: " + RestAssured.port);
  }

  @Test
  public void testGetAllProducts() {
    LOG.info("Sending request to /api/product");
    given()
        .when().get("/api/product")
        .then()
        .statusCode(200)
        .body("$", hasSize(2)) // Change to 2 if there are 2 products in your repository
        .body("[0].name", is("Product1")) // Ensure these match your product names
        .body("[1].name", is("Product2"));
  }

  @Test
  public void testGetProductCount() {
    LOG.info("Sending request to /api/product/count");
    given()
        .when().get("/api/product/count")
        .then()
        .statusCode(200)
        .body(is("2")); // Change to 2 if there are 2 products in your repository
  }

  @Test
  public void testGetProduct() {
    LOG.info("Sending request to /api/product/1");
    given()
        .when().get("/api/product/1")
        .then()
        .statusCode(200)
        .body("name", is("Product1"));

    LOG.info("Sending request to /api/product/4");
    given()
        .when().get("/api/product/4")
        .then()
        .statusCode(204); // This should match your application's behavior for non-existing product
  }
}
