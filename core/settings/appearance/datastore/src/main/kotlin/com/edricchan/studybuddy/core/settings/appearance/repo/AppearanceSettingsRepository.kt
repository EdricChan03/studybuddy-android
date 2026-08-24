package com.edricchan.studybuddy.core.settings.appearance.repo

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import com.edricchan.studybuddy.core.di.qualifiers.coroutines.ApplicationScope
import com.edricchan.studybuddy.core.settings.appearance.AppThemeSetting
import com.edricchan.studybuddy.core.settings.appearance.DarkModeSetting
import com.edricchan.studybuddy.core.settings.appearance.font.TypefaceSetting
import com.edricchan.studybuddy.core.settings.appearance.repo.source.AppearanceSettingsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/** Whether dynamic colour theming is supported. */
@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
private val supportsDynamicColor get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

/**
 * Repository for settings related to the general category.
 *
 * This includes:
 * * [AppearanceSettingsDataStore.darkMode]
 * * [AppearanceSettingsDataStore.appTheme]
 * * [AppearanceSettingsDataStore.displayTypeface]
 * * [AppearanceSettingsDataStore.bodyTypeface]
 * * [AppearanceSettingsDataStore.baseSpacing]
 */
class AppearanceSettingsRepository @Inject constructor(
    private val dataStore: AppearanceSettingsDataStore,
    @ApplicationScope private val coroutineScope: CoroutineScope
) {
    val darkMode: StateFlow<DarkModeSetting> = dataStore.darkMode.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = DarkModeSetting.FollowSystem
    )

    val darkModeFlow: Flow<DarkModeSetting> = dataStore.darkMode

    suspend fun setDarkMode(value: DarkModeSetting) {
        dataStore.setDarkMode(value)
    }

    val appTheme: StateFlow<AppThemeSetting> = dataStore.appTheme.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = if (supportsDynamicColor) AppThemeSetting.Monet else AppThemeSetting.StudyBuddy
    )

    val appThemeFlow: Flow<AppThemeSetting> = dataStore.appTheme

    suspend fun setAppTheme(value: AppThemeSetting) {
        dataStore.setAppTheme(value)
    }

    fun isMonetTheme(): Boolean = appTheme.value == AppThemeSetting.Monet

    // Display settings

    val displayTypeface: StateFlow<TypefaceSetting> = dataStore.displayTypeface.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TypefaceSetting.FunnelDisplay
    )

    suspend fun setDisplayTypeface(style: TypefaceSetting) {
        dataStore.setDisplayTypeface(style)
    }

    val bodyTypeface: StateFlow<TypefaceSetting> = dataStore.bodyTypeface.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TypefaceSetting.Baloo2
    )

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

    val baseSpacing: StateFlow<Int> = dataStore.baseSpacing.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = 8
    )

    suspend fun setBaseSpacing(spacing: Int) {
        dataStore.setBaseSpacing(spacing)
    }
}
