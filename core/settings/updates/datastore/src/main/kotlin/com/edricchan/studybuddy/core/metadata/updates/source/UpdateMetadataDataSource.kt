package com.edricchan.studybuddy.core.metadata.updates.source

import com.edricchan.studybuddy.core.metadata.updates.AppVersion
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface UpdateMetadataDataSource {
    val previousVersion: Flow<AppVersion?>
    suspend fun setPreviousVersion(version: AppVersion?)
    suspend fun setPreviousVersion(code: Long, name: String, source: AppVersion.Source) {
        setPreviousVersion(AppVersion(code = code, name = name, source = source))
    }

    val currentVersion: Flow<AppVersion?>
    suspend fun setCurrentVersion(version: AppVersion?)
    suspend fun setCurrentVersion(code: Long, name: String, source: AppVersion.Source) {
        setCurrentVersion(AppVersion(code = code, name = name, source = source))
    }

    val lastChecked: Flow<Instant?>
    suspend fun setLastChecked(lastChecked: Instant?)

    val lastUpdated: Flow<Instant?>
    suspend fun setLastUpdated(lastUpdated: Instant?)
}
