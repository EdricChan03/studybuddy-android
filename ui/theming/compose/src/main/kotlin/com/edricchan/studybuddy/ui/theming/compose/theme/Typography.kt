package com.edricchan.studybuddy.ui.theming.compose.theme

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.edricchan.studybuddy.ui.theming.common.R as ThemingR

val funnelDisplayFamily = FontFamily(
    Font(ThemingR.font.funnel_display_bold, FontWeight.Bold),
    Font(ThemingR.font.funnel_display_medium, FontWeight.Medium),
    Font(ThemingR.font.funnel_display_regular, FontWeight.Normal),
)

val baloo2Family = FontFamily(
    Font(ThemingR.font.baloo2_bold, FontWeight.Bold),
    Font(ThemingR.font.baloo2_medium, FontWeight.Medium),
    Font(ThemingR.font.baloo2_regular, FontWeight.Normal)
)

// TODO: Remove when https://issuetracker.google.com/issues/236358112 is fixed
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun Typography.defaultFontFamily(
    displayFontFamily: FontFamily,
    bodyFontFamily: FontFamily
): Typography = this.copy(
    displayLarge = this.displayLarge.copy(fontFamily = displayFontFamily),
    displayLargeEmphasized = this.displayLargeEmphasized.copy(fontFamily = displayFontFamily),
    displayMedium = this.displayMedium.copy(fontFamily = displayFontFamily),
    displayMediumEmphasized = this.displayMediumEmphasized.copy(fontFamily = displayFontFamily),
    displaySmall = this.displaySmall.copy(fontFamily = displayFontFamily),
    displaySmallEmphasized = this.displaySmallEmphasized.copy(fontFamily = displayFontFamily),
    headlineLarge = this.headlineLarge.copy(fontFamily = displayFontFamily),
    headlineLargeEmphasized = this.headlineLargeEmphasized.copy(fontFamily = displayFontFamily),
    headlineMedium = this.headlineMedium.copy(fontFamily = displayFontFamily),
    headlineMediumEmphasized = this.headlineMediumEmphasized.copy(fontFamily = displayFontFamily),
    headlineSmall = this.headlineSmall.copy(fontFamily = displayFontFamily),
    headlineSmallEmphasized = this.headlineSmallEmphasized.copy(fontFamily = displayFontFamily),
    titleLarge = this.titleLarge.copy(fontFamily = displayFontFamily),
    titleLargeEmphasized = this.titleLargeEmphasized.copy(fontFamily = displayFontFamily),
    titleMedium = this.titleMedium.copy(fontFamily = displayFontFamily),
    titleMediumEmphasized = this.titleMediumEmphasized.copy(fontFamily = displayFontFamily),
    titleSmall = this.titleSmall.copy(fontFamily = displayFontFamily),
    titleSmallEmphasized = this.titleSmallEmphasized.copy(fontFamily = displayFontFamily),
    bodyLarge = this.bodyLarge.copy(fontFamily = bodyFontFamily),
    bodyLargeEmphasized = this.bodyLargeEmphasized.copy(fontFamily = bodyFontFamily),
    bodyMedium = this.bodyMedium.copy(fontFamily = bodyFontFamily),
    bodyMediumEmphasized = this.bodyMediumEmphasized.copy(fontFamily = bodyFontFamily),
    bodySmall = this.bodySmall.copy(fontFamily = bodyFontFamily),
    bodySmallEmphasized = this.bodySmallEmphasized.copy(fontFamily = bodyFontFamily),
    labelLarge = this.labelLarge.copy(fontFamily = displayFontFamily),
    labelLargeEmphasized = this.labelLargeEmphasized.copy(fontFamily = displayFontFamily),
    labelMedium = this.labelMedium.copy(fontFamily = displayFontFamily),
    labelMediumEmphasized = this.labelMediumEmphasized.copy(fontFamily = displayFontFamily),
    labelSmall = this.labelSmall.copy(fontFamily = displayFontFamily),
    labelSmallEmphasized = this.labelSmallEmphasized.copy(fontFamily = displayFontFamily),
)

/**
 * Creates the [Typography] styles for the app with the font-families set for the relevant styles.
 * @param displayFontFamily The [FontFamily] to be applied to all display [Typography] styles.
 * @param bodyFontFamily The [FontFamily] to be applied to all body [Typography] styles.
 */

fun StudyBuddyTypography(
    displayFontFamily: FontFamily,
    bodyFontFamily: FontFamily
) = Typography().defaultFontFamily(displayFontFamily, bodyFontFamily)

/**
 * The default [Typography] style to use, where the [Funnel Display][funnelDisplayFamily] and
 * [Baloo 2][baloo2Family] type-faces are applied to the display and body styles respectively.
 */
val StudyBuddyTypography = StudyBuddyTypography(
    displayFontFamily = funnelDisplayFamily,
    bodyFontFamily = baloo2Family,
)
