package com.example.spring_boot_reflection_demo;

import com.example.spring_boot_reflection_demo.annotation.QController;
import com.example.spring_boot_reflection_demo.annotation.QRequestMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class SimpleDispatcher {
    // Store both the method and the controller instance
    private final Map<String, ControllerMethodPair> routeMap = new HashMap<>();

    // Inner class to hold both method and controller
    private static class ControllerMethodPair {
        private final Object controller;
        private final Method method;

        public ControllerMethodPair(Object controller, Method method) {
            this.controller = controller;
            this.method = method;
        }

        public Object getController() {
            return controller;
        }

        public Method getMethod() {
            return method;
        }
    }

    // Register controller and its methods
    public void register(Object controller) {
        Class<?> clazz = controller.getClass();
        if (clazz.isAnnotationPresent(QController.class)) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(QRequestMapping.class)) {
                    QRequestMapping getMapping = method.getAnnotation(QRequestMapping.class);
                    // Store both the controller and the method in the map
                    routeMap.put(getMapping.value(), new ControllerMethodPair(controller, method));
                }
            }
        }
    }

    // Handle request by looking up method and invoking on the correct controller
    public String handleRequest(String path) throws Exception {
        ControllerMethodPair pair = routeMap.get(path);
        if (pair != null) {
            // Invoke the method on the controller instance
            return (String) pair.getMethod().invoke(pair.getController());
        }
        return "404 Not Found";
    }
}
