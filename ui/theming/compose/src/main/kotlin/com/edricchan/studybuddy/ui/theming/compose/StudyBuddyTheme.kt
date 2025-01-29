package com.edricchan.studybuddy.ui.theming.compose

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.edricchan.studybuddy.ui.theming.compose.theme.StudyBuddyTypography
import com.edricchan.studybuddy.ui.theming.compose.theme.compat.StudyBuddyCompatDarkColors
import com.edricchan.studybuddy.ui.theming.compose.theme.compat.StudyBuddyCompatLightColors
import com.edricchan.studybuddy.ui.theming.compose.theme.m3.StudyBuddyM3DarkColors
import com.edricchan.studybuddy.ui.theming.compose.theme.m3.StudyBuddyM3LightColors
import com.edricchan.studybuddy.ui.theming.compose.theme.supportsDynamicColor

/**
 * Retrieves the dark colour scheme to use.
 * @param useM3Colors Whether the [M3 dark colours][StudyBuddyM3DarkColors] should
 * be used instead of the [compat dark colours][StudyBuddyCompatDarkColors].
 */
fun darkColorScheme(useM3Colors: Boolean = true) =
    if (useM3Colors) StudyBuddyM3DarkColors else StudyBuddyCompatDarkColors

/**
 * Retrieves the light colour scheme to use.
 * @param useM3Colors Whether the [M3 light colours][StudyBuddyM3LightColors] should
 * be used instead of the [compat light colours][StudyBuddyCompatLightColors].
 */
fun lightColorScheme(useM3Colors: Boolean = true) =
    if (useM3Colors) StudyBuddyM3LightColors else StudyBuddyCompatLightColors

/**
 * Sets the [MaterialTheme] for all of the Composables in [content].
 *
 * On Android 12+, the system-wide dynamic colours will be used.
 * @param context The [Context] to be used to retrieve the dynamic colour (Android 12+).
 * @param enableDarkTheme Whether dark theme should be enabled.
 * @param useDynamicTheme Whether the system's wallpaper colour should be used. This defaults to
 * `true` for supported devices (Android 12+), or `false` otherwise.
 * @param useM3Colors Whether the M3 colours ([StudyBuddyM3LightColors],
 * [StudyBuddyM3DarkColors]) should be used instead of [StudyBuddyCompatLightColors] and
 * [StudyBuddyCompatDarkColors] when [dynamic theming][useDynamicTheme] is disabled.
 * @param typography The [Typography] to use.
 */
@Composable
fun StudyBuddyTheme(
    context: Context = LocalContext.current,
    enableDarkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicTheme: Boolean = supportsDynamicColor,
    useM3Colors: Boolean = true,
    typography: Typography = StudyBuddyTypography,
    content: @Composable () -> Unit
) {
    // Dynamic color is available on Android 12+
    val darkColor = darkColorScheme(useM3Colors)
    val lightColor = lightColorScheme(useM3Colors)
    val colors = when {
        // useDynamicTheme doesn't imply that the Android device is actually running on
        // Android 12+, so an additional check is required
        useDynamicTheme && supportsDynamicColor ->
            if (enableDarkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)

        enableDarkTheme -> darkColor
        else -> lightColor
    }

    MaterialTheme(colorScheme = colors, typography = typography, content = content)
}
