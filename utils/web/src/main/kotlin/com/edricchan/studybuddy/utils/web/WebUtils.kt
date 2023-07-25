package com.edricchan.studybuddy.utils.web

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import io.github.edricchan03.androidx.browser.ktx.customTabsIntent

internal fun Context.launchUri(uri: Uri, customTabsIntent: CustomTabsIntent?) {
    customTabsIntent?.launchUrl(this, uri) ?: startActivity(Intent(Intent.ACTION_VIEW, uri))
}

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
    val customTabsIntent: CustomTabsIntent? = useCustomTabs.takeIf { it }?.let {
        customTabsIntent {
            applyAppDefaults(this@launchUri, useAppColorScheme)
        }
    }
    launchUri(uri, customTabsIntent)
}
