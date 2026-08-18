package com.edricchan.studybuddy.core.settings.general.repo.source

import com.edricchan.studybuddy.core.settings.general.locale.LocaleOptions
import com.edricchan.studybuddy.core.settings.general.temporal.format.TemporalFormatOptions
import kotlinx.coroutines.flow.Flow

interface GeneralSettingsDataSource {
    val localeOptions: Flow<LocaleOptions>
    suspend fun setLocaleOptions(
        transform: suspend (LocaleOptions) -> LocaleOptions
    )

    suspend fun setLocaleOptions(options: LocaleOptions) {
        setLocaleOptions { options }
    }

    val temporalFormatOptions: Flow<TemporalFormatOptions>
    suspend fun setTemporalFormatOptions(
        transform: suspend (TemporalFormatOptions) -> TemporalFormatOptions
    )

    suspend fun setTemporalFormatOptions(options: TemporalFormatOptions) {
        setTemporalFormatOptions { options }
    }

    val openLinksInApp: Flow<Boolean>
    suspend fun setOpenLinksInApp(transform: suspend (Boolean) -> Boolean)
    suspend fun setOpenLinksInApp(value: Boolean) {
        setOpenLinksInApp { value }
    }
}
