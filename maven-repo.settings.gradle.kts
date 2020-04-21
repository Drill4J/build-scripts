pluginManagement {
    val mavenRepo: String = extra.properties["mavenRepo"] as? String
        ?: "https://oss.jfrog.org/oss-release-local"
    repositories {
        maven(url = mavenRepo)
        gradlePluginPortal()
    }
}
