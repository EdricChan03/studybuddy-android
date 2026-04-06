package com.edricchan.studybuddy.ui.theming.compose.theme.font

import androidx.compose.ui.text.font.FontFamily
import com.edricchan.studybuddy.core.settings.appearance.font.TypefaceConfig
import com.edricchan.studybuddy.ui.theming.compose.theme.baloo2Family
import com.edricchan.studybuddy.ui.theming.compose.theme.funnelDisplayFamily

/**
 * The list of desired [font families][FontFamily] to be used for the
 * Compose-backed user interface.
 *
 * For the data equivalent, use [TypefaceConfig] instead.
 * @property displayFontFamily Desired [FontFamily] for heading-like text.
 * @property bodyFontFamily Desired [FontFamily] for body text.
 */
data class FontFamilyConfig(
    val displayFontFamily: FontFamily,
    val bodyFontFamily: FontFamily,
) {
    companion object {
        /** The default value of [FontFamilyConfig]. */
        val Default = FontFamilyConfig(
            displayFontFamily = funnelDisplayFamily, bodyFontFamily = baloo2Family
        )
    }
}

/** Converts the receiver [TypefaceConfig] to its [FontFamilyConfig] equivalent. */
fun TypefaceConfig.toFontFamilyConfig() = FontFamilyConfig(
    displayFontFamily = this.displayStyle.toFontFamily(),
    bodyFontFamily = this.bodyStyle.toFontFamily(),
)
