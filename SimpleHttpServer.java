package com.example.spring_boot_reflection_demo;

import com.example.spring_boot_reflection_demo.annotation.QController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.reflections.Reflections;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Set;

public class SimpleHttpServer {
    private SimpleDispatcher dispatcher = new SimpleDispatcher();

    public SimpleHttpServer() {
        scanAndRegisterControllers("com.example.spring_boot_reflection_demo.controller");
    }

    private void scanAndRegisterControllers(String basePackage) {
        // Scan the package for @Controller annotated classes
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(QController.class);

        try {
            // Instantiate and register each controller
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
        server.setExecutor(null); // Default executor
        server.start();
        System.out.println("Server started on port " + port);
    }

    class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String response = "404 Not Found";

            try {
                //serialize => json -> java object // jackson
                response = dispatcher.handleRequest(path);
                //deserialize => java object -> json
            } catch (Exception e) {
                e.printStackTrace();
            }
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

    }
}
