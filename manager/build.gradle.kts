plugins {
    alias(libs.plugins.agp.app) apply false
    alias(libs.plugins.agp.test) apply false
    alias(libs.plugins.androidx.baselineprofile) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.compose.compiler) apply false
}

extra["androidMinSdkVersion"] = 26
extra["androidTargetSdkVersion"] = 37
extra["androidCompileSdkVersion"] = 37
extra["androidBuildToolsVersion"] = "36.1.0"
extra["androidCompileNdkVersion"] = libs.versions.ndk.get()
extra["androidSourceCompatibility"] = JavaVersion.VERSION_21
extra["androidTargetCompatibility"] = JavaVersion.VERSION_21
extra["managerVersionCode"] = 30000 + getGitCommitCount() + 700
extra["managerVersionName"] = getGitDescribe()
extra["isPrBuild"] = project.findProperty("IS_PR_BUILD")?.toString()?.toBoolean() ?: false
extra["defaultManagerPackageName"] = "com.resukisu.resukisu"
extra["managerPackageName"] = project.findProperty("KSU_PACKAGE_NAME")?.toString() ?: extra["defaultManagerPackageName"]
extra["defaultManagerAppName"] = if (extra["isPrBuild"] == true) "ReSukiSU PR" else "ReSukiSU"
extra["managerName"] = project.findProperty("KSU_NAME")?.toString() ?: extra["defaultManagerAppName"]

val isSpoofedBuild = project.findProperty("IS_SPOOFED_BUILD")?.toString()?.toBoolean() ?: false


fun getGitCommitCount(): Int {
    return try {
        val hash = providers.exec {
            commandLine("git", "ls-remote", "https://github.com/ReSukiSU/ReSukiSU.git", "main")
        }.standardOutput.asText.get().split("\\s+".toRegex())[0]
        providers.exec {
            commandLine("git", "fetch", "https://github.com/ReSukiSU/ReSukiSU.git", "$hash")
        }
        providers.exec {
            commandLine("git", "rev-list", "--count", "$hash")
        }.standardOutput.asText.get().trim().toInt()
    } catch (e: Exception) {
        0
    }
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
