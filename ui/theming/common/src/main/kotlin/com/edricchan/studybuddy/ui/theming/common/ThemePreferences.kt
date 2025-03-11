package com.edricchan.studybuddy.ui.theming.common

import android.content.Context
import com.edricchan.studybuddy.core.settings.appearance.DarkThemeValue
import com.edricchan.studybuddy.core.settings.appearance.keyPrefDarkTheme
import com.edricchan.studybuddy.core.settings.appearance.keyPrefDynamicTheme
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.edricchan.studybuddy.ui.theming.common.dynamic.isDynamicColorAvailable
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.fredporciuncula.flow.preferences.Preference
import com.fredporciuncula.flow.preferences.map

class ThemePreferences(context: Context) {
    private val appPreferences = FlowSharedPreferences(
        sharedPreferences = context.defaultSharedPreferences
    )

    val prefDarkTheme: Preference<DarkThemeValue.Version2> = appPreferences.getString(
        keyPrefDarkTheme,
        defaultValue = DarkThemeValue.V2FollowSystem.value
    ).map(
        mapper = DarkThemeValue.Version2::fromPrefValue,
        reverse = DarkThemeValue::value
    )

    val prefEnableDynamicTheme: Preference<Boolean> = appPreferences.getBoolean(
        keyPrefDynamicTheme,
        defaultValue = isDynamicColorAvailable
    )
}
