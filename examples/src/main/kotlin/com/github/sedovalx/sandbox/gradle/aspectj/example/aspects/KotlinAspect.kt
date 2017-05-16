package com.github.sedovalx.sandbox.gradle.aspectj.example.aspects

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect

@Aspect
class KotlinAspect {
    @Around("@annotation(annotation) && execution(* *(..))")
    @Throws(Throwable::class)
    fun around(joinPoint: ProceedingJoinPoint, annotation: KotlinAnnotation): Any {
        println("Running from ${annotation.value} aspect BEFORE the execution")
        try {
            return joinPoint.proceed()
        } finally {
            println("Running from ${annotation.value} aspect AFTER the execution")
        }
    }
}
