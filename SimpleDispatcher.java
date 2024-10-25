package com.example.spring_boot_reflection_demo;

import com.example.spring_boot_reflection_demo.annotation.QController;
import com.example.spring_boot_reflection_demo.annotation.QRequestMapping;
import com.example.spring_boot_reflection_demo.qenum.QMethod;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class SimpleDispatcher {
    // Store both the method, controller instance, and HTTP method
    private final Map<String, Map<QMethod, ControllerMethodPair>> routeMap = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private record ControllerMethodPair(Object controller, Method method) {
    }

    // Register controller and its methods for different HTTP methods
    public void register(Object controller) {
        Class<?> clazz = controller.getClass();
        if (clazz.isAnnotationPresent(QController.class)) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(QRequestMapping.class)) {
                    QRequestMapping mapping = method.getAnnotation(QRequestMapping.class);
                    String path = mapping.value();
                    QMethod httpMethod = mapping.method();

                    routeMap.computeIfAbsent(path, k -> new HashMap<>())
                            .put(httpMethod, new ControllerMethodPair(controller, method));
                    System.out.println("Register endpoint [" + httpMethod.name() + ":" + path + "] successfully");
                }
            }
        }
    }

    // Handle request by looking up method, controller, and HTTP method
    public String handleRequest(String path, QMethod httpMethod, String requestBody) throws Exception {
        Map<QMethod, ControllerMethodPair> methodMap = routeMap.get(path);
        if (methodMap != null && methodMap.containsKey(httpMethod)) {
            ControllerMethodPair pair = methodMap.get(httpMethod);

            Object result;
            if (httpMethod == QMethod.GET || requestBody == null) {
                result = pair.method().invoke(pair.controller());
            } else {
                Class<?> paramType = pair.method().getParameterTypes()[0];
                Object deserialized = objectMapper.readValue(requestBody, paramType);
                result = pair.method().invoke(pair.controller(), deserialized);
            }
            return objectMapper.writeValueAsString(result);
        }
        return "404 Not Found";
    }
}