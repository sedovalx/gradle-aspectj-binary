package com.github.sedovalx.sandbox.gradle.aspectj.example.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class JavaAspect {
    @Before("@annotation(annotation) && execution(* *(..))")
    public void before(JavaAnnotation annotation) throws Throwable {
        System.out.println("Running from " + annotation.value() + "aspect BEFORE the execution");
    }
}
