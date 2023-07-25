package com.edricchan.studybuddy.utils.web

import android.content.Context
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.google.android.material.color.MaterialColors
import io.github.edricchan03.androidx.browser.ktx.enums.ColorScheme
import io.github.edricchan03.androidx.browser.ktx.enums.ShareState
import io.github.edricchan03.androidx.browser.ktx.setColorScheme
import io.github.edricchan03.androidx.browser.ktx.setDefaultColorSchemeParams
import io.github.edricchan03.androidx.browser.ktx.setShareState

@get:ColorInt
private val Context.dynamicColorPrimary
    get() = MaterialColors.getColor(
        this,
        R.attr.colorPrimary,
        ContextCompat.getColor(this, R.color.colorPrimary)
    )

/**
 * Applies StudyBuddy's colour schemes to the [CustomTabsIntent.Builder].
 * @param colorPrimary The primary colour to use for the toolbar, secondary toolbar
 * and navigation bar.
 */
fun CustomTabsIntent.Builder.setAppColorScheme(
    @ColorInt colorPrimary: Int
) = apply {
    setColorScheme(
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_NO -> ColorScheme.Light
            AppCompatDelegate.MODE_NIGHT_YES -> ColorScheme.Dark
            else -> ColorScheme.System
        }
    )

    setDefaultColorSchemeParams(
        toolbarColor = colorPrimary,
        secondaryToolbarColor = colorPrimary,
        navigationBarColor = colorPrimary
    )
}

/**
 * Applies StudyBuddy's colour schemes to the [CustomTabsIntent.Builder].
 * @param context The context used to retrieve the primary colour.
 * @see setAppColorScheme
 */
fun CustomTabsIntent.Builder.setAppColorScheme(
    context: Context
) = setAppColorScheme(context.dynamicColorPrimary)

/**
 * Applies StudyBuddy's default settings to the [CustomTabsIntent.Builder].
 * @param context The context used to retrieve the primary colour (see [setAppColorScheme]).
 * @param useAppColorScheme Whether to respect StudyBuddy's colour scheme.
 */
fun CustomTabsIntent.Builder.applyAppDefaults(
    context: Context,
    useAppColorScheme: Boolean = true
) = apply {
    if (useAppColorScheme) {
        setAppColorScheme(context)
    }

    setShareState(ShareState.On)
    setShowTitle(true)
}
