package com.edricchan.studybuddy.core.settings.appearance

import androidx.annotation.StringRes
import com.edricchan.studybuddy.core.settings.appearance.proto.DarkModeSettingProto
import com.edricchan.studybuddy.core.settings.appearance.resources.R

enum class DarkModeSetting(
    val protoValue: DarkModeSettingProto
) {
    /** Follow the system's dark setting. */
    FollowSystem(DarkModeSettingProto.FollowSystem),

    /** Always enable dark theme. */
    AlwaysOn(DarkModeSettingProto.AlwaysOn),

    /** Always disable dark theme. */
    AlwaysOff(DarkModeSettingProto.AlwaysOff);

    companion object {
        /** Alias for [AlwaysOn]. */
        val Always = AlwaysOn

        /** Alias for [AlwaysOff]. */
        val Never = AlwaysOff

        fun fromProto(proto: DarkModeSettingProto): DarkModeSetting? =
            entries.find { it.protoValue == proto }
    }
}

@get:StringRes
val DarkModeSetting.labelResource: Int
    get() = when (this) {
        DarkModeSetting.FollowSystem -> R.string.pref_dark_theme_entry_system
        DarkModeSetting.AlwaysOn -> R.string.pref_dark_theme_entry_always
        DarkModeSetting.AlwaysOff -> R.string.pref_dark_theme_entry_never
    }
