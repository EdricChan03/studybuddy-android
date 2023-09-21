package com.edricchan.studybuddy.features.tasks.migrations

import android.content.Context
import android.util.Log
import com.boswelja.migration.Migrator
import com.edricchan.studybuddy.exts.common.TAG

// TODO: Add versioning logic
class TasksMigrator(
    context: Context,
    private val onMigrated: (version: Int) -> Unit = {}
) : Migrator(
    currentVersion = 1,
    migrations = listOf(
        context.TaskSharedPrefFileMigration,
        context.TaskSharedPrefValuesMigration
    )
) {
    override suspend fun getOldVersion() = 1

    override suspend fun onMigratedTo(version: Int) {
        onMigrated(version)
        Log.d(TAG, "Successfully migrated tasks from ${getOldVersion()} to $version")
    }
}
