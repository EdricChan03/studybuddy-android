package com.edricchan.studybuddy.ui.theming.compose.theme

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import io.kotest.core.spec.style.FunSpec

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
class TypographyUnitTest : FunSpec({
    test("Baloo font is applied to all body text-styles") {
        with(StudyBuddyTypography) {
            bodyLarge shouldUseFontFamily baloo2Family
            bodyLargeEmphasized shouldUseFontFamily baloo2Family
            bodyMedium shouldUseFontFamily baloo2Family
            bodyMediumEmphasized shouldUseFontFamily baloo2Family
            bodySmall shouldUseFontFamily baloo2Family
            bodySmallEmphasized shouldUseFontFamily baloo2Family
        }
    }

    test("Funnel Display font is applied to all non-body text-styles") {
        with(StudyBuddyTypography) {
            displayLarge shouldUseFontFamily funnelDisplayFamily
            displayLargeEmphasized shouldUseFontFamily funnelDisplayFamily
            displayMedium shouldUseFontFamily funnelDisplayFamily
            displayMediumEmphasized shouldUseFontFamily funnelDisplayFamily
            displaySmall shouldUseFontFamily funnelDisplayFamily
            displaySmallEmphasized shouldUseFontFamily funnelDisplayFamily
            headlineLarge shouldUseFontFamily funnelDisplayFamily
            headlineLargeEmphasized shouldUseFontFamily funnelDisplayFamily
            headlineMedium shouldUseFontFamily funnelDisplayFamily
            headlineMediumEmphasized shouldUseFontFamily funnelDisplayFamily
            headlineSmall shouldUseFontFamily funnelDisplayFamily
            headlineSmallEmphasized shouldUseFontFamily funnelDisplayFamily
            titleLarge shouldUseFontFamily funnelDisplayFamily
            titleLargeEmphasized shouldUseFontFamily funnelDisplayFamily
            titleMedium shouldUseFontFamily funnelDisplayFamily
            titleMediumEmphasized shouldUseFontFamily funnelDisplayFamily
            titleSmall shouldUseFontFamily funnelDisplayFamily
            titleSmallEmphasized shouldUseFontFamily funnelDisplayFamily
            labelLarge shouldUseFontFamily funnelDisplayFamily
            labelLargeEmphasized shouldUseFontFamily funnelDisplayFamily
            labelMedium shouldUseFontFamily funnelDisplayFamily
            labelMediumEmphasized shouldUseFontFamily funnelDisplayFamily
            labelSmall shouldUseFontFamily funnelDisplayFamily
            labelSmallEmphasized shouldUseFontFamily funnelDisplayFamily
        }
    }
})

