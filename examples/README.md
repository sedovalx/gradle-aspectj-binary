Simple example of using AspectJ in a Kotlin project. 

It expects `1.0-SNAPSHOT` version of the plugin in the local Maven repository or a value in the 
`TREVIS_BUILD_NUMBER` environment variable. Suppose you have `20` as a value for the env variable, 
 the `1.0.20` plugin version is used than. Of course, you can edit the `build.gradle` and specify 
 the version directly. 
 
 Run
   
    ./gradlew clean :examples:app:run
    
to see the result. Probably you will see something similar to this:
 
    Custom java aspect:
    Running from javaaspect BEFORE the execution
    Hello Java
    
    Custom kotlin aspect:
    Running from kotlin aspect BEFORE the execution
    Running from kotlin aspect AFTER the execution
    Hello Kotlin
    
    Jcabi aspect:
    [main] WARN com.github.sedovalx.sandbox.gradle.aspectj.examples.aspects.App - java.lang.RuntimeException: I'm very quiet
            at com.github.sedovalx.sandbox.gradle.aspectj.examples.aspects.App.jcabiExample_aroundBody2(App.java:24)
            at com.github.sedovalx.sandbox.gradle.aspectj.examples.aspects.App$AjcClosure3.run(App.java:1)
            at org.aspectj.runtime.reflect.JoinPointImpl.proceed(JoinPointImpl.java:149)
            at com.jcabi.aspects.aj.QuietExceptionsLogger.wrap(QuietExceptionsLogger.java:83)
            at com.github.sedovalx.sandbox.gradle.aspectj.examples.aspects.App.jcabiExample(App.java:24)
            at com.github.sedovalx.sandbox.gradle.aspectj.examples.aspects.App.main(App.java:37)
   
> You have to use AspectJ annotations to describe aspects.     

You don't need all this tricky things in your application. Just add this to the build script

    buildscript {
      repositories {
        jcenter()
      }
      dependencies {
        classpath "com.github.sedovalx.gradle:gradle-aspectj-binary:$pluginVersion"
      }
    }
    
    apply plugin: 'com.github.sedovalx.gradle-aspectj-binary'
             
to the project you want to weave during the build.             