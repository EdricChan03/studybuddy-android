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
        // (AppUpdater is only published on JCenter)
        jcenter()
    }
}

plugins {
    // Used for Gradle's Build Scan feature - see
    // https://docs.gradle.com/enterprise/gradle-plugin/
    id("com.gradle.enterprise") version("3.8.1")
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
    ":app"
)
