val mavenRepo: String = extra.properties["mavenRepo"] as? String
    ?: "https://oss.jfrog.org/oss-release-local"

repositories {
    maven(url = mavenRepo)
}

extensions.findByType<PublishingExtension>()?.repositories {
    maven(url = mavenRepo).credentials {
        username = project.propOrEnv("bintrayUser", "BINTRAY_USER")
        password = project.propOrEnv("bintrayApiKey", "BINTRAY_API_KEY")
    }
}

fun Project.propOrEnv(prop: String, env: String): String? = run {
    findProperty(prop)?.toString() ?: System.getenv(env)
}
