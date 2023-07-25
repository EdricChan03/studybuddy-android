package com.edricchan.studybuddy.utils.web

import android.content.Context
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.google.android.material.color.MaterialColors

@get:ColorInt
private val Context.dynamicColorPrimary
    get() = MaterialColors.getColor(
        this,
        R.attr.colorPrimary,
        ContextCompat.getColor(this, R.color.colorPrimary)
    )

internal fun buildCCTIntent(init: CustomTabsIntent.Builder.() -> Unit) =
    CustomTabsIntent.Builder().apply(init).build()

internal fun CustomTabsIntent.Builder.setDefaultColorSchemeParams(
    init: CustomTabColorSchemeParams.Builder.() -> Unit
) {
    setDefaultColorSchemeParams(CustomTabColorSchemeParams.Builder().apply(init).build())
}

internal fun CustomTabsIntent.Builder.setDefaultColorSchemeParams(
    @ColorInt toolbarColor: Int? = null,
    @ColorInt secondaryToolbarColor: Int? = null,
    @ColorInt navigationBarColor: Int? = null,
    @ColorInt navigationBarDividerColor: Int? = null
) = setDefaultColorSchemeParams {
    toolbarColor?.let { setToolbarColor(it) }
    secondaryToolbarColor?.let { setSecondaryToolbarColor(it) }
    navigationBarColor?.let { setNavigationBarColor(it) }
    navigationBarDividerColor?.let { setNavigationBarDividerColor(it) }
}

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
            AppCompatDelegate.MODE_NIGHT_NO -> CustomTabsIntent.COLOR_SCHEME_LIGHT
            AppCompatDelegate.MODE_NIGHT_YES -> CustomTabsIntent.COLOR_SCHEME_DARK
            else -> CustomTabsIntent.COLOR_SCHEME_SYSTEM
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

    setShareState(CustomTabsIntent.SHARE_STATE_ON)
    setShowTitle(true)
}
