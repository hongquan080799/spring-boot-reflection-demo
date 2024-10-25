package com.example.spring_boot_reflection_demo;

import com.example.spring_boot_reflection_demo.annotation.QController;
import com.example.spring_boot_reflection_demo.qenum.QMethod;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.reflections.Reflections;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Set;

public class SimpleHttpServer {
    private final SimpleDispatcher dispatcher = new SimpleDispatcher();
    public SimpleHttpServer() {
        scanAndRegisterControllers("com.example.spring_boot_reflection_demo.controller");
    }

    private void scanAndRegisterControllers(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(QController.class);

        try {
            for (Class<?> controllerClass : controllers) {
                Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
                dispatcher.register(controllerInstance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null);  // default executor
        server.start();
        System.out.println("Server started on port " + port);
    }

    class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            QMethod httpMethod = QMethod.valueOf(exchange.getRequestMethod().toUpperCase());
            String requestBody = null;
            String response = "404 Not Found";
            int responseCode = 404;

            // Read request body if applicable (POST or PUT)
            if (httpMethod == QMethod.POST || httpMethod == QMethod.PUT) {
                try (InputStream is = exchange.getRequestBody()) {
                    requestBody = new String(is.readAllBytes());
                }
            }

            try {
                // Use dispatcher to handle request and get the response as an object
                String result = dispatcher.handleRequest(path, httpMethod, requestBody);
                if (result != null) {
                    // Serialize the response object to JSON
                    response = result;
                    responseCode = 200;
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "{\"error\": \"Internal Server Error\"}";
                responseCode = 500;
            }

            // Send the response
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(responseCode, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}