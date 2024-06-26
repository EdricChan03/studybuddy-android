package com.edricchan.studybuddy.utils.dev

import android.content.Context

/**
 * Whether the app is in developer mode.
 * @param useSharedPrefsOnly Whether to only check from shared preferences
 * @return `true` if the app is in developer mode, `false` otherwise.
 */
fun Context.isDevMode(useSharedPrefsOnly: Boolean = false): Boolean = devModeOptions.getBoolean(
    DevModePrefConstants.DEV_MODE_ENABLED, false
) || (!useSharedPrefsOnly && BuildConfig.DEBUG)
