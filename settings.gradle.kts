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
    id("com.gradle.enterprise") version "3.12.2"
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
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":ui:common",
    ":ui:preference",
    ":ui:theming",
    ":ui:widgets",
    ":core:deeplink",
    ":core:di",
    ":data:serialization:android",
    ":utils:recyclerview",
    ":utils:web",
    ":exts:common",
    ":exts:material",
    ":feature:help",
    ":app"
)
