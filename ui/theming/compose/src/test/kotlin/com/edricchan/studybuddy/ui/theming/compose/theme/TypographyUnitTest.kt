package com.edricchan.studybuddy.ui.theming.compose.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import kotlin.test.Test
import kotlin.test.assertEquals

class TypographyUnitTest {
    @Test
    fun appliesBalooFont() {
        assertFontFamilyEquals(StudyBuddyTypography, baloo2Family)
    }
}

private fun assertFontFamilyEquals(typography: Typography, family: FontFamily) {
    with(typography) {
        assertEquals(displayLarge.fontFamily, family)
        assertEquals(displayMedium.fontFamily, family)
        assertEquals(displaySmall.fontFamily, family)
        assertEquals(headlineLarge.fontFamily, family)
        assertEquals(headlineMedium.fontFamily, family)
        assertEquals(headlineSmall.fontFamily, family)
        assertEquals(titleLarge.fontFamily, family)
        assertEquals(titleMedium.fontFamily, family)
        assertEquals(titleSmall.fontFamily, family)
        assertEquals(bodyLarge.fontFamily, family)
        assertEquals(bodyMedium.fontFamily, family)
        assertEquals(bodySmall.fontFamily, family)
        assertEquals(labelLarge.fontFamily, family)
        assertEquals(labelMedium.fontFamily, family)
        assertEquals(labelSmall.fontFamily, family)
    }
}
