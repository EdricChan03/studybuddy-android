/** Convention plugin for Android libraries or applications made with Jetpack Compose. */
package com.edricchan.studybuddy.common

import com.android.build.api.dsl.CommonExtension

plugins {
    org.jetbrains.kotlin.plugin.compose
}

extensions.findByType(CommonExtension::class)?.apply {
    buildFeatures.compose = true
}

composeCompiler {
    includeSourceInformation = true
    // Access the metric/reports at "<project>/build/compose-compiler/"
    metricsDestination = layout.buildDirectory.dir("compose-compiler/metrics")
    reportsDestination = layout.buildDirectory.dir("compose-compiler/reports")
}
