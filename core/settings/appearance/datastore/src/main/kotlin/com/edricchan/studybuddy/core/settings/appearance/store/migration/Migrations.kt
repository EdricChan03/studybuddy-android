package com.edricchan.studybuddy.core.settings.appearance.store.migration

import android.content.Context
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.datastore.migrations.SharedPreferencesMigration
import com.edricchan.studybuddy.core.settings.appearance.DarkThemeValue
import com.edricchan.studybuddy.core.settings.appearance.keyPrefDarkTheme
import com.edricchan.studybuddy.core.settings.appearance.keyPrefDynamicTheme
import com.edricchan.studybuddy.core.settings.appearance.proto.AppThemeSettingProto
import com.edricchan.studybuddy.core.settings.appearance.proto.AppearanceSettings
import com.edricchan.studybuddy.core.settings.appearance.toSetting
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences

@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
private val supportsDynamicColor get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

val AppearancePrefsKeys = setOf(
    keyPrefDarkTheme,
    keyPrefDynamicTheme
)

@Suppress("DEPRECATION")
val Context.AppearancePrefsMigration: SharedPreferencesMigration<AppearanceSettings>
    get() = SharedPreferencesMigration(
        produceSharedPreferences = { defaultSharedPreferences },
        keysToMigrate = AppearancePrefsKeys
    ) { view, settings ->
        settings.copy(
            dark_mode = DarkThemeValue.fromPrefValue(
                view.getString(
                    keyPrefDarkTheme,
                    DarkThemeValue.V2FollowSystem.value
                )
            ).toSetting()?.protoValue ?: settings.dark_mode,
            theme = if (
                view.getBoolean(
                    keyPrefDynamicTheme,
                    supportsDynamicColor
                )
            ) AppThemeSettingProto.Monet
            else settings.theme
        )
    }
