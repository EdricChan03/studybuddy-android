package com.edricchan.studybuddy.ui.theming.common.night

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.edricchan.studybuddy.core.settings.appearance.DarkThemeValue
import com.edricchan.studybuddy.core.settings.appearance.proto.DarkModeSetting

/** Checks if the receiver [Resources] has dark theme enabled. */
val Resources.isDarkThemeEnabled: Boolean
    get() = (configuration.uiMode and
        Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

/**
 * Checks if the receiver [Context] is using dark theme.
 * @see [Resources.isDarkThemeEnabled]
 */
val Context.isDarkThemeEnabled: Boolean get() = resources.isDarkThemeEnabled

/**
 * Whether dark theme should be applied based on the value of [themeValue].
 * @param themeValue The current [DarkThemeValue.Version2].
 * @param isSystemInDarkTheme Whether the system is using dark theme.
 */
@Suppress("DEPRECATION")
fun Context.shouldApplyDarkTheme(
    themeValue: DarkThemeValue.Version2,
    isSystemInDarkTheme: Boolean = isDarkThemeEnabled
): Boolean = when (themeValue) {
    DarkThemeValue.V2Never -> false
    DarkThemeValue.V2Always -> true
    DarkThemeValue.V2FollowSystem -> isSystemInDarkTheme
}

/**
 * Whether dark theme should be applied based on the value of [themeSetting].
 * @param themeSetting The current [DarkModeSetting].
 * @param isSystemInDarkTheme Whether the system is using dark theme.
 */
fun shouldApplyDarkTheme(
    themeSetting: DarkModeSetting,
    isSystemInDarkTheme: Boolean
): Boolean = when (themeSetting) {
    DarkModeSetting.AlwaysOff -> false
    DarkModeSetting.AlwaysOn -> true
    DarkModeSetting.FollowSystem -> isSystemInDarkTheme
    else -> true
}
