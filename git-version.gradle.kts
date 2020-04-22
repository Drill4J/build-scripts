import java.io.*

if (version == Project.DEFAULT_VERSION) {
    version = fromEnv() ?: fromGit() ?: version
}

fun fromEnv(): String? = System.getenv("GITHUB_REF")?.let {
    "refs/tags/v(.*)".toRegex().matchEntire(it)
}?.groupValues?.get(1)

fun Project.fromGit(): String? = takeIf { rootDir.resolve(".git").isDirectory }?.run {
    val output = ByteArrayOutputStream()
    val abbrevRegex = "-(\\d+)-g([0-9a-f]+)$".toRegex()
    exec {
        val tagPattern = "v[0-9]*.[0-9]*.[0-9]*"
        commandLine("git", "describe", "--tags", "--long", "--match", tagPattern)
        standardOutput = output
        isIgnoreExitValue = true
    }.takeIf { it.exitValue == 0 }
        ?.let { "$output".substring(1).trim() }
        ?.replace(abbrevRegex, "")
}
