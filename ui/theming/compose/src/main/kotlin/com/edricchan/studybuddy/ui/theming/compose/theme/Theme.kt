package com.edricchan.studybuddy.ui.theming.compose.theme

import android.content.Context
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/** Whether dynamic colour theming is supported. */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
val supportsDynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

/**
 * Sets the [MaterialTheme] for all of the Composables in [content].
 *
 * On Android 12+, the system-wide dynamic colours will be used.
 * @param context The [Context] to be used to retrieve the dynamic colour (Android 12+).
 * @param enableDarkTheme Whether dark theme should be enabled.
 */
@Composable
fun StudyBuddyTheme(
    context: Context = LocalContext.current,
    enableDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Dynamic color is available on Android 12+
    val colors = when {
        supportsDynamicColor && enableDarkTheme -> dynamicDarkColorScheme(context)
        supportsDynamicColor && !enableDarkTheme -> dynamicLightColorScheme(context)
        enableDarkTheme -> StudyBuddyDarkColors
        else -> StudyBuddyLightColors
    }

    MaterialTheme(colorScheme = colors, typography = StudyBuddyTypography, content = content)
}
