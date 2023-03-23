// Repository settings
// (See https://developer.android.com/studio/releases/gradle-plugin#settings-gradle)
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

plugins {
    // Used for Gradle's Build Scan feature - see
    // https://docs.gradle.com/enterprise/gradle-plugin/
    id("com.gradle.enterprise") version "3.12.3"
    // Gradle JVM Toolchains repository - see
    // https://docs.gradle.org/8.0-rc-2/userguide/toolchains.html#sub:download_repositories
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}

// Configure the Gradle Enterprise Plugin
gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"

        // Only publish build scans on CI
        if (!System.getenv("CI").isNullOrEmpty()) {
            publishAlways()
            tag("CI")
        }

        // GitHub Actions-specific config
        if (!System.getenv("GITHUB_ACTIONS").isNullOrEmpty()) {
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
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":ui:common",
    ":ui:preference",
    ":ui:theming",
    ":ui:theming:compose",
    ":ui:widgets",
    ":core:deeplink",
    ":core:di",
    ":core:resources",
    ":data:serialization:android",
    ":utils:recyclerview",
    ":utils:web",
    ":exts:common",
    ":exts:material",
    ":feature:help",
    ":app"
)
