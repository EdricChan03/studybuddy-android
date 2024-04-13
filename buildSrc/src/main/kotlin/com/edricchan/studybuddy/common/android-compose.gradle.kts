/** Convention plugin for Android libraries or applications made with Jetpack Compose. */
package com.edricchan.studybuddy.common

import com.android.build.api.dsl.CommonExtension

plugins {
    org.jetbrains.kotlin.plugin.compose
}

extensions.findByType<CommonExtension<*, *, *, *, *, *>>()?.apply {
    buildFeatures.compose = true
}

private val kotlinVersion = versionCatalogs.named("libs")
    .findVersion("kotlin").map { it.requiredVersion }
    .orElse("2.0.0-RC1")

composeCompiler {
    enableIntrinsicRemember = true

    // Access the metric/reports at "<project>/build/compose-compiler/"
    metricsDestination = layout.buildDirectory.dir("compose-compiler/metrics")
    reportsDestination = layout.buildDirectory.dir("compose-compiler/reports")

    // TODO: Remove workaround for https://youtrack.jetbrains.com/issue/KT-67216
    suppressKotlinVersionCompatibilityCheck = kotlinVersion
}
