// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
    }

    dependencies {
        // (Plugin marker for OSS Licenses doesn't exist yet)
        classpath(libs.playServices.ossLicenses.gradle)
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.gradle.application) apply false
    alias(libs.plugins.android.gradle.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.gradleVersions)
    alias(libs.plugins.gradleVersions.catalog)
}

versionCatalogUpdate {
    // Don't sort by keys to preserve comments
    sortByKey.set(false)
}

tasks {
    val clean by registering(Delete::class) {
        delete(rootProject.buildDir)
    }
}
