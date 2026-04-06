package com.edricchan.studybuddy.ui.theming.compose.theme.font

import androidx.compose.ui.text.font.FontFamily
import com.edricchan.studybuddy.core.settings.appearance.font.TypefaceSetting
import com.edricchan.studybuddy.ui.theming.compose.theme.baloo2Family
import com.edricchan.studybuddy.ui.theming.compose.theme.funnelDisplayFamily

/**
 * Converts the receiver [TypefaceSetting] model to its Compose
 * [FontFamily] equivalent.
 */
fun TypefaceSetting.toFontFamily(): FontFamily = when (this) {
    TypefaceSetting.Baloo2 -> baloo2Family
    TypefaceSetting.FunnelDisplay -> funnelDisplayFamily
    TypefaceSetting.SystemDefault -> FontFamily.Default
    TypefaceSetting.SystemSerif -> FontFamily.Serif
    TypefaceSetting.SystemMonospace -> FontFamily.Monospace
    TypefaceSetting.SystemSansSerif -> FontFamily.SansSerif
}
