import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect

@Aspect
class MyAspect {
    @Around("@annotation(annotation) && execution(* *(..))")
    @Throws(Throwable::class)
    fun around(joinPoint: ProceedingJoinPoint, annotation: MyAnnotation): Any {
        println("Before " + annotation.value)
        try {
            return joinPoint.proceed()
        } finally {
            println("After")
        }
    }
}
