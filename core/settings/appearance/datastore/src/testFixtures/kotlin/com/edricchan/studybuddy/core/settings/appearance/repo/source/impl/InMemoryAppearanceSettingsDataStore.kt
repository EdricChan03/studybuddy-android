package com.edricchan.studybuddy.core.settings.appearance.repo.source.impl

import com.edricchan.studybuddy.core.settings.appearance.AppThemeSetting
import com.edricchan.studybuddy.core.settings.appearance.DarkModeSetting
import com.edricchan.studybuddy.core.settings.appearance.font.TypefaceSetting
import com.edricchan.studybuddy.core.settings.appearance.repo.source.AppearanceSettingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/** [AppearanceSettingsDataStore] implementation backed by in-memory [MutableStateFlow]s. */
class InMemoryAppearanceSettingsDataStore(
    initialDarkMode: DarkModeSetting = DarkModeSetting.FollowSystem,
    initialAppTheme: AppThemeSetting = AppThemeSetting.StudyBuddy,
    initialDisplayTypeface: TypefaceSetting = TypefaceSetting.FunnelDisplay,
    initialBodyTypeface: TypefaceSetting = TypefaceSetting.Baloo2,
    initialBaseSpacing: Int = 8
) : AppearanceSettingsDataStore {
    override val darkMode: Flow<DarkModeSetting>
        field = MutableStateFlow(initialDarkMode)

    override suspend fun setDarkMode(value: DarkModeSetting) {
        darkMode.value = value
    }

    override val appTheme: Flow<AppThemeSetting>
        field = MutableStateFlow(initialAppTheme)

    override suspend fun setAppTheme(value: AppThemeSetting) {
        appTheme.value = value
    }

    override val displayTypeface: Flow<TypefaceSetting>
        field = MutableStateFlow(initialDisplayTypeface)

    override suspend fun setDisplayTypeface(style: TypefaceSetting) {
        displayTypeface.value = style
    }

    override val bodyTypeface: Flow<TypefaceSetting>
        field = MutableStateFlow(initialBodyTypeface)

    override suspend fun setBodyTypeface(style: TypefaceSetting) {
        bodyTypeface.value = style
    }

    override val baseSpacing: Flow<Int> field = MutableStateFlow(initialBaseSpacing)

    override suspend fun setBaseSpacing(value: Int) {
        baseSpacing.value = value
    }
}
