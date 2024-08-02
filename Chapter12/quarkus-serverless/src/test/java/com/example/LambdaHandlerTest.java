package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.inject.Inject;

@QuarkusTest
public class LambdaHandlerTest {

    private static final Logger LOG = Logger.getLogger(LambdaHandlerTest.class);

    @Inject
    LambdaHandler handler;

    @BeforeEach
    public void setUp() {
        // Ensure RestAssured uses the random port assigned by Quarkus
        RestAssured.port = RestAssured.DEFAULT_PORT;
        LOG.info("RestAssured.port set to: " + RestAssured.port);
    }

    @Test
    public void testGetProductCount() {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setHttpMethod("GET");
        request.setPath("/api/product/count");

        LOG.info("Sending request to /api/product/count");

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, null);
        LOG.info("Received response with status code: " + response.getStatusCode());
        LOG.info("Response body: " + response.getBody());

        assertEquals(200, response.getStatusCode());
        assertEquals("2", response.getBody()); // Assuming 2 products in repository
    }
}
