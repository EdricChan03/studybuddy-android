package com.edricchan.studybuddy.core.metadata.updates.source

import androidx.datastore.core.DataStore
import com.edricchan.studybuddy.core.metadata.updates.AppVersion
import com.edricchan.studybuddy.core.settings.updates.proto.UpdateMetadata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

class LocalUpdateMetadataDataSource @Inject constructor(
    private val dataStore: DataStore<@JvmSuppressWildcards UpdateMetadata>
) : UpdateMetadataDataSource {
    override val previousVersion: Flow<AppVersion?> =
        dataStore.data.map { it.previous_version?.toDomain() }

    override suspend fun setPreviousVersion(version: AppVersion?) {
        dataStore.updateData {
            it.copy(previous_version = version?.toProto())
        }
    }

    override val currentVersion: Flow<AppVersion?> =
        dataStore.data.map { it.current_version?.toDomain() }

    override suspend fun setCurrentVersion(version: AppVersion?) {
        dataStore.updateData {
            it.copy(current_version = version?.toProto())
        }
    }

    override val lastChecked: Flow<Instant?> =
        dataStore.data.map { it.last_checked }

    override suspend fun setLastChecked(lastChecked: Instant?) {
        dataStore.updateData {
            it.copy(last_checked = lastChecked)
        }
    }

    override val lastUpdated: Flow<Instant?> =
        dataStore.data.map { it.last_updated }

    override suspend fun setLastUpdated(lastUpdated: Instant?) {
        dataStore.updateData {
            it.copy(last_updated = lastUpdated)
        }
    }
}
