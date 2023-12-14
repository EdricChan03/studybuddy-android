import com.android.build.api.dsl.SettingsExtension

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
    `gradle-enterprise` version "3.14.1"
    // Gradle JVM Toolchains repository - see
    // https://docs.gradle.org/8.0-rc-2/userguide/toolchains.html#sub:download_repositories
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"

    id("com.android.settings") version "8.3.0-alpha18"
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

// TODO: configure<...> call is a workaround for https://github.com/gradle/gradle/issues/11210,
//   remove call when issue is fixed
configure<SettingsExtension> {
    buildToolsVersion = "34.0.0"
    compileSdk = 34
    minSdk = 21
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":ui:common",
    ":ui:insets",
    ":ui:preference",
    ":ui:theming:compose",
    ":ui:theming:views",
    ":ui:widgets:compose",
    ":ui:widgets:views",
    ":ui:widgets:compose:about-libraries",
    ":ui:widgets:compose:option-bottom-sheet",
    ":ui:widgets:modal-bottom-sheet",
    ":core:deeplink",
    ":core:di",
    ":core:resources",
    ":data:common",
    ":data:serialization:android",
    ":utils:dev-mode",
    ":utils:recyclerview",
    ":utils:web",
    ":exts:android",
    ":exts:androidx:preference",
    ":exts:androidx:preference-files",
    ":exts:common",
    ":exts:datetime",
    ":exts:firebase",
    ":exts:markwon",
    ":exts:material",
    ":features:help",
    ":features:tasks",
    ":app"
)
