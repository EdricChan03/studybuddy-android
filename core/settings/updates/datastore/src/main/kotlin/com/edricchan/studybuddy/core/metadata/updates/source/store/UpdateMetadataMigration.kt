package com.edricchan.studybuddy.core.metadata.updates.source.store

import android.content.Context
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.migrations.SharedPreferencesView
import com.edricchan.studybuddy.core.settings.updates.UpdateInfoPrefConstants
import com.edricchan.studybuddy.core.settings.updates.proto.UpdateMetadata
import java.time.Instant

private const val UnsetValue = -1L

private fun migrateUpdateMetadata(
    view: SharedPreferencesView, metadata: UpdateMetadata
): UpdateMetadata {
    val oldLastUpdatedMs = view.getLong(
        UpdateInfoPrefConstants.PREF_LAST_UPDATED_DATE,
        view.getLong(UpdateInfoPrefConstants.PREF_LAST_UPDATED_DATE, UnsetValue)
    ).takeIf { it > UnsetValue }

    val oldLastCheckedMs = view.getLong(
        UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE,
        view.getLong(UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE, UnsetValue)
    ).takeIf { it > UnsetValue }

    return metadata.copy(
        last_updated = oldLastUpdatedMs?.let(Instant::ofEpochMilli),
        last_checked = oldLastCheckedMs?.let(Instant::ofEpochMilli)
    )
}

val Context.updateMetadataMigration
    get() = SharedPreferencesMigration(
        context = this,
        sharedPreferencesName = UpdateInfoPrefConstants.FILE_UPDATE_INFO,
        migrate = ::migrateUpdateMetadata
    )
