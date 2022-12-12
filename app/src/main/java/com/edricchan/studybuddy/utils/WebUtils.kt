package com.edricchan.studybuddy.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.google.android.material.color.MaterialColors

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

internal fun Context.launchUri(uri: Uri, customTabsIntent: CustomTabsIntent?) {
    customTabsIntent?.launchUrl(this, uri) ?: startActivity(Intent(Intent.ACTION_VIEW, uri))
}

/**
 * Launches a [uri] with the given [context]
 * @param uri The URI to launch/open
 * @param useCustomTabs Whether to use Chrome Custom Tabs
 * @param useAppColorScheme Whether to respect the app's color/colour scheme
 */
fun Context.launchUri(
    uri: Uri,
    useCustomTabs: Boolean = defaultSharedPreferences.getBoolean(Constants.prefUseCustomTabs, true),
    useAppColorScheme: Boolean = true
) {
    var customTabsIntent: CustomTabsIntent? = null
    if (useCustomTabs) {
        customTabsIntent = buildCCTIntent {
            if (useAppColorScheme) {
                @CustomTabsIntent.ColorScheme var colorScheme =
                    CustomTabsIntent.COLOR_SCHEME_SYSTEM
                @SuppressLint("SwitchIntDef")
                when (AppCompatDelegate.getDefaultNightMode()) {
                    AppCompatDelegate.MODE_NIGHT_NO -> colorScheme =
                        CustomTabsIntent.COLOR_SCHEME_LIGHT

                    AppCompatDelegate.MODE_NIGHT_YES -> colorScheme =
                        CustomTabsIntent.COLOR_SCHEME_DARK
                }
                setColorScheme(colorScheme)
            }

            val color = MaterialColors.getColor(
                this@launchUri,
                R.attr.colorPrimary,
                ContextCompat.getColor(this@launchUri, R.color.colorPrimary)
            )
            setDefaultColorSchemeParams(
                toolbarColor = color,
                secondaryToolbarColor = color,
                navigationBarColor = color
            )

            setShareState(CustomTabsIntent.SHARE_STATE_ON)
            setShowTitle(true)
        }
    }
    launchUri(uri, customTabsIntent)
}
