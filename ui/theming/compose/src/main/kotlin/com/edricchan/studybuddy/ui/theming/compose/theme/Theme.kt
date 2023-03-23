package com.edricchan.studybuddy.ui.theming.compose.theme

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

@Composable
fun StudyBuddyTheme(content: @Composable () -> Unit) {
    // Dynamic color is available on Android 12+
    val darkTheme = isSystemInDarkTheme()
    val colors = when {
        supportsDynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
        supportsDynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
        darkTheme -> StudyBuddyDarkColors
        else -> StudyBuddyLightColors
    }

    MaterialTheme(colorScheme = colors, typography = StudyBuddyTypography, content = content)
}
