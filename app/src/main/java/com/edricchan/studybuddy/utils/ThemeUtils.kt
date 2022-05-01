package com.edricchan.studybuddy.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.edricchan.studybuddy.R
import com.google.android.material.color.DynamicColors

/** Whether Android 12's dynamic theming system is available. */
val isDynamicColorAvailable get() = DynamicColors.isDynamicColorAvailable()

/** Theming-related utilities. */
class ThemeUtils(private val context: Context) {
    private var prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    /**
     * Whether the app should use Android 12's dynamic theming system.
     */
    var isUsingDynamicColor
        get() = prefs.getBoolean(PREF_DYNAMIC_THEME, isDynamicColorAvailable)
        set(value) {
            prefs.edit { putBoolean(PREF_DYNAMIC_THEME, value) }
        }

    /** Applies the theme based on whether Android 12's dynamic theming system should be used. */
    fun applyTheme() {
        context.theme.applyStyle(
            if (isUsingDynamicColor) R.style.ThemeOverlay_Material3_DynamicColors_DayNight
            else R.style.ThemeOverlay_Material3,
            true
        )
    }

    companion object {
        /** Preference key used for the dynamic theme preference. */
        const val PREF_DYNAMIC_THEME = "pref_dynamic_theme"
    }
}

/** Retrieves an instance of [ThemeUtils] given the context. */
val Context.themeUtils get() = ThemeUtils(this)
