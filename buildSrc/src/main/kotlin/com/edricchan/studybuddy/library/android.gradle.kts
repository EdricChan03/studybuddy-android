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
}
