pluginManagement {
    val kotlinVersion: String by settings

    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }

    plugins {
        kotlin("jvm") version kotlinVersion
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.github.sedovalx.gradle-aspectj-binary") {
                // always use the local version
                useModule("com.github.sedovalx.gradle:gradle-aspectj-binary:1.0-SNAPSHOT")
            }
        }
    }
}

val disableExamples: String by extra

include("plugin")
if (!disableExamples.toBoolean()) {
    include("examples", "examples:aspects", "examples:app")
}