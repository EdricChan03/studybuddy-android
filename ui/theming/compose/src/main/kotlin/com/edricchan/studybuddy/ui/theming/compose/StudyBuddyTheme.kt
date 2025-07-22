package com.edricchan.studybuddy.ui.theming.compose

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edricchan.studybuddy.core.settings.appearance.DarkThemeValue
import com.edricchan.studybuddy.ui.theming.common.ThemePreferences
import com.edricchan.studybuddy.ui.theming.common.dynamic.isDynamicColorAvailable
import com.edricchan.studybuddy.ui.theming.compose.night.shouldApplyDarkTheme
import com.edricchan.studybuddy.ui.theming.compose.theme.StudyBuddyTypography
import com.edricchan.studybuddy.ui.theming.compose.theme.m3.expressive.StudyBuddyExpressiveDarkColors
import com.edricchan.studybuddy.ui.theming.compose.theme.m3.expressive.StudyBuddyExpressiveLightColors
import com.edricchan.studybuddy.ui.theming.compose.theme.supportsDynamicColor

/**
 * Sets the [MaterialTheme] for all of the Composables in [content].
 *
 * On Android 12+, the system-wide dynamic colours will be used.
 * @param context The [Context] to be used to retrieve the dynamic colour (Android 12+).
 * @param enableDarkTheme Whether dark theme should be enabled.
 * @param useDynamicTheme Whether the system's wallpaper colour should be used. This defaults to
 * `true` for supported devices (Android 12+), or `false` otherwise.
 * @param typography The [Typography] to use.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun StudyBuddyTheme(
    context: Context = LocalContext.current,
    enableDarkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicTheme: Boolean,
    typography: Typography = StudyBuddyTypography,
    content: @Composable () -> Unit
) {
    // Dynamic color is available on Android 12+
    val colors = when {
        // useDynamicTheme doesn't imply that the Android device is actually running on
        // Android 12+, so an additional check is required
        useDynamicTheme && supportsDynamicColor ->
            if (enableDarkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)

        enableDarkTheme -> StudyBuddyExpressiveDarkColors
        else -> StudyBuddyExpressiveLightColors
    }

    MaterialExpressiveTheme(
        colorScheme = colors,
        typography = typography,
        content = content,
        motionScheme = MotionScheme.expressive()
    )
}

/**
 * Sets the [MaterialTheme] for all of the Composables in [content].
 *
 * On Android 12+, the system-wide dynamic colours will be used.
 *
 * This overload uses the values from [ThemePreferences], where their values are automatically
 * collected as a Compose state.
 * @param context The [Context] to be used to retrieve the dynamic colour (Android 12+).
 * @param themePrefs [ThemePreferences] object to retrieve the [ThemePreferences.prefDarkTheme]
 * and [ThemePreferences.prefEnableDynamicTheme] values from.
 * @param typography The [Typography] to use.
 */
@Composable
fun StudyBuddyTheme(
    context: Context = LocalContext.current,
    themePrefs: ThemePreferences = ThemePreferences(context),
    typography: Typography = StudyBuddyTypography,
    content: @Composable () -> Unit
) {
    val darkTheme by themePrefs.prefDarkTheme.asFlow()
        .collectAsStateWithLifecycle(initialValue = DarkThemeValue.V2FollowSystem)
    val dynamicTheme by themePrefs.prefEnableDynamicTheme.asFlow().collectAsStateWithLifecycle(
        initialValue = isDynamicColorAvailable
    )

    StudyBuddyTheme(
        context = context,
        enableDarkTheme = shouldApplyDarkTheme(themeValue = darkTheme),
        useDynamicTheme = dynamicTheme,
        typography = typography,
        content = content
    )
}
