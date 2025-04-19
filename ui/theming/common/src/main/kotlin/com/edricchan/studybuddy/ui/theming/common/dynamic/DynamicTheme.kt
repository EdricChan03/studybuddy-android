package com.edricchan.studybuddy.ui.theming.common.dynamic

import android.content.Context
import androidx.core.content.edit
import com.edricchan.studybuddy.core.settings.appearance.keyPrefDynamicTheme
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.google.android.material.color.DynamicColors

/** Whether Android 12's dynamic theming system is available. */
val isDynamicColorAvailable get() = DynamicColors.isDynamicColorAvailable()

/** Whether the app should use Android 12's dynamic theming system. */
var Context.prefDynamicTheme
    get() = defaultSharedPreferences.getBoolean(
        keyPrefDynamicTheme, isDynamicColorAvailable
    )
    set(value) {
        defaultSharedPreferences.edit { putBoolean(keyPrefDynamicTheme, value) }
    }
