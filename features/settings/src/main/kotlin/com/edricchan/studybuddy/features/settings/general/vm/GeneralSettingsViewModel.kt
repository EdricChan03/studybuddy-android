package com.edricchan.studybuddy.features.settings.general.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import com.edricchan.studybuddy.core.settings.appearance.DarkThemeValue
import com.edricchan.studybuddy.core.settings.appearance.keyPrefDarkTheme
import com.edricchan.studybuddy.core.settings.appearance.keyPrefDynamicTheme
import com.edricchan.studybuddy.core.settings.appearance.keyPrefUseCustomTabs
import com.edricchan.studybuddy.core.settings.tracking.keyPrefEnableUserTracking
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.edricchan.studybuddy.ui.theming.isDynamicColorAvailable
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.fredporciuncula.flow.preferences.Preference
import com.fredporciuncula.flow.preferences.map
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class GeneralSettingsViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    private val appPreferences = FlowSharedPreferences(
        context.defaultSharedPreferences
    )

    val prefEnableUserTracking: Preference<Boolean> = appPreferences.getBoolean(
        keyPrefEnableUserTracking,
        defaultValue = false
    )

    val prefUseCustomTabs: Preference<Boolean> = appPreferences.getBoolean(
        keyPrefUseCustomTabs,
        defaultValue = true
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
