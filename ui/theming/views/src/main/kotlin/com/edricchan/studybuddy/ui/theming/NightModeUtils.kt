package com.edricchan.studybuddy.ui.theming

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.edricchan.studybuddy.core.settings.appearance.DarkThemePrefValues
import com.edricchan.studybuddy.core.settings.appearance.DarkThemeValue
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
enum class DarkThemeOption(val prefValue: DarkThemeValue, val mode: NightMode) {

    V1Always(
        DarkThemeValue.V1Always,
        NightMode.Yes
    ),

    @Suppress("DEPRECATION")
    @Deprecated(
        "Automatic switching of dark/light based on the current time is deprecated. " +
            "Considering using an explicit setting, or AutoBattery.",
        ReplaceWith(
            "V2AutoBatterySaver",
            "com.edricchan.studybuddy.ui.theming.DarkThemeOption.V2AutoBatterySaver"
        )
    )
    V1AutoTime(
        DarkThemeValue.V1AutoTime,
        NightMode.AutoTime
    ),
    V1Never(
        DarkThemeValue.V1Never,
        NightMode.No
    ),
    V2Always(
        DarkThemeValue.V2Always,
        NightMode.Yes
    ),
    V2AutoBatterySaver(
        DarkThemeValue.V2AutoBattery,
        NightMode.AutoBattery
    ),
    V2FollowSystem(
        DarkThemeValue.V2FollowSystem,
        NightMode.FollowSystem
    ),
    V2Never(
        DarkThemeValue.V2Never,
        NightMode.No
    ),
    Default(DarkThemeValue.V2FollowSystem, NightMode.Unspecified);

    companion object {
        /** Retrieves the [DarkThemeOption] given the [value], or [Default] if invalid. */
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

        /** Retrieves the [DarkThemeOption] given the [value], or [Default] if invalid. */
        fun fromValue(value: DarkThemeValue) =
            entries.find { it.prefValue == value } ?: Default

        /** Retrieves the [DarkThemeOption] given the [mode], or [Default] if invalid. */
        fun fromMode(mode: NightMode) =
            entries.find { it.mode == mode } ?: Default
    }
}

/** The current night mode. */
var defaultNightMode: NightMode
    get() = NightMode.fromMode(AppCompatDelegate.getDefaultNightMode()) ?: NightMode.Unspecified
    set(value) = AppCompatDelegate.setDefaultNightMode(value.modeId)

/** The currently selected dark theme. */
var Context.prefDarkTheme: DarkThemeOption
    get() = DarkThemeOption.fromValue(
        defaultSharedPreferences.getString(
            PREF_DARK_THEME,
            DarkThemePrefValues.V2_FOLLOW_SYSTEM
        )
    )
    set(theme) {
        defaultSharedPreferences.edit {
            putString(PREF_DARK_THEME, theme.prefValue.value)
        }
    }

/** Applies the dark theme given the [newTheme]. */
fun Context.applyDarkTheme(newTheme: DarkThemeOption = prefDarkTheme) {
    // Update the preference value if they don't match
    if (newTheme != prefDarkTheme) prefDarkTheme = newTheme
    defaultNightMode = newTheme.mode
}
