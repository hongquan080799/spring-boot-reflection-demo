package com.example.spring_boot_reflection_demo;

import com.example.spring_boot_reflection_demo.annotation.QRequestMapping;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

class QTest {
	class Person {
		private Long id;
		private String name;
		private String address;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}
		@QRequestMapping("")
		public void testAnnotation() {
			System.out.println("execute testAnnotation function");
		}
	}

	@Test
	public void reflectionTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<?> clazz = Person.class;
		Field[] fields = clazz.getDeclaredFields();
		for (Field field: fields) {
			System.out.println(field.getName());
		}

		Method[] methods = clazz.getDeclaredMethods();
		for (Method method: methods) {
			System.out.println(method.getName());
			Arrays.stream(method.getDeclaredAnnotations()).forEach(e -> {
				System.out.println(e.annotationType().getTypeName());
			});
		}

		Method method = clazz.getMethod("testAnnotation");
		method.invoke(new Person());
	}

}































