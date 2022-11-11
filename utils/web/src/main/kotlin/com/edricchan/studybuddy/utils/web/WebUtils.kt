package com.edricchan.studybuddy.utils.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.preference.PreferenceManager
import com.google.android.material.color.MaterialColors

/**
 * Utility class for web-related functionality.
 * @param context The context to be used for Chrome Custom Tabs (CCT).
 */
@Deprecated("Use the top-level extension functions instead")
class WebUtils(private val context: Context) {

    /**
     * Launches a [uri] with the given [context]
     * @param uri The URI to launch/open
     */
    @Deprecated(
        "Use the extension function instead",
        ReplaceWith(
            "context.launchUri(uri)",
            "com.edricchan.studybuddy.utils.launchUri"
        )
    )
    fun launchUri(uri: Uri) = context.launchUri(uri)

    /**
     * Launches a [uri] with the given [context]
     * @param uri The URI to launch/open as a [String]
     */
    @Deprecated(
        "Use the extension function instead",
        ReplaceWith(
            "context.launchUri(uri.toUri(), useCustomTabs, useAppColorScheme)",
            "com.edricchan.studybuddy.utils.launchUri",
            "androidx.core.next.toUri"
        )
    )
    fun launchUri(uri: String) = launchUri(uri.toUri())

    /**
     * Launches a [uri] with the given [context]
     * @param uri The URI to launch/open
     * @param useCustomTabs Whether to use Chrome Custom Tabs
     * @param useAppColorScheme Whether to respect the app's color/colour scheme
     */
    @Deprecated(
        "Use the extension function instead",
        ReplaceWith(
            "context.launchUri(uri, useCustomTabs, useAppColorScheme)",
            "com.edricchan.studybuddy.utils.launchUri"
        )
    )
    fun launchUri(
        uri: Uri, useCustomTabs: Boolean = true,
        useAppColorScheme: Boolean = true
    ) = context.launchUri(uri, useCustomTabs, useAppColorScheme)

    /**
     * Launches a [uri] with the given [context]
     * @param uri The URI to launch/open as a [String]
     * @param useCustomTabs Whether to use Chrome Custom Tabs
     * @param useAppColorScheme Whether to respect the app's color/colour scheme
     */
    @Deprecated(
        "Use the extension function instead",
        ReplaceWith(
            "context.launchUri(uri.toUri(), useCustomTabs, useAppColorScheme)",
            "com.edricchan.studybuddy.utils.launchUri",
            "androidx.core.net.toUri"
        )
    )
    fun launchUri(
        uri: String, useCustomTabs: Boolean = true,
        useAppColorScheme: Boolean = true
    ) = launchUri(uri.toUri(), useCustomTabs, useAppColorScheme)

    companion object {
        /**
         * Creates an instance of the utility class.
         * @param context The context to be used for the class.
         * @see WebUtils
         */
        @Suppress("DeprecatedCallableAddReplaceWith") // WebUtils itself is deprecated
        @Deprecated("Use the top-level extension functions instead")
        fun getInstance(context: Context) = WebUtils(context)
    }
}

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

private val Context.defaultSharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)

const val keyPrefUseCustomTabs = "pref_use_custom_tabs"

/**
 * Launches a [uri] with the given [context]
 * @param uri The URI to launch/open
 * @param useCustomTabs Whether to use Chrome Custom Tabs
 * @param useAppColorScheme Whether to respect the app's color/colour scheme
 */
fun Context.launchUri(
    uri: Uri,
    useCustomTabs: Boolean = defaultSharedPreferences.getBoolean(keyPrefUseCustomTabs, true),
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
