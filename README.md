# gradle-aspectj-binary

[![Build Status](https://travis-ci.org/sedovalx/gradle-aspectj-binary.svg?branch=master)](https://travis-ci.org/sedovalx/gradle-aspectj-binary)
[ ![Download](https://api.bintray.com/packages/sedovalx/com.github.sedovalx/com.github.sedovalx.gradle-aspectj-binary/images/download.svg) ](https://bintray.com/sedovalx/com.github.sedovalx/com.github.sedovalx.gradle-aspectj-binary/_latestVersion)

Gradle plugin for AspectJ binary weaving. It works similar to https://github.com/jcabi/jcabi-maven-plugin 
doing the weaving on the weaving over already compiled classes. It is particularly helpful if the source code
  is written in a language different from Java. For example, [Kotlin](https://kotlinlang.org), as it produces 
  fully compatible Java bytecode. It goes without saying that the plugin works for Java sources as well. 
  
### Requirements  
  
  - The plugin uses [AspectJ 1.9.2](https://www.eclipse.org/aspectj/) for generating the bytecode. Please read there
  about supported JDK versions. 
  - The plugin itself is compiled with `openjdk8`, so it requires JDK 8 or above to work.
  - The plugin uses Gradle 4.9 API so you need Gradle of that version or newer
      
### Usage

  Here is an example project in the `examples` folder. Basically you need to add the plugin to your build script
  
      buildscript {
        repositories {
          jcenter()
        }
        dependencies {
          classpath "com.github.sedovalx.gradle:gradle-aspectj-binary:$pluginVersion"
        }
      }
      
      apply plugin: 'com.github.sedovalx.gradle-aspectj-binary'
            
  `weaveClasses` task becomes available after that. By default, if the `java` plugin is not disabled via 
  configuration (see below), there are pre-configured tasks dependencies:
  
      weaveClasses.dependsOn compileJava
      classes.dependsOn weaveClasses
  
  so you can just run the build and have all your aspects in the `main` source set applied.
  
  > You need to weave both aspects and classes where aspects should be applied. So if you have aspect 
  classes in a project A and classes to be weaved in a project B you should add the `weaveClasses` task to the build 
  process of both projects. See the `examples` project for details.
  
### Available configuration
  
  The plugins can be configured with the following parameters
  
      aspectjBinary {
        applyJavaPlugin = true
        weaveClasses {
          ajcSourceSets = [project.sourceSets.main]
          outputDir = project.file(...)
          source = '1.7'  
          target = '1.7'
          additionalAjcParams = ['-proceedOnError']  
          writeToLog = true
        } 
      }
      
  Parameters:
  - `applyJavaPlugin` should the `java` plugin be applied with the `aspectjBinary` plugin. By default, it is applied but
  in some cases, for example Android projects, it is not desired. WARN: in this case, you **must** provide the `ajcSourceSets`
  and the `outputDir` property values by yourself as there is no default configuration for Android projects (any help is appreciated).  
  - `ajcSourceSets` is a set of Gradle source sets to be weaved. By default, it includes the `main` source set only. 
  Both `compileClasspath` and `runtimeClasspath` collections are extracted from each source set.
  - `outputDir` is a folder where the weaved classes are copied. By default, it is `build/classes/java/main`. 
  - `source` has '1.7' as default value and is passed as it is to the [ajc](http://www.eclipse.org/aspectj/doc/next/devguide/ajc-ref.html) compiler parameter
  - `target` has '1.7' as default value and is passed as it is to the [ajc](http://www.eclipse.org/aspectj/doc/next/devguide/ajc-ref.html) compiler parameter
  - `additionalAjcParams` allows addition of arbitrary entries to the tail of the default AJC parameters 
  - `writeToLog` has `false` as a default value and defines if ajc's compile messages should 
  affect the build process. In case of value is `true` all the messages are written in the 
  `build/ajc.log` file and do not affect the build result. Otherwise, error or warning messages are 
  printed in the build output and ajc errors (if any) break the build process.   

### Clean local build

The examples project depends on a version of the plugin. In case of a clean build no plugin version exists in 
the repository. So I use a little bit hacky way to do the trick.

     $ echo "include 'plugin'" > settings.gradle
     $ ./gradlew clean :plugin:publishMavenJavaPublicationToMavenLocal
     $ echo "include 'plugin', 'examples', 'examples:aspects', 'examples:app'" > settings.gradle
     $ ./gradlew :examples:app:run
  
