package com.edricchan.studybuddy.utils.dev

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

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

/**
 * Whether developer mode is enabled.
 * @see DevModePrefConstants.DEV_MODE_ENABLED
 */
var Context.prefDevModeEnabled: Boolean
    get() = devModeOptions.getBoolean(
        DevModePrefConstants.DEV_MODE_ENABLED, false
    )
    set(value) {
        devModeOptions.edit {
            putBoolean(DevModePrefConstants.DEV_MODE_ENABLED, value)
        }
    }
