package com.edricchan.studybuddy.core.settings.appearance

/**
 * Possible values for the [com.edricchan.studybuddy.core.settings.prefDarkTheme]
 * preference.
 */
object DarkThemePrefValues {
    // We should probably migrate away from these old constants at some point
    @Deprecated(
        "This property is kept for backwards-compatibility, " +
            "use V2_ALWAYS where applicable",
        ReplaceWith(
            "V2_ALWAYS",
            "com.edricchan.studybuddy.core.settings.appearance.DarkThemePrefValues.V2_ALWAYS"
        )
    )
    const val V1_ALWAYS = "1"

    @Deprecated(
        "Automatic switching of dark/light based on the current time is deprecated. " +
            "Considering using an explicit setting, or AutoBattery.",
        ReplaceWith(
            "V2_AUTO_BATTERY",
            "com.edricchan.studybuddy.core.settings.appearance.DarkThemePrefValues.V2_AUTO_BATTERY"
        )
    )
    const val V1_AUTO_TIME = "2"

    @Deprecated(
        "This property is kept for backwards-compatibility, " +
            "use V2_NEVER where applicable",
        ReplaceWith(
            "V2_NEVER",
            "com.edricchan.studybuddy.core.settings.appearance.DarkThemePrefValues.V2_NEVER"
        )
    )
    const val V1_NEVER = "3"
    const val V2_ALWAYS = "always"
    const val V2_AUTO_BATTERY = "automatic_battery_Saver"
    const val V2_FOLLOW_SYSTEM = "follow_system"
    const val V2_NEVER = "never"
}
