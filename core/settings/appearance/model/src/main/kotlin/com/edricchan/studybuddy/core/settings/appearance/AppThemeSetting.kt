package com.edricchan.studybuddy.core.settings.appearance

import androidx.annotation.StringRes
import com.edricchan.studybuddy.core.settings.appearance.proto.AppThemeSettingProto
import com.edricchan.studybuddy.core.settings.appearance.resources.R

enum class AppThemeSetting(
    val protoValue: AppThemeSettingProto
) {
    /** The dynamic theme, available in Android 12+. */
    Monet(AppThemeSettingProto.Monet),

    /** The default app theme. */
    StudyBuddy(AppThemeSettingProto.StudyBuddy);

    companion object {
        fun fromProto(proto: AppThemeSettingProto): AppThemeSetting? =
            entries.find { it.protoValue == proto }
    }
}

@get:StringRes
val AppThemeSetting.labelResource: Int
    get() = when (this) {
        AppThemeSetting.Monet -> R.string.theme_monet_name
        AppThemeSetting.StudyBuddy -> R.string.theme_studybuddy_name
    }
