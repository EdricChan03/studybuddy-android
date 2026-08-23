package com.edricchan.studybuddy.ui.theming.compose

import android.content.Context
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalRippleThemeConfiguration
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.RippleDefaults
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edricchan.studybuddy.core.settings.appearance.AppThemeSetting
import com.edricchan.studybuddy.core.settings.appearance.DarkModeSetting
import com.edricchan.studybuddy.core.settings.appearance.DarkThemeValue
import com.edricchan.studybuddy.ui.theming.common.ThemePreferences
import com.edricchan.studybuddy.ui.theming.common.dynamic.isDynamicColorAvailable
import com.edricchan.studybuddy.ui.theming.compose.night.shouldApplyDarkTheme
import com.edricchan.studybuddy.ui.theming.compose.spacing.LocalThemeSpacing
import com.edricchan.studybuddy.ui.theming.compose.spacing.SpacingTokens
import com.edricchan.studybuddy.ui.theming.compose.theme.StudyBuddyTypography
import com.edricchan.studybuddy.ui.theming.compose.theme.m3.expressive.StudyBuddyExpressiveDarkColors
import com.edricchan.studybuddy.ui.theming.compose.theme.m3.expressive.StudyBuddyExpressiveLightColors
import com.edricchan.studybuddy.ui.theming.compose.theme.supportsDynamicColor
import com.edricchan.studybuddy.ui.theming.compose.theme.toAppColorScheme
import com.valentinilk.shimmer.LocalShimmerTheme
import com.valentinilk.shimmer.ShimmerTheme
import com.valentinilk.shimmer.defaultShimmerTheme

/**
 * Default implementation for [StudyBuddyTheme]'s `colors` parameter, if not overridden
 * by a custom theme.
 */
@Composable
fun studyBuddyColors(
    context: Context = LocalContext.current,
    enableDarkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicTheme: Boolean,
): ColorScheme = when {
    // useDynamicTheme doesn't imply that the Android device is actually running on
    // Android 12+, so an additional check is required
    useDynamicTheme && supportsDynamicColor ->
        if (enableDarkTheme) dynamicDarkColorScheme(context)
        else dynamicLightColorScheme(context)

    enableDarkTheme -> StudyBuddyExpressiveDarkColors
    else -> StudyBuddyExpressiveLightColors
}

@Composable
fun studyBuddyShimmerTheme(): ShimmerTheme {
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    return defaultShimmerTheme.copy(
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 800,
                easing = LinearEasing,
                delayMillis = 250,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        shaderColors = listOf(
            surfaceVariant.copy(alpha = 0.25f),
            surfaceVariant.copy(alpha = 0.50f),
            surfaceVariant.copy(alpha = 0.25f),
        ),
    )
}

/**
 * Sets the [MaterialTheme] for all the Composables in [content].
 *
 * On Android 12+, the system-wide dynamic colours will be used.
 * @param context The [Context] to be used to retrieve the dynamic colour (Android 12+).
 * @param enableDarkTheme Whether dark theme should be enabled.
 * @param useDynamicTheme Whether the system's wallpaper colour should be used. This defaults to
 * `true` for supported devices (Android 12+), or `false` otherwise.
 * @param colors Desired [ColorScheme] to use - see [studyBuddyColors].
 * @param shimmerTheme Desired [ShimmerTheme] to use for the [com.valentinilk.shimmer.shimmer]
 * modifier.
 * @param baseSpacing Base spacing for [spacingTokens]. Note that setting this has no effect
 * when [spacingTokens] is manually specified.
 * @param spacingTokens The [SpacingTokens] to use.
 * @param typography The [Typography] to use.
 */
@Composable
fun StudyBuddyTheme(
    context: Context = LocalContext.current,
    enableDarkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicTheme: Boolean,
    colors: ColorScheme = studyBuddyColors(
        context = context,
        useDynamicTheme = useDynamicTheme,
        enableDarkTheme = enableDarkTheme
    ),
    shimmerTheme: ShimmerTheme = studyBuddyShimmerTheme(),
    baseSpacing: Dp = SpacingTokens.BaseSpacing,
    spacingTokens: SpacingTokens = SpacingTokens(base = baseSpacing),
    typography: Typography = StudyBuddyTypography,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalRippleThemeConfiguration provides RippleDefaults.InsetFocusRingThemeConfiguration,
        LocalThemeSpacing provides spacingTokens,
        LocalShimmerTheme provides shimmerTheme
    ) {
        MaterialExpressiveTheme(
            colorScheme = colors,
            typography = typography,
            content = content,
            motionScheme = MotionScheme.expressive()
        )
    }
}

/**
 * Sets the [MaterialTheme] for all the Composables in [content].
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
@Deprecated(
    "This overload does not account for the new appearance settings " +
        "data-store. Use the explicit overload instead"
)
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

/**
 * Sets the [MaterialTheme] for all the Composables in [content] based on the specified [appTheme].
 * @param enableDarkTheme Whether the dark theme variant of the desired [appTheme] should be used.
 * @param appTheme The desired [AppThemeSetting] to be used - see [toAppColorScheme] for more info.
 * @param baseSpacing Base spacing for the [SpacingTokens] to be used.
 * @param typography Desired [typographical][Typography] styles to be used.
 */
@Composable
fun StudyBuddyTheme(
    enableDarkTheme: Boolean = isSystemInDarkTheme(),
    appTheme: AppThemeSetting,
    baseSpacing: Dp = SpacingTokens.BaseSpacing,
    typography: Typography = StudyBuddyTypography,
    content: @Composable () -> Unit
) {
    StudyBuddyTheme(
        enableDarkTheme = enableDarkTheme,
        // TODO: Remove useDynamicTheme option when we switch all implementations to this overload
        useDynamicTheme = appTheme == AppThemeSetting.Monet,
        colors = appTheme.toAppColorScheme().getColorScheme(enableDarkTheme),
        baseSpacing = baseSpacing,
        typography = typography,
        content = content,
    )
}

/**
 * Sets the [MaterialTheme] for all the Composables in [content] based on the specified [appTheme].
 * @param darkMode The current [DarkModeSetting] which determines whether dark mode colours should
 * be used - see [shouldApplyDarkTheme].
 * @param appTheme The desired [AppThemeSetting] to be used - see [toAppColorScheme] for more info.
 * @param baseSpacing Base spacing for the [SpacingTokens] to be used.
 * @param typography Desired [typographical][Typography] styles to be used.
 */
@Composable
fun StudyBuddyTheme(
    darkMode: DarkModeSetting,
    appTheme: AppThemeSetting,
    baseSpacing: Dp = SpacingTokens.BaseSpacing,
    typography: Typography = StudyBuddyTypography,
    content: @Composable () -> Unit
) {
    StudyBuddyTheme(
        enableDarkTheme = shouldApplyDarkTheme(darkMode),
        appTheme = appTheme,
        baseSpacing = baseSpacing,
        typography = typography,
        content = content
    )
}
