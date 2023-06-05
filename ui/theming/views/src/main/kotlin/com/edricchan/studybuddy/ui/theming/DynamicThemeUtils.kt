package com.edricchan.studybuddy.ui.theming

import android.content.Context
import androidx.core.content.edit
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.google.android.material.color.DynamicColors

/** Preference key used for the dynamic theme preference. */
const val PREF_DYNAMIC_THEME = "pref_dynamic_theme"

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
        if (shouldApply) R.style.ThemeOverlay_Material3_DynamicColors_DayNight
        else R.style.ThemeOverlay_Material3, true
    )
}
