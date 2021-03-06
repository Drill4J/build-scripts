if (version == Project.DEFAULT_VERSION) {
    version = rootProject.run {
        val propName = "gitVersion"
        extra.properties[propName] ?: run {
            fromEnv() ?: fromGit() ?: version
        }.also { extra[propName] = it }
    }
}

fun fromEnv(): String? = System.getenv("GITHUB_REF")?.let {
    "refs/tags/v(.*)".toRegex().matchEntire(it)
}?.groupValues?.get(1)

fun Project.fromGit(): String? = takeIf { rootDir.resolve(".git").isDirectory }?.run {
    val output = java.io.ByteArrayOutputStream()
    exec {
        val tagPattern = "v[0-9]*.[0-9]*.[0-9]*"
        commandLine("git", "describe", "--tags", "--long", "--match", tagPattern)
        standardOutput = output
        isIgnoreExitValue = true
    }.takeIf { it.exitValue == 0 }
        ?.let {
            val abbrevRegex = "-(\\d+)-g([0-9a-f]+)$".toRegex()
            "$output".run {
                trim().removePrefix("v").replace(abbrevRegex, "")
            }.takeIf(String::any)
        }
}
