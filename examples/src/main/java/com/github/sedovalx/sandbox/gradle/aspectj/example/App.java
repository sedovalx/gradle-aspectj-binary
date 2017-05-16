package com.github.sedovalx.sandbox.gradle.aspectj.example;

import com.github.sedovalx.sandbox.gradle.aspectj.example.aspects.JavaAnnotation;
import com.github.sedovalx.sandbox.gradle.aspectj.example.aspects.KotlinAnnotation;
import com.jcabi.aspects.Quietly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static Logger logger = LoggerFactory.getLogger(App.class);

    @KotlinAnnotation("kotlin")
    public String kotlinExample(String name) {
        return "Hello " + name;
    }

    @JavaAnnotation("java")
    public String javaExample(String name) {
        return "Hello " + name;
    }

    @Quietly
    public void jcabiExample() {
        throw new RuntimeException("I'm very quiet");
    }

    public static void main(String[] args) {
        App app = new App();
        System.out.println();
        System.out.println("Custom java aspect:");
        System.out.println(app.javaExample("Java"));

        System.out.println();
        System.out.println("Custom kotlin aspect:");
        System.out.println(app.kotlinExample("Kotlin"));

        System.out.println();
        System.out.println("Jcabi aspect:");
        app.jcabiExample();
    }
}
