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

@Suppress("DEPRECATION")
enum class DarkThemeValue(val value: String) {
    @Deprecated(
        "This property is kept for backwards-compatibility, " +
            "use V2Always where applicable",
        ReplaceWith(
            "V2Always",
            "com.edricchan.studybuddy.core.settings.appearance.DarkThemeValue.V2Always"
        )
    )
    V1Always(DarkThemePrefValues.V1_ALWAYS),

    @Deprecated(
        "Automatic switching of dark/light based on the current time is deprecated. " +
            "Considering using an explicit setting, or AutoBattery.",
        ReplaceWith(
            "V2AutoBattery",
            "com.edricchan.studybuddy.core.settings.appearance.DarkThemeValue.V2AutoBattery"
        )
    )
    V1AutoTime(DarkThemePrefValues.V1_AUTO_TIME),

    @Deprecated(
        "This property is kept for backwards-compatibility, " +
            "use V2Never where applicable",
        ReplaceWith(
            "V2Never",
            "com.edricchan.studybuddy.core.settings.appearance.DarkThemeValue.V2Never"
        )
    )
    V1Never(DarkThemePrefValues.V1_NEVER),
    V2Always(DarkThemePrefValues.V2_ALWAYS),
    V2AutoBattery(DarkThemePrefValues.V2_AUTO_BATTERY),
    V2FollowSystem(DarkThemePrefValues.V2_FOLLOW_SYSTEM),
    V2Never(DarkThemePrefValues.V2_NEVER);

    companion object {
        fun fromPrefValue(value: String?) = when (value) {
            DarkThemePrefValues.V1_ALWAYS -> V1Always
            DarkThemePrefValues.V1_AUTO_TIME -> V1AutoTime
            DarkThemePrefValues.V1_NEVER -> V1AutoTime
            DarkThemePrefValues.V2_ALWAYS -> V2Always
            DarkThemePrefValues.V2_AUTO_BATTERY -> V2AutoBattery
            DarkThemePrefValues.V2_FOLLOW_SYSTEM -> V2FollowSystem
            DarkThemePrefValues.V2_NEVER -> V2Never
            else -> V2FollowSystem
        }
    }
}
