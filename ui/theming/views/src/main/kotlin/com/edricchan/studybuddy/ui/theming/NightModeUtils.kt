package com.edricchan.studybuddy.ui.theming

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.edricchan.studybuddy.core.settings.appearance.DarkThemePrefValues
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import androidx.appcompat.app.AppCompatDelegate.NightMode as NightModeAnnotation

/** Preference key used for the dark theme preference. */
const val PREF_DARK_THEME = "pref_dark_theme"

enum class NightMode(@NightModeAnnotation val modeId: Int) {
    Yes(AppCompatDelegate.MODE_NIGHT_YES),
    No(AppCompatDelegate.MODE_NIGHT_NO),

    @Suppress("DEPRECATION")
    @Deprecated(
        "Automatic switching of dark/light based on the current time is deprecated. " +
            "Considering using an explicit setting, or AutoBattery.",
        ReplaceWith("AutoBattery", "com.edricchan.studybuddy.ui.theming.NightMode.AutoTime")
    )
    AutoTime(AppCompatDelegate.MODE_NIGHT_AUTO_TIME),
    AutoBattery(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY),
    FollowSystem(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM),
    Unspecified(AppCompatDelegate.MODE_NIGHT_UNSPECIFIED);

    companion object {
        /**
         * Retrieves the [NightMode] enum representation of [modeId],
         * or `null` if no such representation exists.
         */
        fun fromMode(@NightModeAnnotation modeId: Int) =
            NightMode.entries.find { it.modeId == modeId }
    }
}

/** An enum-like class used to represent a theme setting value and its corresponding night mode value. */
enum class DarkThemeValue(val value: String?, val mode: NightMode) {

    V1Always(
        DarkThemePrefValues.V1_ALWAYS,
        NightMode.Yes
    ),

    @Suppress("DEPRECATION")
    @Deprecated(
        "Automatic switching of dark/light based on the current time is deprecated. " +
            "Considering using an explicit setting, or AutoBattery.",
        ReplaceWith(
            "V2AutoBatterySaver",
            "com.edricchan.studybuddy.ui.theming.DarkThemeValue.V2AutoBatterySaver"
        )
    )
    V1AutoTime(
        DarkThemePrefValues.V1_AUTO_TIME,
        NightMode.AutoTime
    ),
    V1Never(
        DarkThemePrefValues.V1_NEVER,
        NightMode.No
    ),
    V2Always(
        DarkThemePrefValues.V2_ALWAYS,
        NightMode.Yes
    ),
    V2AutoBatterySaver(
        DarkThemePrefValues.V2_AUTO_BATTERY,
        NightMode.AutoBattery
    ),
    V2FollowSystem(
        DarkThemePrefValues.V2_FOLLOW_SYSTEM,
        NightMode.FollowSystem
    ),
    V2Never(
        DarkThemePrefValues.V2_NEVER,
        NightMode.No
    ),
    Default(null, NightMode.Unspecified);

    companion object {
        /** Retrieves the [DarkThemeValue] given the [value], or [Default] if invalid. */
        @Suppress("DEPRECATION")
        fun fromValue(value: String?) = when (value) {
            DarkThemePrefValues.V1_ALWAYS -> V1Always
            DarkThemePrefValues.V1_AUTO_TIME -> V1AutoTime
            DarkThemePrefValues.V1_NEVER -> V1Never
            DarkThemePrefValues.V2_ALWAYS -> V2Always
            DarkThemePrefValues.V2_AUTO_BATTERY -> V2AutoBatterySaver
            DarkThemePrefValues.V2_FOLLOW_SYSTEM -> V2FollowSystem
            DarkThemePrefValues.V2_NEVER -> V2Never
            else -> Default
        }

        /** Retrieves the [DarkThemeValue] given the [mode], or [Default] if invalid. */
        @Suppress("DEPRECATION")
        fun fromMode(mode: NightMode) = when (mode) {
            NightMode.AutoTime -> V1AutoTime
            NightMode.Yes -> V2Always
            NightMode.No -> V2Never
            NightMode.FollowSystem -> V2FollowSystem
            NightMode.AutoBattery -> V2AutoBatterySaver
            else -> Default
        }
    }
}

/** The current night mode. */
var defaultNightMode: NightMode
    get() = NightMode.fromMode(AppCompatDelegate.getDefaultNightMode()) ?: NightMode.Unspecified
    set(value) = AppCompatDelegate.setDefaultNightMode(value.modeId)

/** The currently selected dark theme. */
var Context.prefDarkTheme: DarkThemeValue
    get() = DarkThemeValue.fromValue(
        defaultSharedPreferences.getString(
            PREF_DARK_THEME,
            DarkThemePrefValues.V2_AUTO_BATTERY
        )
    )
    set(theme) {
        defaultSharedPreferences.edit {
            putString(PREF_DARK_THEME, theme.value)
        }
    }

/** Applies the dark theme given the [newTheme]. */
fun Context.applyDarkTheme(newTheme: DarkThemeValue = prefDarkTheme) {
    // Update the preference value if they don't match
    if (newTheme != prefDarkTheme) prefDarkTheme = newTheme
    defaultNightMode = newTheme.mode
}
