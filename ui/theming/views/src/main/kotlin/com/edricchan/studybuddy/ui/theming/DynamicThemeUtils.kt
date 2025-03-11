package com.edricchan.studybuddy.ui.theming

import android.content.Context
import androidx.core.content.edit
import com.edricchan.studybuddy.core.settings.appearance.keyPrefDynamicTheme
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.google.android.material.color.DynamicColors
import com.google.android.material.R as MaterialR

/** Preference key used for the dynamic theme preference. */
@Deprecated(
    "Use keyPrefDynamicTheme from core-settings",
    ReplaceWith(
        "keyPrefDynamicTheme",
        "com.edricchan.studybuddy.core.settings.appearance.keyPrefDynamicTheme"
    )
)
const val PREF_DYNAMIC_THEME = keyPrefDynamicTheme

/** Whether Android 12's dynamic theming system is available. */
val isDynamicColorAvailable get() = DynamicColors.isDynamicColorAvailable()

/** Whether the app should use Android 12's dynamic theming system. */
var Context.prefDynamicTheme
    get() = defaultSharedPreferences.getBoolean(
        PREF_DYNAMIC_THEME, isDynamicColorAvailable
    )
    set(value) {
        defaultSharedPreferences.edit { putBoolean(PREF_DYNAMIC_THEME, value) }
    }

/** Applies the dynamic theme based on whether [shouldApply] is `true`. */
fun Context.applyDynamicTheme(shouldApply: Boolean = prefDynamicTheme) {
    theme.applyStyle(
        if (shouldApply) MaterialR.style.ThemeOverlay_Material3_DynamicColors_DayNight
        else MaterialR.style.ThemeOverlay_Material3, true
    )
}
