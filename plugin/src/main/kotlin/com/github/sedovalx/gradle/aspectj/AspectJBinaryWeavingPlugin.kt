package com.github.sedovalx.gradle.aspectj

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention


class AspectJBinaryWeavingPlugin : Plugin<Project> {
    companion object {
        val WEAVE_TASK_NAME = "weaveClasses"
    }
    override fun apply(project: Project) {
        with (project.logger) {
            // Silently apply java plugin to the project
            project.plugins.apply(JavaPlugin::class.java)
            val javaConvention = project.convention.getPlugin(JavaPluginConvention::class.java)
            // Only the main source set is supported for now
            val main = javaConvention.sourceSets.find { it.name == "main" } ?: throw GradleException("Source set 'main' is not found")

            project.tasks.create(WEAVE_TASK_NAME, AjcTask::class.java).apply {
                group = "aspects"
                description = "AspectJ binary weaving (⌐■_■)"
                sourceSet = main
                buildDir = project.buildDir
            }
        }
    }
}

