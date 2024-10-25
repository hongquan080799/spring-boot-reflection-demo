package com.example.spring_boot_reflection_demo;

public class SpringBootReflectionDemoApplication {
	public static void main(String[] args) throws Exception {
		SimpleHttpServer server = new SimpleHttpServer();
		server.start(8000);
	}
}
