/** Convention plugin for Android libraries made with Jetpack Compose. */
package com.edricchan.studybuddy.library

plugins {
    id("com.edricchan.studybuddy.library.android")
}

val compilerVersion = versionCatalogs.named("libs").findVersion("androidx-compose-compiler")
    .map { it.requiredVersion }
    .orElse("1.5.6")

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = compilerVersion
}
