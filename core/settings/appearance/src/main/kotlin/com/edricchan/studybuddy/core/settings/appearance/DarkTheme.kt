package com.edricchan.studybuddy.core.settings.appearance

/**
 * Possible values for the [com.edricchan.studybuddy.core.settings.appearance.keyPrefDarkTheme]
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

    @Deprecated(
        "AutoBattery is now deprecated in favour of FollowSystem.",
        ReplaceWith(
            "V2_FOLLOW_SYSTEM",
            "com.edricchan.studybuddy.core.settings.appearance.DarkThemePrefValues.V2_FOLLOW_SYSTEM"
        )
    )
    const val V1_AUTO_BATTERY = "automatic_battery_saver"
    const val V2_ALWAYS = "always"
    const val V2_FOLLOW_SYSTEM = "follow_system"
    const val V2_NEVER = "never"
}

@Suppress("DEPRECATION")
sealed interface DarkThemeValue {
    val value: String

    sealed class Version1(override val value: String) : DarkThemeValue

    sealed class Version2(override val value: String) : DarkThemeValue {
        companion object {
            // by lazy is needed for https://youtrack.jetbrains.com/issue/KT-59723
            val entries by lazy {
                listOf(
                    V2FollowSystem,
                    V2Always,
                    V2Never
                )
            }

            fun fromPrefValue(value: String?): Version2 = when (value) {
                DarkThemePrefValues.V1_ALWAYS, DarkThemePrefValues.V2_ALWAYS -> V2Always
                DarkThemePrefValues.V2_FOLLOW_SYSTEM -> V2FollowSystem
                DarkThemePrefValues.V1_NEVER, DarkThemePrefValues.V2_NEVER -> V2Never
                else -> V2FollowSystem
            }
        }
    }

    @Deprecated(
        "This property is kept for backwards-compatibility, " +
            "use V2Always where applicable",
        ReplaceWith(
            "V2Always",
            "com.edricchan.studybuddy.core.settings.appearance.DarkThemeValue.V2Always"
        )
    )
    data object V1Always : Version1(DarkThemePrefValues.V1_ALWAYS)

    @Deprecated(
        "Automatic switching of dark/light based on the current time is deprecated. " +
            "Considering using an explicit setting, or AutoBattery.",
        ReplaceWith(
            "V2AutoBattery",
            "com.edricchan.studybuddy.core.settings.appearance.DarkThemeValue.V2AutoBattery"
        )
    )
    data object V1AutoTime : Version1(DarkThemePrefValues.V1_AUTO_TIME)

    @Deprecated(
        "This property is kept for backwards-compatibility, " +
            "use V2Never where applicable",
        ReplaceWith(
            "V2Never",
            "com.edricchan.studybuddy.core.settings.appearance.DarkThemeValue.V2Never"
        )
    )
    data object V1Never : Version1(DarkThemePrefValues.V1_NEVER)

    @Deprecated(
        "AutoBattery is now deprecated in favour of FollowSystem.",
        ReplaceWith(
            "V2FollowSystem",
            "com.edricchan.studybuddy.core.settings.appearance.DarkThemeValue.V2FollowSystem"
        )
    )
    data object V1AutoBattery : Version1(DarkThemePrefValues.V1_AUTO_BATTERY)
    data object V2Always : Version2(DarkThemePrefValues.V2_ALWAYS)
    data object V2FollowSystem : Version2(DarkThemePrefValues.V2_FOLLOW_SYSTEM)
    data object V2Never : Version2(DarkThemePrefValues.V2_NEVER)

    companion object {
        fun fromPrefValue(value: String?) = when (value) {
            DarkThemePrefValues.V1_ALWAYS -> V1Always
            DarkThemePrefValues.V1_AUTO_TIME -> V1AutoTime
            DarkThemePrefValues.V1_NEVER -> V1AutoTime
            DarkThemePrefValues.V1_AUTO_BATTERY -> V1AutoBattery
            DarkThemePrefValues.V2_ALWAYS -> V2Always
            DarkThemePrefValues.V2_FOLLOW_SYSTEM -> V2FollowSystem
            DarkThemePrefValues.V2_NEVER -> V2Never
            else -> V2FollowSystem
        }
    }
}
