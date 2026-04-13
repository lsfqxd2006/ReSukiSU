plugins {
    alias(libs.plugins.agp.app) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.compose.compiler) apply false
}

val androidMinSdkVersion by extra(26)
val androidTargetSdkVersion by extra(36)
val androidCompileSdkVersion by extra(36)
val androidBuildToolsVersion by extra("36.1.0")
val androidCompileNdkVersion by extra(libs.versions.ndk.get())
val androidSourceCompatibility by extra(JavaVersion.VERSION_21)
val androidTargetCompatibility by extra(JavaVersion.VERSION_21)
val managerVersionCode by extra(30000 + getGitCommitCount() + 700)
val managerVersionName by extra(getGitDescribe())

fun getGitCommitCount(): Int {
    return try {
        providers.exec { commandLine("git", "fetch", "--depth=999999", "https://github.com/ReSukiSU/ReSukiSU.git", "main") }
        providers.exec { commandLine("git", "rev-list", "--count", "FETCH_HEAD") }
            .standardOutput.asText.get().trim().toInt()
    } catch (e: Exception) { 0 }
}

fun getGitDescribe(): String {
    return try {
        providers.exec { commandLine("git", "ls-remote", "--tags", "--sort=-v:refname", "https://github.com/ReSukiSU/ReSukiSU.git") }
            .standardOutput.asText.get()
            .lineSequence()
            .firstOrNull { it.contains("refs/tags/") && !it.contains("^") }
            ?.substringAfterLast("/") ?: "unknown"
    } catch (e: Exception) { "unknown" }
}
