package com.edricchan.studybuddy.core.settings.general.repo.source

import androidx.datastore.core.DataStore
import com.edricchan.studybuddy.core.settings.general.locale.LocaleOptions
import com.edricchan.studybuddy.core.settings.general.locale.toDomain
import com.edricchan.studybuddy.core.settings.general.locale.toProto
import com.edricchan.studybuddy.core.settings.general.proto.GeneralSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Implementation of [GeneralSettingsDataSource] backed by a ProtoBuf file. */
class LocalGeneralSettingsDataSource @Inject constructor(
    private val dataStore: DataStore<@JvmSuppressWildcards GeneralSettings>
) : GeneralSettingsDataSource {
    override val localeOptions: Flow<LocaleOptions> =
        dataStore.data.map { it.locale_settings?.toDomain() ?: LocaleOptions() }

    override suspend fun setLocaleOptions(transform: suspend (LocaleOptions) -> LocaleOptions) {
        dataStore.updateData { settings ->
            val localeOptions = settings.locale_settings?.toDomain() ?: LocaleOptions()
            settings.copy(
                locale_settings = transform(localeOptions).toProto()
            )
        }
    }

    override val openLinksInApp: Flow<Boolean> = dataStore.data.map { it.open_links_in_app }

    override suspend fun setOpenLinksInApp(transform: suspend (Boolean) -> Boolean) {
        dataStore.updateData { settings ->
            settings.copy(open_links_in_app = transform(settings.open_links_in_app))
        }
    }
}
