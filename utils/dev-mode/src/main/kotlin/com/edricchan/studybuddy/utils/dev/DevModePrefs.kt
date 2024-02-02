package com.edricchan.studybuddy.utils.dev

import android.content.Context
import android.content.SharedPreferences

/**
 * Retrieves the [SharedPreferences] instance for developer mode-related
 * options.
 * @receiver The [Context] to retrieve the [SharedPreferences] from.
 * @see Context.getSharedPreferences
 */
val Context.devModeOptions: SharedPreferences
    get() = getSharedPreferences(
        DevModePrefConstants.FILE_DEV_MODE,
        Context.MODE_PRIVATE
    )
