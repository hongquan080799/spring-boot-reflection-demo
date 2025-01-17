package com.example.spring_boot_reflection_demo.annotation;

import com.example.spring_boot_reflection_demo.qenum.QMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface QRequestMapping {
    String value();
    QMethod method() default QMethod.GET;
}

