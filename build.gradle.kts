// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
    }

    dependencies {
        // Declared here such that a custom reporter can be used
        classpath(libs.gradleVersions.gradle)
    }
}

plugins {
    alias(libs.plugins.kotlin.plugin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.dagger.hilt) apply false
    alias(libs.plugins.dependencyAnalysis)
    id(libs.plugins.gradleVersions.get().pluginId)
    alias(libs.plugins.testAggregation.results)
    alias(libs.plugins.aboutLibraries) apply false
}

tasks {
    val clean by registering(Delete::class) {
        delete(rootProject.layout.buildDirectory)
    }
}

apply<com.edricchan.studybuddy.gradle.versions.DependencyUpdatesPlugin>()

dependencyAnalysis {
    // See https://github.com/autonomousapps/dependency-analysis-android-gradle-plugin/wiki/Customizing-plugin-behavior#ktx-dependencies
    issues {
        structure {
            ignoreKtx(true)
        }
    }
}
