package com.edricchan.studybuddy.core.settings.appearance.repo

import com.edricchan.studybuddy.core.settings.appearance.AppThemeSetting
import com.edricchan.studybuddy.core.settings.appearance.DarkModeSetting
import com.edricchan.studybuddy.core.settings.appearance.font.TypefaceSetting
import com.edricchan.studybuddy.core.settings.appearance.repo.source.AppearanceSettingsDataStore
import javax.inject.Inject

/**
 * Repository for settings related to the general category.
 *
 * This includes:
 * * [AppearanceSettingsDataStore.darkMode]
 * * [AppearanceSettingsDataStore.appTheme]
 */
class AppearanceSettingsRepository @Inject constructor(
    private val dataStore: AppearanceSettingsDataStore
) {
    val darkMode by dataStore::darkMode
    suspend fun setDarkMode(value: DarkModeSetting) {
        dataStore.setDarkMode(value)
    }

    val appTheme by dataStore::appTheme
    suspend fun setAppTheme(value: AppThemeSetting) {
        dataStore.setAppTheme(value)
    }

    // Display settings

    val displayTypeface by dataStore::displayTypeface
    suspend fun setDisplayTypeface(style: TypefaceSetting) {
        dataStore.setDisplayTypeface(style)
    }

    val bodyTypeface by dataStore::bodyTypeface
    suspend fun setBodyTypeface(style: TypefaceSetting) {
        dataStore.setBodyTypeface(style)
    }

    val typefaces by dataStore::typefaceConfig
    suspend fun setTypefaceConfig(
        displayStyle: TypefaceSetting,
        bodyStyle: TypefaceSetting
    ) {
        dataStore.setTypefaceConfig(displayStyle, bodyStyle)
    }
}
