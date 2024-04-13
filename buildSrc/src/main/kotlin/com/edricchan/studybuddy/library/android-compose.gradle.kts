/** Convention plugin for Android libraries made with Jetpack Compose. */
package com.edricchan.studybuddy.library

plugins {
    id("com.edricchan.studybuddy.library.android")
    org.jetbrains.kotlin.plugin.compose
}

android.buildFeatures.compose = true

composeCompiler {
    enableIntrinsicRemember = true

    // Access the metric/reports at "<project>/build/compose-compiler/"
    metricsDestination = layout.buildDirectory.dir("compose-compiler/metrics")
    reportsDestination = layout.buildDirectory.dir("compose-compiler/reports")
}
