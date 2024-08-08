package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

public class VertxHttpServerExample extends AbstractVerticle {

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();
        server.requestHandler(request -> {
            String path = request.path();
            if ("/hello".equals(path)) {
                request.response().putHeader("content-type", "text/plain").end("Hello, Vert.x!");
            } else {
                request.response().setStatusCode(404).end("Not Found");
            }
        });
        server.listen(8082, result -> {
            if (result.succeeded()) {
                System.out.println("Server started on port 8082");
            } else {
                System.err.println("Failed to start server: " + result.cause());
            }
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new VertxHttpServerExample());
    }
}
