package com.edricchan.studybuddy.ui.theming.compose.theme

import io.kotest.core.spec.style.FunSpec

class TypographyUnitTest : FunSpec({
    test("Baloo font is applied to all text-styles") {
        StudyBuddyTypography shouldUseFontFamily baloo2Family
    }
})

