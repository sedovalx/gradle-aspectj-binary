package com.github.sedovalx.gradle.aspectj

import groovy.lang.Closure
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet
import java.io.File

open class AspectjBinaryExtension {
    var applyJavaPlugin: Boolean = true
    val weaveClasses = WeaveClassesExtension()

    fun weaveClasses(closure: Closure<Unit>) {
        closure.delegate = this.weaveClasses
        closure.call()
    }
}

open class WeaveClassesExtension {
    var ajcSourceSets: Set<SourceSet>? = null
    var source: String = "1.7"
    var target: String = "1.7"
    var writeToLog: Boolean = false
    var outputDir: File? = null
    var additionalAjcParams: List<String>? = null
}

class AspectJBinaryWeavingPlugin : Plugin<Project> {
    companion object {
        const val WEAVE_TASK_NAME = "weaveClasses"
    }

    override fun apply(project: Project) {
        val extension = project.extensions.create("aspectjBinary", AspectjBinaryExtension::class.java)
        val weaveClasses = project.tasks.register(WEAVE_TASK_NAME, AjcTask::class.java) {
            with (it) {
                group = "aspects"
                description = "AspectJ binary weaving (⌐■_■)"
            }
        }

        project.afterEvaluate {
            val sourceSets = extension.weaveClasses.ajcSourceSets ?: run {
                if (extension.applyJavaPlugin) {
                    val javaConvention = project.convention.getPlugin(JavaPluginConvention::class.java)
                    val main = javaConvention.sourceSets.find { it.name == "main" } ?: throw GradleException("Source set 'main' is not found")
                    setOf(main)
                } else {
                    throw GradleException("Either java plugin should be applied or aspectjBinary.weaveClasses.sourceSets property should be set")
                }
            }

            if (extension.applyJavaPlugin) {
                project.plugins.apply(JavaPlugin::class.java)

                val compileJava = project.tasks.named("compileJava")
                val classes = project.tasks.named("classes")
                weaveClasses.configure { it.dependsOn(compileJava) }
                classes.configure { it.dependsOn(weaveClasses) }
            }

            weaveClasses.configure { task ->
                task.source = extension.weaveClasses.source
                task.target = extension.weaveClasses.target
                task.sourceSets = sourceSets
                task.outputDir = extension.weaveClasses.outputDir
                task.writeToLog = extension.weaveClasses.writeToLog
                task.additionalAjcParams = extension.weaveClasses.additionalAjcParams ?: emptyList()
            }
        }
    }
}

