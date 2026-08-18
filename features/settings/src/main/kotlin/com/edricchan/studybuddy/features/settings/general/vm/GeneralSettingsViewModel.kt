package com.edricchan.studybuddy.features.settings.general.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edricchan.studybuddy.core.settings.appearance.DarkThemeValue
import com.edricchan.studybuddy.core.settings.general.repo.GeneralSettingsRepository
import com.edricchan.studybuddy.core.settings.tracking.keyPrefEnableUserTracking
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.edricchan.studybuddy.ui.theming.common.ThemePreferences
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.fredporciuncula.flow.preferences.Preference
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeneralSettingsViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: GeneralSettingsRepository
) : ViewModel() {
    private val appPreferences = FlowSharedPreferences(
        context.defaultSharedPreferences
    )

    private val themePrefs = ThemePreferences(context)

    val prefEnableUserTracking: Preference<Boolean> = appPreferences.getBoolean(
        keyPrefEnableUserTracking,
        defaultValue = false
    )

    val openLinksInApp by repository::openLinksInApp

    fun setOpenLinksInApp(value: Boolean) {
        viewModelScope.launch {
            repository.setOpenLinksInApp(value)
        }
    }

    val prefDarkTheme: Preference<DarkThemeValue.Version2> = themePrefs.prefDarkTheme

    val prefEnableDynamicTheme: Preference<Boolean> = themePrefs.prefEnableDynamicTheme
}
