package com.edricchan.studybuddy.ui.theming.compose.theme

import androidx.compose.material3.ColorScheme

abstract class AppColorScheme {
    /** Desired light [ColorScheme] to use. */
    abstract val lightScheme: ColorScheme

    /** Desired dark [ColorScheme] to use. */
    abstract val darkScheme: ColorScheme

    fun getColorScheme(isDark: Boolean): ColorScheme {
        return if (isDark) darkScheme else lightScheme
    }
}
