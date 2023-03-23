/** Convention plugin for Android libraries made with Jetpack Compose. */
package com.edricchan.studybuddy.library

plugins {
    id("com.edricchan.studybuddy.library.android")
}

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = "1.4.4"
}
