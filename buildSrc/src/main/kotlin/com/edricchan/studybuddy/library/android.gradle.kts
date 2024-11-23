/** Convention plugin for Android libraries. */
package com.edricchan.studybuddy.library

plugins {
    com.android.library
    org.jetbrains.kotlin.android
    id("com.edricchan.studybuddy.library.common")
}

kotlin {
    jvmToolchain(17)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions.targetSdk = 34
}

private val libs = versionCatalogs.named("libs")

dependencies {
    androidTestImplementation(libs.findLibrary("kotlin-test").get())
    androidTestImplementation(libs.findLibrary("androidx-test-core-ktx").get())
    androidTestImplementation(libs.findLibrary("androidx-test-runner").get())
    androidTestImplementation(libs.findLibrary("androidx-test-rules").get())
}
