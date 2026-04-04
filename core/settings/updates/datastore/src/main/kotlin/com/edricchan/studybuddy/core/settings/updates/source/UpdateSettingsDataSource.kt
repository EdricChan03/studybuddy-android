package com.edricchan.studybuddy.core.settings.updates.source

import kotlinx.coroutines.flow.Flow
import java.time.Duration

interface UpdateSettingsDataSource {
    val checkFrequency: Flow<Duration>
    suspend fun setCheckFrequency(duration: Duration)

    val canDownloadMetered: Flow<Boolean>
    suspend fun setCanDownloadMetered(value: Boolean)
    val onlyDownloadCharging: Flow<Boolean>
    suspend fun setOnlyDownloadCharging(value: Boolean)
}
