package com.edricchan.studybuddy.ui.theming.compose.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.edricchan.studybuddy.ui.theming.R as ThemingR

// FIXME: defaultFontFamily can't be used, see https://issuetracker.google.com/issues/236358112
val baloo2Family = FontFamily(
    Font(ThemingR.font.baloo2_bold, FontWeight.Bold),
    Font(ThemingR.font.baloo2_medium, FontWeight.Medium),
    Font(ThemingR.font.baloo2_regular, FontWeight.Normal)
)

private val defaultTypography = Typography()

val StudyBuddyTypography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = baloo2Family),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = baloo2Family),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = baloo2Family),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = baloo2Family),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = baloo2Family),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = baloo2Family),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = baloo2Family),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = baloo2Family),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = baloo2Family),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = baloo2Family),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = baloo2Family),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = baloo2Family),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = baloo2Family),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = baloo2Family),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = baloo2Family),
)
