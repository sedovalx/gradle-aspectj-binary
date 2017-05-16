package com.github.sedovalx.sandbox.gradle.aspectj.example.aspects

@org.aspectj.lang.annotation.Aspect
class MyAspect {
    @org.aspectj.lang.annotation.Around("@annotation(annotation) && execution(* *(..))")
    @Throws(Throwable::class)
    fun around(joinPoint: org.aspectj.lang.ProceedingJoinPoint, annotation: MyAnnotation): Any {
        println("Before " + annotation.value)
        try {
            return joinPoint.proceed()
        } finally {
            println("After")
        }
    }
}
