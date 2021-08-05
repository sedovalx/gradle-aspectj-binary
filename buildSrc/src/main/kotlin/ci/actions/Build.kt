package ci.actions

object Build {
    val buildNumber: String? = System.getenv("GITHUB_RUN_NUMBER")
    val pluginVersion = if (buildNumber != null) "1.0.$buildNumber" else "1.0-SNAPSHOT"
}