package com.github.sedovalx.sandbox.gradle.aspectj.example;

import com.github.sedovalx.sandbox.gradle.aspectj.example.aspects.JavaAnnotation;
import com.github.sedovalx.sandbox.gradle.aspectj.example.aspects.KotlinAnnotation;

public class App {

    @JavaAnnotation("java")
    @KotlinAnnotation("kotlin")
    public String hello(String name) {
        System.out.println("Got " + name);
        return "Hello " + name;
    }

    public static void main(String[] args) {
        App app = new App();
        System.out.println(app.hello("Bob"));
    }
}
