package com.example.spring_boot_reflection_demo.controller;

import com.example.spring_boot_reflection_demo.annotation.QController;
import com.example.spring_boot_reflection_demo.annotation.QRequestMapping;

@QController
public class MyController {
    @QRequestMapping("/")
    public String hello() {
        return "Hello World";
    }
}
