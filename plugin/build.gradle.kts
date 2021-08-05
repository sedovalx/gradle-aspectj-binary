import ci.actions.Build
import deps.Deps
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm")
    `maven-publish`
}

group = "com.github.sedovalx.gradle"
version = Build.pluginVersion

val kotlinVersion: String by project

dependencies {
    api(gradleApi())
    api(Deps.kotlinStdlib(kotlinVersion))
    api(Deps.aspectjTools)
    api(Deps.commonsIo)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.6"
}

val sourcesJar by tasks.registering(Jar::class) {
    dependsOn("classes")
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val javadoc: Task by tasks.getting

val javadocJar by tasks.registering(Jar::class) {
    dependsOn(javadoc)
    archiveClassifier.set("javadoc")
    from(javadoc)
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(sourcesJar.get())
            artifact(javadocJar.get())
            artifactId = "gradle-aspectj-binary"
            pom.withXml {
                val root = asNode()
                root.appendNode("description", "Gradle AspectJ binary weaving plugin")
                root.appendNode("name", "Gradle AspectJ binary weaving plugin")
                root.appendNode("url", "https://github.com/sedovalx/gradle-aspectj-binary")
            }
        }
    }
}

//def pomConfig = {
//    licenses {
//        license {
//            name "The Apache Software License, Version 2.0"
//            url "http://www.apache.org/licenses/LICENSE-2.0.txt"
//            distribution "repo"
//        }
//    }
//    developers {
//        developer {
//            id "1"
//            name "Alexander Sedov"
//            email "sedov.alx@gmail.com"
//        }
//    }
//
//    scm {
//       url "https://github.com/sedovalx/gradle-aspectj-binary"
//    }
//}
//
//publishing {
//    publications {
//        mavenJava(MavenPublication) {
//            from components.java
//            artifact sourcesJar
//            artifact javadocJar
//            artifactId "gradle-aspectj-binary"
//            pom.withXml {
//                def root = asNode()
//                root.appendNode("description", "Gradle AspectJ binary weaving plugin")
//                root.appendNode("name", "Gradle AspectJ binary weaving plugin")
//                root.appendNode("url", "https://github.com/sedovalx/gradle-aspectj-binary")
//                root.children().last() + pomConfig
//            }
//        }
//    }
//}
//
//bintray {
//    user = System.getenv("BINTRAY_USER")
//    key = System.getenv("BINTRAY_KEY")
//    publications = ["mavenJava"]
//    pkg {
//        repo = "com.github.sedovalx"
//        name = "com.github.sedovalx.gradle-aspectj-binary"
//        licenses = ["Apache-2.0"]
//        vcsUrl = "https://github.com/sedovalx/gradle-aspectj-binary"
//        publicDownloadNumbers = true
//        version {
//            name = pluginVersion
//            desc = "Gradle AspectJ binary weaving plugin"
//            released = new Date()
//            gpg {
//                sign = true
//            }
//        }
//    }
//
//}
