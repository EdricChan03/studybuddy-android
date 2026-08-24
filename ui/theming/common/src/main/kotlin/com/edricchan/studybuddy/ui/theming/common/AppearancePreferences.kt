package com.edricchan.studybuddy.ui.theming.common

import com.edricchan.studybuddy.core.settings.appearance.AppThemeSetting
import com.edricchan.studybuddy.core.settings.appearance.DarkModeSetting
import com.edricchan.studybuddy.core.settings.appearance.font.TypefaceSetting
import com.edricchan.studybuddy.core.settings.appearance.repo.AppearanceSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppearancePreferences @Inject constructor(
    private val settingsRepo: AppearanceSettingsRepository
) {
    val darkMode: StateFlow<DarkModeSetting> = settingsRepo.darkMode
    val darkModeFlow: Flow<DarkModeSetting> = settingsRepo.darkModeFlow
    suspend fun setDarkMode(value: DarkModeSetting) {
        settingsRepo.setDarkMode(value)
    }

    val appTheme: StateFlow<AppThemeSetting> = settingsRepo.appTheme
    val appThemeFlow: Flow<AppThemeSetting> = settingsRepo.appThemeFlow
    suspend fun setAppTheme(value: AppThemeSetting) {
        settingsRepo.setAppTheme(value)
    }

    fun isMonetTheme(): Boolean = settingsRepo.isMonetTheme()

    val isMonetTheme: Flow<Boolean> = appThemeFlow.map { it == AppThemeSetting.Monet }

    val displayTypeface: StateFlow<TypefaceSetting> = settingsRepo.displayTypeface
    suspend fun setDisplayTypeface(style: TypefaceSetting) {
        settingsRepo.setDisplayTypeface(style)
    }

    val bodyTypeface: StateFlow<TypefaceSetting> = settingsRepo.bodyTypeface
    suspend fun setBodyTypeface(style: TypefaceSetting) {
        settingsRepo.setBodyTypeface(style)
    }

    val baseSpacing: StateFlow<Int> = settingsRepo.baseSpacing
    suspend fun setBaseSpacing(spacing: Int) {
        settingsRepo.setBaseSpacing(spacing)
    }
}
