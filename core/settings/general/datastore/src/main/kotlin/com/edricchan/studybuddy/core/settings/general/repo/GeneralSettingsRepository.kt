package com.edricchan.studybuddy.core.settings.general.repo

import com.edricchan.studybuddy.core.di.qualifiers.coroutines.ApplicationScope
import com.edricchan.studybuddy.core.settings.general.locale.LocaleOptions
import com.edricchan.studybuddy.core.settings.general.repo.source.GeneralSettingsDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Repository for settings related to the general category.
 *
 * This includes:
 * * [GeneralSettingsDataSource.localeOptions]
 * * [GeneralSettingsDataSource.openLinksInApp]
 */
class GeneralSettingsRepository @Inject constructor(
    private val dataStore: GeneralSettingsDataSource,
    @ApplicationScope private val coroutineScope: CoroutineScope
) {
    val localeOptions: StateFlow<LocaleOptions> = dataStore.localeOptions
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = LocaleOptions()
        )

    suspend fun setLocaleOptions(options: LocaleOptions) {
        dataStore.setLocaleOptions(options)
    }

    suspend fun setLocaleOptions(transform: suspend (LocaleOptions) -> LocaleOptions) {
        dataStore.setLocaleOptions(transform)
    }

    val openLinksInApp: StateFlow<Boolean> = dataStore.openLinksInApp
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = true
        )

    suspend fun setOpenLinksInApp(value: Boolean) {
        dataStore.setOpenLinksInApp(value)
    }

    suspend fun setOpenLinksInApp(transform: suspend (Boolean) -> Boolean) {
        dataStore.setOpenLinksInApp(transform)
    }
}
