package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("com.example.LambdaHandler")
public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Inject
    ProductResource productResource;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        String path = input.getPath();
        String httpMethod = input.getHttpMethod();

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        response.setBody("Default response");

        if ("GET".equalsIgnoreCase(httpMethod)) {
            if ("/api/product".equals(path)) {
                response.setBody(productResource.getAllProducts().toString());
            } else if ("/api/product/count".equals(path)) {
                response.setBody(String.valueOf(productResource.countAllProducts()));
            } else if (path.startsWith("/api/product/")) {
                int id = Integer.parseInt(path.substring("/api/product/".length()));
                response.setBody(productResource.getProduct(id).toString());
            }
        }

        return response;
    }
}
