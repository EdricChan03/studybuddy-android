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
    @Deprecated(
        "This getter is not backed by the new data-store implementation " +
            "- use the appTheme StateFlow property (checking if the value is " +
            "AppThemeSetting.Monet) from AppearanceSettingsRepository instead"
    )
    get() = defaultSharedPreferences.getBoolean(
        keyPrefDynamicTheme, isDynamicColorAvailable
    )
    @Deprecated(
        "This setter is not backed by the new data-store implementation " +
            "- use the setAppTheme method (with the value AppThemeSetting.Monet) " +
            "from AppearanceSettingsRepository instead"
    )
    set(value) {
        defaultSharedPreferences.edit { putBoolean(keyPrefDynamicTheme, value) }
    }
