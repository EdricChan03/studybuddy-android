package com.edricchan.studybuddy.ui.theming

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
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
            NightMode.values().find { it.modeId == modeId }
    }
}

/** An enum-like class used to represent a theme setting value and its corresponding night mode value. */
sealed class DarkThemeValue(val value: String?, val mode: NightMode) {
    object V1Always : DarkThemeValue(VALUE_V1_ALWAYS, NightMode.Yes)

    @Suppress("DEPRECATION")
    @Deprecated(
        "Automatic switching of dark/light based on the current time is deprecated. " +
            "Considering using an explicit setting, or AutoBattery.",
        ReplaceWith(
            "V2AutoBatterySaver",
            "com.edricchan.studybuddy.ui.theming.DarkThemeValue.V2AutoBatterySaver"
        )
    )
    object V1AutoTime : DarkThemeValue(VALUE_V1_AUTO_TIME, NightMode.AutoTime)
    object V1Never : DarkThemeValue(VALUE_V1_NEVER, NightMode.No)

    object V2Always : DarkThemeValue(VALUE_V2_ALWAYS, NightMode.Yes)
    object V2AutoBatterySaver : DarkThemeValue(
        VALUE_V2_AUTO_BATTERY, NightMode.AutoBattery
    )

    object V2FollowSystem : DarkThemeValue(
        VALUE_V2_FOLLOW_SYSTEM, NightMode.FollowSystem
    )

    object V2Never : DarkThemeValue(VALUE_V2_NEVER, NightMode.No)
    object Default : DarkThemeValue(null, NightMode.Unspecified)

    companion object {
        const val VALUE_V1_ALWAYS = "1"
        const val VALUE_V1_AUTO_TIME = "2"
        const val VALUE_V1_NEVER = "3"
        const val VALUE_V2_ALWAYS = "always"
        const val VALUE_V2_AUTO_BATTERY = "automatic_battery_Saver"
        const val VALUE_V2_FOLLOW_SYSTEM = "follow_system"
        const val VALUE_V2_NEVER = "never"

        /** Retrieves the [DarkThemeValue] given the [value], or [Default] if invalid. */
        @Suppress("DEPRECATION")
        fun fromValue(value: String?) = when (value) {
            VALUE_V1_ALWAYS -> V1Always
            VALUE_V1_AUTO_TIME -> V1AutoTime
            VALUE_V1_NEVER -> V1Never
            VALUE_V2_ALWAYS -> V2Always
            VALUE_V2_AUTO_BATTERY -> V2AutoBatterySaver
            VALUE_V2_FOLLOW_SYSTEM -> V2FollowSystem
            VALUE_V2_NEVER -> V2Never
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
            DarkThemeValue.VALUE_V2_AUTO_BATTERY
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
