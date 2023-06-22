/** Convention plugin for Android libraries. */
package com.edricchan.studybuddy.library

plugins {
    com.android.library
    org.jetbrains.kotlin.android
}

kotlin {
    jvmToolchain(17)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}
