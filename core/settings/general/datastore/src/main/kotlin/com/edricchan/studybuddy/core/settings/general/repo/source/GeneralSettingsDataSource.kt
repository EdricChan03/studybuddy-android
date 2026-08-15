package com.edricchan.studybuddy.core.settings.general.repo.source

import com.edricchan.studybuddy.core.settings.general.locale.LocaleOptions
import kotlinx.coroutines.flow.Flow

interface GeneralSettingsDataSource {
    val localeOptions: Flow<LocaleOptions>
    suspend fun setLocaleOptions(
        transform: suspend (LocaleOptions) -> LocaleOptions
    )

    suspend fun setLocaleOptions(options: LocaleOptions) {
        setLocaleOptions { options }
    }

    val openLinksInApp: Flow<Boolean>
    suspend fun setOpenLinksInApp(transform: suspend (Boolean) -> Boolean)
    suspend fun setOpenLinksInApp(value: Boolean) {
        setOpenLinksInApp { value }
    }
}
