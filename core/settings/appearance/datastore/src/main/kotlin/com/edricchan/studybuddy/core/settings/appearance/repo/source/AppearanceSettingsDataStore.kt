package com.edricchan.studybuddy.core.settings.appearance.repo.source

import com.edricchan.studybuddy.core.settings.appearance.AppThemeSetting
import com.edricchan.studybuddy.core.settings.appearance.DarkModeSetting
import com.edricchan.studybuddy.core.settings.appearance.font.TypefaceConfig
import com.edricchan.studybuddy.core.settings.appearance.font.TypefaceSetting
import com.edricchan.studybuddy.core.settings.appearance.keyPrefDarkTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

interface AppearanceSettingsDataStore {
    /**
     * The current [DarkModeSetting] as a [Flow].
     * @see keyPrefDarkTheme
     */
    val darkMode: Flow<DarkModeSetting>

    /**
     * Sets the [darkMode] to use.
     * @param value The new [DarkModeSetting] to set.
     */
    suspend fun setDarkMode(value: DarkModeSetting)

    /** The application theme to use. */
    val appTheme: Flow<AppThemeSetting>

    /**
     * Sets the application theme to use.
     * @param value The new [AppTheme] to set.
     */
    suspend fun setAppTheme(value: AppThemeSetting)

    // Display settings

    /** The display (i.e. headers) font style to use across the app. */
    val displayTypeface: Flow<TypefaceSetting>

    /**
     * Sets the [displayTypeface] value to be used.
     * @param style The new [TypefaceSetting] value to use.
     */
    suspend fun setDisplayTypeface(style: TypefaceSetting)

    /** The body font style to use across the app. */
    val bodyTypeface: Flow<TypefaceSetting>

    /**
     * Sets the [bodyTypeface] value to be used.
     * @param style The new [TypefaceSetting] value to use.
     */
    suspend fun setBodyTypeface(style: TypefaceSetting)

    /**
     * The [display][displayTypeface] and [body][bodyTypeface] font styles to be used
     * as a [data class][TypefaceConfig].
     */
    val typefaceConfig: Flow<TypefaceConfig>
        get() = combine(displayTypeface, bodyTypeface, ::TypefaceConfig)

    /**
     * Sets the font styles ([displayTypeface] and [bodyTypeface]) to be used.
     * @param displayStyle The display [TypefaceSetting]. See [displayTypeface]
     * @param bodyStyle The body [TypefaceSetting]. See [bodyTypeface]
     */
    suspend fun setTypefaceConfig(displayStyle: TypefaceSetting, bodyStyle: TypefaceSetting) {
        setDisplayTypeface(displayStyle)
        setBodyTypeface(bodyStyle)
    }
}
