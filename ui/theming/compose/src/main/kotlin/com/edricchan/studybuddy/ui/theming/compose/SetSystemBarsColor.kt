package com.edricchan.studybuddy.ui.theming.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Sets the system bars (status and navigation bars) colours.
 * @param color The status bar colour to use.
 * @param useDarkIcons Whether the status bar icons should be darkened.
 */
@Composable
fun SetSystemBarsColor(
    systemUiController: SystemUiController = rememberSystemUiController(),
    color: Color = Color.Transparent,
    useDarkIcons: Boolean = !isSystemInDarkTheme()
) {
    DisposableEffect(systemUiController, useDarkIcons) {
        systemUiController.setSystemBarsColor(color, useDarkIcons)

        onDispose {}
    }
}
