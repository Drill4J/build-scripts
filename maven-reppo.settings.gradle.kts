pluginManagement {
    val mavenRepo: String by extra("https://oss.jfrog.org/oss-release-local")
    repositories {
        maven(url = mavenRepo)
        gradlePluginPortal()
    }
}
