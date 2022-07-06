package com.edricchan.studybuddy.utils

import android.content.Context
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.constants.sharedprefs.DevModePrefConstants

/**
 * Whether the app is in developer mode.
 * @param useSharedPrefsOnly Whether to only check from shared preferences
 * @return `true` if the app is in developer mode, `false` otherwise.
 */
fun Context.isDevMode(useSharedPrefsOnly: Boolean = false): Boolean {
    val devModeOpts = getSharedPreferences(
        DevModePrefConstants.FILE_DEV_MODE,
        Context.MODE_PRIVATE
    )
    return if (useSharedPrefsOnly) devModeOpts.getBoolean(
        DevModePrefConstants.DEV_MODE_ENABLED,
        false
    )
    else devModeOpts.getBoolean(
        DevModePrefConstants.DEV_MODE_ENABLED,
        false
    ) || BuildConfig.DEBUG
}
