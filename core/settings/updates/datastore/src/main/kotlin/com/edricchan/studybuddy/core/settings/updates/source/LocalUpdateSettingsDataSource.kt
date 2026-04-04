package com.edricchan.studybuddy.core.settings.updates.source

import androidx.datastore.core.DataStore
import com.edricchan.studybuddy.core.settings.updates.proto.UpdateSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Duration
import javax.inject.Inject

class LocalUpdateSettingsDataSource @Inject constructor(
    private val dataStore: DataStore<@JvmSuppressWildcards UpdateSettings>
) : UpdateSettingsDataSource {
    override val checkFrequency: Flow<Duration> =
        dataStore.data.map { it.check_freq ?: Duration.ofHours(6) }

    override suspend fun setCheckFrequency(duration: Duration) {
        dataStore.updateData {
            it.copy(check_freq = duration)
        }
    }

    override val canDownloadMetered: Flow<Boolean> = dataStore.data.map { it.can_download_metered }
    override suspend fun setCanDownloadMetered(value: Boolean) {
        dataStore.updateData {
            it.copy(can_download_metered = value)
        }
    }

    override val onlyDownloadCharging: Flow<Boolean> =
        dataStore.data.map { it.only_download_charging }

    override suspend fun setOnlyDownloadCharging(value: Boolean) {
        dataStore.updateData {
            it.copy(only_download_charging = value)
        }
    }
}
