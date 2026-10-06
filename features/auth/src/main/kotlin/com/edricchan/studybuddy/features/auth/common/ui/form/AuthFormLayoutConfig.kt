package com.edricchan.studybuddy.features.auth.common.ui.form

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass

data class AuthFormLayoutConfig(
    val useRowLayout: Boolean,
    val useFullSizeCardLayout: Boolean
) {
    companion object {
        fun fromWindowSizeClass(windowSizeClass: WindowSizeClass): AuthFormLayoutConfig {
            val isCompactWidth = !windowSizeClass.isWidthAtLeastBreakpoint(
                WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
            )
            val isShortHeight = !windowSizeClass.isHeightAtLeastBreakpoint(
                WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
            )
            return AuthFormLayoutConfig(
                useRowLayout = isShortHeight,
                useFullSizeCardLayout = isCompactWidth
            )
        }
    }
}

@Composable
fun currentAuthFormLayoutConfig(): AuthFormLayoutConfig =
    AuthFormLayoutConfig.fromWindowSizeClass(currentWindowAdaptiveInfoV2().windowSizeClass)
