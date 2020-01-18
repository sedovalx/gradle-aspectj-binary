package com.github.sedovalx.gradle.aspectj

import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.TrueFileFilter
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.IMessageHolder
import org.aspectj.tools.ajc.Main
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.FileCollection
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.IOException
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Paths

open class AjcTask : DefaultTask() {
    init {
        logging.captureStandardOutput(LogLevel.INFO)
    }

    // Task properties
    lateinit var sourceSets: Set<SourceSet>
    lateinit var additionalAjcParams: List<String>
    lateinit var source: String
    lateinit var target: String
    var outputDir: File? = null
    var writeToLog: Boolean = false

    @TaskAction
    fun compile() {
        val tempDirectory = Paths.get(project.buildDir.toPath().toString(), "ajc").toFile()
        if (!tempDirectory.exists()) {
            logger.info("Created temp folder $tempDirectory")
            tempDirectory.mkdirs()
        }
        val logPath = Paths.get(project.buildDir.toPath().toString(), "ajc.log")

        val inpath = sourceSets.map { it.output.classesDirs }.fold().joinToString(File.pathSeparator) { it.absolutePath }
        val classpath = sourceSets.map { it.compileClasspath + it.runtimeClasspath }.fold().filter { it.exists() }.asPath
        val outputDir = this.outputDir
            ?: sourceSets.map { it.output.classesDirs }.fold().find { it.name == "java" }?.absoluteFile
            ?: sourceSets.firstOrNull()?.output?.classesDirs?.firstOrNull()?.absoluteFile
            ?: throw GradleException("Property outputDir for the weave task is undefined. You must either specify aspectjBinary.weaveClasses.outputDir manually or apply the java plugin")

        logger.info("=".repeat(30))
        logger.info(
            buildString {
                appendln("Ajc task parameters:")
                appendln("source: $source")
                appendln("target: $target")
                appendln("sourceSets: ${sourceSets.map { it.name }}")
                appendln("outputDir: $outputDir")
                appendln("writeToLog: $writeToLog")
                appendln("logPath: $logPath")
                appendln("additionalAjcParams: $additionalAjcParams")
            }.trimEnd('\n')
        )
        logger.info("=".repeat(30))

        val ajcParams = arrayOf(
                "-Xset:avoidFinal=true",
                "-Xlint:warning",
                "-inpath",
                inpath,
                "-sourceroots",
                getSourceRoots(),
                "-d",
                tempDirectory.absolutePath,
                "-classpath",
                classpath,
                "-aspectpath",
                classpath,
                "-source",
                this.source,
                "-target",
                this.target,
                "-g:none",
                "-encoding",
                "UTF-8",
                "-time",
                "-warn:constructorName",
                "-warn:packageDefaultMethod",
                "-warn:deprecation",
                "-warn:maskedCatchBlocks",
                "-warn:unusedLocals",
                "-warn:unusedArguments",
                "-warn:unusedImports",
                "-warn:syntheticAccess",
                "-warn:assertIdentifier"
        ).chainIf(writeToLog) {
            plus("-log").plus(logPath.toString()).plus("-showWeaveInfo")
        }.chainIf(additionalAjcParams.isNotEmpty()) {
            plus(additionalAjcParams)
        }

        logger.debug("About to run ajc with parameters: \n${ajcParams.toList().joinToString("\t\n")}")

        val currentClasspath =
            (Thread.currentThread().contextClassLoader as? URLClassLoader)?.urLs?.joinToString("\n") { it.path }
        if (currentClasspath != null) {
            logger.debug("Task classpath:\n$currentClasspath")
        }

        val msgHolder = try {
            MsgHolder().apply {
                Main().run(ajcParams, this)
            }
        } catch (ex: Exception) {
            throw GradleException("Error running task", ex)
        }

        val processedFiles = files(temporaryDir).size

        try {
            logger.info("ajc completed, processing the temp")
            FileUtils.copyDirectory(tempDirectory, outputDir)
            FileUtils.cleanDirectory(tempDirectory)
        } catch (ex: IOException) {
            throw GradleException("Failed to copy files and clean temp", ex)
        }

        if (writeToLog) {
            logger.info("See $logPath for the Ajc log messages")
        } else {
            logger.info("ajc result: %d file(s) processed, %d pointcut(s) woven, %d error(s), %d warning(s)".format(
                    processedFiles,
                    msgHolder.numMessages(IMessage.WEAVEINFO, false),
                    msgHolder.numMessages(IMessage.ERROR, true),
                    msgHolder.numMessages(IMessage.WARNING, false)
            ))

            msgHolder.logIfAny(LogLevel.ERROR, IMessage.ERROR, greater = true)
            msgHolder.logIfAny(LogLevel.WARN, IMessage.WARNING, greater = false)

            if (msgHolder.hasAnyMessage(IMessage.ERROR, greater = true)) {
                throw GradleException("Ajc failed, see messages above. You can run the task with --info or --debug " +
                        "parameters to get more detailed output.")
            }
        }
    }

    private fun files(dir: File): Collection<File> {
        return FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE).filter { it.isFile }
    }

    /**
     * Comma separated absolute paths to folders with aspects
     */
    private fun getSourceRoots(): String = Files.createTempDirectory("aspects").toAbsolutePath().toString()

    private fun IMessageHolder.logIfAny(logLevel: LogLevel, kind: IMessage.Kind, greater: Boolean) {
        if (hasAnyMessage(kind, greater)) {
            val message = getMessages(kind, greater).joinToString("\n * ", prefix = "\n$logLevel:\n * ")
            logger.log(logLevel, message)
        }
    }

    private fun Iterable<FileCollection>.fold(): FileCollection {
        return this.fold<FileCollection, FileCollection>(project.files()) { files, item -> files + item }
    }
}

fun <T> T.chainIf(condition: Boolean, body: T.() -> T): T {
    return if (condition) {
        body()
    } else {
        this
    }
}