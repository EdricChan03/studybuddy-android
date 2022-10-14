package com.edricchan.studybuddy.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.edricchan.studybuddy.R
import com.google.android.material.color.DynamicColors

/** Whether Android 12's dynamic theming system is available. */
val isDynamicColorAvailable get() = DynamicColors.isDynamicColorAvailable()

/** Whether the app should use Android 12's dynamic theming system. */
var Context.prefDynamicTheme
    get() = defaultSharedPreferences.getBoolean(
        ThemeUtils.PREF_DYNAMIC_THEME, isDynamicColorAvailable
    )
    set(value) {
        defaultSharedPreferences.edit { putBoolean(ThemeUtils.PREF_DYNAMIC_THEME, value) }
    }

/** Applies the dynamic theme based on whether [shouldApply] is `true`. */
fun Context.applyDynamicTheme(shouldApply: Boolean = prefDynamicTheme) {
    theme.applyStyle(
        if (shouldApply) R.style.ThemeOverlay_Material3_DynamicColors_DayNight
        else R.style.ThemeOverlay_Material3, true
    )
}

/** Theming-related utilities. */
class ThemeUtils(private val context: Context) {
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    /**
     * Whether the app should use Android 12's dynamic theming system.
     */
    @Deprecated(
        "Use the top-level prefDynamicTheme extension instead",
        ReplaceWith("context.prefDynamicTheme", "com.edricchan.studybuddy.utils.prefDynamicTheme")
    )
    var isUsingDynamicColor by context::prefDynamicTheme

    /** Applies the theme based on whether Android 12's dynamic theming system should be used. */
    @Deprecated(
        "Use the top-level applyDynamicTheme extension instead",
        ReplaceWith("context.applyDynamicTheme", "com.edricchan.studybuddy.utils.applyDynamicTheme")
    )
    fun applyTheme() = context.applyDynamicTheme()

    companion object {
        /** Preference key used for the dynamic theme preference. */
        const val PREF_DYNAMIC_THEME = "pref_dynamic_theme"
    }
}

/** Retrieves an instance of [ThemeUtils] given the context. */
val Context.themeUtils get() = ThemeUtils(this)
