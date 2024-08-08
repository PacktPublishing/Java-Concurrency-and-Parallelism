package com.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NonBlockingWebServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", 8080));
        serverSocket.configureBlocking(false);

        while (true) {
            SocketChannel clientSocket = serverSocket.accept();
            if (clientSocket != null) {
                clientSocket.configureBlocking(false);
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                clientSocket.read(buffer);
                String request = new String(buffer.array()).trim();
                System.out.println("Received request: " + request);

                // Process the request and send a response
                String response = "HTTP/1.1 200 OK\r\nContent-Length: 12\r\n\r\nHello, World!";
                ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes());
                clientSocket.write(responseBuffer);
                clientSocket.close();
            }
        }
    }
}
