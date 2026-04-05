package com.edricchan.studybuddy.core.metadata.updates.source.store

import android.content.Context
import androidx.datastore.core.DataMigration
import com.edricchan.studybuddy.core.settings.updates.proto.UpdateMetadata
import com.edricchan.studybuddy.exts.android.metadata.versionCode
import com.edricchan.studybuddy.exts.android.metadata.versionName

class SeedMetadataMigration(private val context: Context) : DataMigration<UpdateMetadata> {
    override suspend fun cleanUp() {
        // No-op
    }

    override suspend fun migrate(currentData: UpdateMetadata): UpdateMetadata {
        return currentData.copy(
            previous_version = UpdateMetadata.AppVersion(
                version_name = context.versionName.orEmpty(),
                version_code = context.versionCode
            ),
            current_version = UpdateMetadata.AppVersion(
                version_name = context.versionName.orEmpty(),
                version_code = context.versionCode
            )
        )
    }

    override suspend fun shouldMigrate(currentData: UpdateMetadata): Boolean {
        return currentData.previous_version == null || currentData.current_version == null
    }
}
