package deps

object Deps {
    const val aspectjVersion = "1.9.2"

    fun kotlinStdlib(version: String) = "org.jetbrains.kotlin:kotlin-stdlib:$version"
    const val aspectjTools = "org.aspectj:aspectjtools:$aspectjVersion"
    const val aspectjrt = "org.aspectj:aspectjrt:$aspectjVersion"
    const val commonsIo = "commons-io:commons-io:2.5"
    const val jcabiAspects = "com.jcabi:jcabi-aspects:0.22.6"
    const val slf4jSimple = "org.slf4j:slf4j-simple:1.7.21"
}