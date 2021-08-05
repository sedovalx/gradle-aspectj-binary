import deps.Deps

plugins {
    java
    kotlin("jvm")
    id("com.github.sedovalx.gradle-aspectj-binary")
}

val kotlinVersion: String by project

dependencies {
    api(Deps.kotlinStdlib(kotlinVersion))
    api(Deps.aspectjrt)
    api(Deps.jcabiAspects)
    api(Deps.slf4jSimple)
}

aspectjBinary {
    with(weaveClasses) {
        source = "1.7"
        target = "1.7"
        ajcSourceSets = setOf(project.sourceSets.main.get())
        additionalAjcParams = listOf("-proceedOnError")
        writeToLog = true
    }
}