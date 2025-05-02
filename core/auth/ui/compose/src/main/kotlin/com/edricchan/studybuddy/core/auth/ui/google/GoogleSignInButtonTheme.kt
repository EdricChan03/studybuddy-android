package com.edricchan.studybuddy.core.auth.ui.google

enum class GoogleSignInButtonTheme {
    /**
     * Applies [GoogleSignInButtonDefaults.lightColors] and a
     * [GoogleSignInButtonDefaults.lightBorderStroke] to the [GoogleSignInButton].
     */
    Light,

    /**
     * Applies [GoogleSignInButtonDefaults.darkColors] and a
     * [GoogleSignInButtonDefaults.darkBorderStroke] to the [GoogleSignInButton].
     */
    Dark,

    /** Applies [GoogleSignInButtonDefaults.neutralColors] to the [GoogleSignInButton]. */
    Neutral,

    /**
     * Use the appropriate theme ([Light] or [Dark]) according to whether the
     * [androidx.compose.material3.MaterialTheme] is in dark mode.
     */
    Auto
}
