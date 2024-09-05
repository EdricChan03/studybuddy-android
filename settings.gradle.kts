rootProject.name = "studybuddy-android"

// Repository settings
// (See https://developer.android.com/studio/releases/gradle-plugin#settings-gradle)
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("""com\.android.*""")
                includeGroupByRegex("""com\.google.*""")
                // Newer versions of KSP are published on Maven Central
                excludeGroup("com.google.devtools.ksp")
                includeGroupByRegex("androidx.*")
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

plugins {
    // Used for Gradle's Build Scan feature - see
    // https://docs.gradle.com/enterprise/gradle-plugin/
    @Suppress("SpellCheckingInspection")
    // "develocity" is the branding for Gradle's build tooling
    id("com.gradle.develocity") version "3.18"
    // Gradle JVM Toolchains repository - see
    // https://docs.gradle.org/8.7/userguide/toolchains.html#sub:download_repositories
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"

    id("com.android.settings") version "8.7.0-alpha09"
}

val isCi = !System.getenv("CI").isNullOrEmpty()
val isGitHubActions = !System.getenv("GITHUB_ACTIONS").isNullOrEmpty()

// Configure the Gradle Enterprise Plugin
develocity.buildScan {
    termsOfUseUrl = "https://gradle.com/terms-of-service"
    termsOfUseAgree = "yes"

    if (isCi) tag("CI")

    // GitHub Actions-specific config
    if (isGitHubActions) {
        tag("GitHub Actions")
        tag("${System.getenv("RUNNER_OS")}-${System.getenv("RUNNER_ARCH")}")

        val actionRunRefName = System.getenv("GITHUB_REF_NAME")
        value("Workflow run ref name", actionRunRefName)
        value("Workflow run ref type", System.getenv("GITHUB_REF_TYPE"))

        val repoUrl =
            "${System.getenv("GITHUB_SERVER_URL")}/${System.getenv("GITHUB_REPOSITORY")}"

        val commitSha = System.getenv("GITHUB_SHA")
        link("View commit", "$repoUrl/commit/$commitSha")
        link("View source at commit", "$repoUrl/tree/$commitSha")
        link("View source at branch/tag", "$repoUrl/tree/$actionRunRefName")
        value("Git commit SHA", commitSha)

        val actionRunRef = System.getenv("GITHUB_REF")
        val prNumber = Regex("""refs/pull/(\d+)/merge""")
            .find(actionRunRef)?.groupValues?.first()
        if (prNumber != null) {
            tag("Pull Request")
            link("View pull request", "$repoUrl/pull/$prNumber")
            value("GitHub pull request number", prNumber)
        }
        val tagName = Regex("""refs/tags/(.+)""")
            .find(actionRunRef)?.groupValues?.first()
        if (tagName != null) {
            tag("Release")
            link("View tag", "$repoUrl/releases/tag/$tagName")
        }

        val actionRunId = System.getenv("GITHUB_RUN_ID")
        link("View workflow run", "$repoUrl/actions/runs/$actionRunId")
        value("Workflow run attempts", System.getenv("GITHUB_RUN_ATTEMPT"))
        value("Workflow run ID", actionRunId)
        value("Workflow run number", System.getenv("GITHUB_RUN_NUMBER"))
    }
}

android {
    buildToolsVersion = "35.0.0"
    compileSdk = 35
    minSdk = 21
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":ui:common",
    ":ui:insets",
    ":ui:preference",
    ":ui:preference:compose",
    ":ui:theming:common",
    ":ui:theming:compose",
    ":ui:theming:views",
    ":ui:widgets:compose",
    ":ui:widgets:views",
    ":ui:widgets:compose:navigation",
    ":ui:widgets:compose:option-bottom-sheet",
    ":ui:widgets:modal-bottom-sheet",
    ":core:auth:ui:compose",
    ":core:auth:gms",
    ":core:compat:navigation",
    ":core:deeplink",
    ":core:di",
    ":core:resources",
    ":core:resources:temporal",
    ":core:settings:appearance",
    ":core:settings:tasks",
    ":data:common",
    ":data:serialization:android",
    ":utils:dev-mode",
    ":utils:network",
    ":utils:recyclerview",
    ":utils:web",
    ":exts:android",
    ":exts:androidx:compose",
    ":exts:androidx:fragment",
    ":exts:androidx:preference",
    ":exts:androidx:preference-files",
    ":exts:androidx:view-binding",
    ":exts:coil",
    ":exts:common",
    ":exts:datetime",
    ":exts:firebase",
    ":exts:markwon",
    ":exts:material",
    ":features:help",
    ":features:auth",
    ":features:settings",
    ":features:tasks",
    ":app"
)
