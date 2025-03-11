package com.edricchan.studybuddy.features.settings.general.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import com.edricchan.studybuddy.core.settings.appearance.DarkThemeValue
import com.edricchan.studybuddy.core.settings.appearance.keyPrefUseCustomTabs
import com.edricchan.studybuddy.core.settings.tracking.keyPrefEnableUserTracking
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.edricchan.studybuddy.ui.theming.common.ThemePreferences
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.fredporciuncula.flow.preferences.Preference
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

    private val themePrefs = ThemePreferences(context)

    val prefEnableUserTracking: Preference<Boolean> = appPreferences.getBoolean(
        keyPrefEnableUserTracking,
        defaultValue = false
    )

    val prefUseCustomTabs: Preference<Boolean> = appPreferences.getBoolean(
        keyPrefUseCustomTabs,
        defaultValue = true
    )

    val prefDarkTheme: Preference<DarkThemeValue.Version2> = themePrefs.prefDarkTheme

    val prefEnableDynamicTheme: Preference<Boolean> = themePrefs.prefEnableDynamicTheme
}
