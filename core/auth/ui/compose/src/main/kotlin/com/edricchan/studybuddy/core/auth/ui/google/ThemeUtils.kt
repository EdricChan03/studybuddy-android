package com.edricchan.studybuddy.core.auth.ui.google

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.luminance

/** Retrieves the associated [ButtonColors] for the receiver [GoogleSignInButtonTheme]. */
@Composable
fun GoogleSignInButtonTheme.getColors(): ButtonColors = when (this) {
    GoogleSignInButtonTheme.Light -> GoogleSignInButtonDefaults.lightColors
    GoogleSignInButtonTheme.Dark -> GoogleSignInButtonDefaults.darkColors
    GoogleSignInButtonTheme.Neutral -> GoogleSignInButtonDefaults.neutralColors
    GoogleSignInButtonTheme.Auto ->
        if (MaterialTheme.colorScheme.background.luminance() > 0.5) GoogleSignInButtonDefaults.lightColors
        else GoogleSignInButtonDefaults.darkColors
}

/** Retrieves the associated [BorderStroke] for the receiver [GoogleSignInButtonTheme]. */
@Composable
fun GoogleSignInButtonTheme.getBorder(): BorderStroke? = when (this) {
    GoogleSignInButtonTheme.Light -> GoogleSignInButtonDefaults.lightBorderStroke
    GoogleSignInButtonTheme.Dark -> GoogleSignInButtonDefaults.darkBorderStroke
    GoogleSignInButtonTheme.Neutral -> null
    GoogleSignInButtonTheme.Auto ->
        if (MaterialTheme.colorScheme.background.luminance() > 0.5) GoogleSignInButtonDefaults.lightBorderStroke
        else GoogleSignInButtonDefaults.darkBorderStroke
}
