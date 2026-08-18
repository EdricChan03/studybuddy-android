package com.edricchan.studybuddy.features.settings.appearance.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edricchan.studybuddy.core.settings.appearance.AppThemeSetting
import com.edricchan.studybuddy.core.settings.appearance.DarkModeSetting
import com.edricchan.studybuddy.core.settings.appearance.font.TypefaceSetting
import com.edricchan.studybuddy.core.settings.appearance.repo.AppearanceSettingsRepository
import com.edricchan.studybuddy.ui.theming.compose.theme.font.toFontFamily
import com.edricchan.studybuddy.ui.theming.compose.theme.font.toFontFamilyConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppearanceSettingsViewModel @Inject constructor(
    private val repo: AppearanceSettingsRepository
) : ViewModel() {
    val darkMode by repo::darkMode
    fun onDarkThemeChange(setting: DarkModeSetting) {
        viewModelScope.launch {
            repo.setDarkMode(setting)
        }
    }

    val appTheme by repo::appTheme
    fun onAppThemeChange(value: AppThemeSetting) {
        viewModelScope.launch {
            repo.setAppTheme(value)
        }
    }

    // Display settings

    //#region Font settings
    val displayFontStyle by repo::displayTypeface
    fun onDisplayFontStyleChange(style: TypefaceSetting) {
        viewModelScope.launch {
            repo.setDisplayTypeface(style)
        }
    }

    /** Gets the [displayFontStyle] setting as a Compose [androidx.compose.ui.text.font.FontFamily]. */
    val displayFontFamily = displayFontStyle.map { it.toFontFamily() }

    val bodyFontStyle by repo::bodyTypeface
    fun onBodyFontStyleChange(style: TypefaceSetting) {
        viewModelScope.launch {
            repo.setBodyTypeface(style)
        }
    }

    /** Gets the [bodyFontStyle] setting as a Compose [androidx.compose.ui.text.font.FontFamily]. */
    val bodyFontFamily = bodyFontStyle.map { it.toFontFamily() }

    val fontStyles by repo::typefaces
    val fontFamilies = fontStyles.map { it.toFontFamilyConfig() }
    //#endregion
}
