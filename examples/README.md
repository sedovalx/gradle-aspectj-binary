Simple example of using AspectJ in a Kotlin project. 

It expects `1.0-SNAPSHOT` version of the plugin in the local Maven repository or a value in the 
`TREVIS_BUILD_NUMBER` environment variable. Suppose you have `20` as a value for the env variable, 
 the `1.0.20` plugin version is used than. Of course, you can edit the `build.config` and specify 
 the version directly. 
 
 Run
   
    ./gradlew clean run
    
to see the result. Probably you will see something similar to this:
 
    Custom java aspect:
    Running from javaaspect BEFORE the execution
    Hello Java
    
    Custom kotlin aspect:
    Running from kotlin aspect BEFORE the execution
    Running from kotlin aspect AFTER the execution
    Hello Kotlin
    
    Jcabi aspect:
    [main] WARN com.github.sedovalx.sandbox.gradle.aspectj.example.App - java.lang.RuntimeException: I'm very quiet
            at com.github.sedovalx.sandbox.gradle.aspectj.example.App.jcabiExample_aroundBody2(App.java:24)
            at com.github.sedovalx.sandbox.gradle.aspectj.example.App$AjcClosure3.run(App.java:1)
            at org.aspectj.runtime.reflect.JoinPointImpl.proceed(JoinPointImpl.java:149)
            at com.jcabi.aspects.aj.QuietExceptionsLogger.wrap(QuietExceptionsLogger.java:83)
            at com.github.sedovalx.sandbox.gradle.aspectj.example.App.jcabiExample(App.java:24)
            at com.github.sedovalx.sandbox.gradle.aspectj.example.App.main(App.java:37)
   
> You have to use AspectJ annotations to describe aspects.     