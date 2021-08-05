plugins {
    java
    application
    kotlin("jvm")
    id("com.github.sedovalx.gradle-aspectj-binary")
}

dependencies {
    implementation(project(":examples:aspects"))
}

application {
    mainClassName = "com.github.sedovalx.sandbox.gradle.aspectj.examples.aspects.App"
}

aspectjBinary {
    with(weaveClasses) {
        source = "1.7"
        target = "1.7"
        ajcSourceSets = setOf(project.sourceSets.main.get())
        writeToLog = true
        additionalAjcParams = listOf("-proceedOnError")
    }
}