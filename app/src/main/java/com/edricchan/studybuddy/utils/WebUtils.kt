package com.edricchan.studybuddy.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.preference.PreferenceManager
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants

/**
 * Utility class for web-related functionality.
 * @param context The context to be used for Chrome Custom Tabs (CCT).
 */
class WebUtils(val context: Context) {

    /**
     * Launches a [uri] with the given [context]
     * @param uri The URI to launch/open
     */
    fun launchUri(uri: Uri) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        launchUri(
            uri, preferences.getBoolean(Constants.prefUseCustomTabs, true),
            true
        )
    }

    /**
     * Launches a [uri] with the given [context]
     * @param uri The URI to launch/open as a [String]
     */
    fun launchUri(uri: String) {
        launchUri(uri.toUri())
    }

    /**
     * Launches a [uri] with the given [context]
     * @param uri The URI to launch/open
     * @param useCustomTabs Whether to use Chrome Custom Tabs
     * @param useAppColorScheme Whether to respect the app's color/colour scheme
     */
    fun launchUri(
        uri: Uri, useCustomTabs: Boolean = true,
        useAppColorScheme: Boolean = true
    ) {
        var customTabsIntent: CustomTabsIntent? = null
        if (useCustomTabs) {
            customTabsIntent = CustomTabsIntent.Builder().apply {
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
                setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
                setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                setNavigationBarColor(ContextCompat.getColor(context, R.color.colorPrimary))
                setShareState(CustomTabsIntent.SHARE_STATE_ON)
                setShowTitle(true)
            }.build()
        }
        launchUri(uri, customTabsIntent)
    }

    /**
     * Launches a [uri] with the given [context]
     * @param uri The URI to launch/open as a [String]
     * @param useCustomTabs Whether to use Chrome Custom Tabs
     * @param useAppColorScheme Whether to respect the app's color/colour scheme
     */
    fun launchUri(
        uri: String, useCustomTabs: Boolean = true,
        useAppColorScheme: Boolean = true
    ) {
        launchUri(uri.toUri(), useCustomTabs, useAppColorScheme)
    }

    /**
     * Launches a [uri] with the given [context] and options for the [CustomTabsIntent.Builder]
     * with the [customTabsIntentBuilderOptions] parameter.
     * @param uri The URI to launch/open
     * @param customTabsIntentBuilderOptions Options for the [CustomTabsIntent.Builder]
     */
    fun launchUri(
        uri: Uri,
        customTabsIntentBuilderOptions: CustomTabsIntent.Builder.() -> Unit
    ) {
        val customTabsIntent = CustomTabsIntent.Builder().apply(customTabsIntentBuilderOptions)
            .build()
        launchUri(uri, customTabsIntent)
    }

    /**
     * Launches a [uri] with the given [context] and options for the [CustomTabsIntent.Builder]
     * with the [customTabsIntentBuilderOptions] parameter.
     * @param uri The URI to launch/open
     * @param customTabsIntentBuilderOptions Options for the [CustomTabsIntent.Builder]
     */
    fun launchUri(
        uri: String,
        customTabsIntentBuilderOptions: CustomTabsIntent.Builder.() -> Unit
    ) {
        launchUri(uri.toUri(), customTabsIntentBuilderOptions)
    }

    private fun launchUri(uri: Uri, customTabsIntent: CustomTabsIntent?) {
        if (customTabsIntent != null) {
            customTabsIntent.launchUrl(context, uri)
        } else {
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }

    companion object {
        /**
         * Creates an instance of the utility class.
         * @param context The context to be used for the class.
         * @see WebUtils
         */
        fun getInstance(context: Context) = WebUtils(context)
    }
}