package com.edricchan.studybuddy.features.tasks.migrations

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.boswelja.migration.conditionalMigration
import com.edricchan.studybuddy.exts.androidx.preference.getSharedPreferencesPath
import com.edricchan.studybuddy.exts.androidx.preference.sharedPreferencesFileExists
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.features.tasks.constants.sharedprefs.TodoOptionsPrefConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.io.path.moveTo

private const val oldSharedPrefsFile = "TodoFragPrefs"

fun Context.shouldMigrateTaskSharedPrefFile() = sharedPreferencesFileExists(oldSharedPrefsFile)

suspend fun Context.migrateTaskSharedPrefFile(): Boolean = withContext(Dispatchers.IO) {
    Log.d(TAG, "Migrating shared preference file...")
    // Rename existing file to new file
    val oldFile = getSharedPreferencesPath(oldSharedPrefsFile)
    val newFile = getSharedPreferencesPath(TodoOptionsPrefConstants.FILE_TODO_OPTIONS)
    return@withContext try {
        oldFile.moveTo(newFile, overwrite = true)
        Log.d(TAG, "Successfully migrated shared preference file!")
        true
    } catch (e: IOException) {
        Log.e(
            TAG,
            "An error occurred while attempting to migrate the shared preference file:",
            e
        )
        false
    }
}

private val Context.taskOptionsPrefs
    get() = getSharedPreferences(
        TodoOptionsPrefConstants.FILE_TODO_OPTIONS,
        Context.MODE_PRIVATE
    )

@Suppress("Deprecation")
fun Context.shouldMigrateTaskOptsPrefs() =
    TodoOptionsPrefConstants.PREF_DEFAULT_SORT_OLD in taskOptionsPrefs

@Suppress("Deprecation")
suspend fun Context.migrateTaskOptsPrefs(): Boolean = withContext(Dispatchers.IO) {
    return@withContext try {
        Log.d(TAG, "Migrating task options shared preference keys...")
        // Old SharedPreference key still exists
        val oldValue = taskOptionsPrefs.getString(
            TodoOptionsPrefConstants.PREF_DEFAULT_SORT_OLD,
            TodoOptionsPrefConstants.TodoSortValues.NONE
        )
        taskOptionsPrefs.edit(commit = true) {
            putString(TodoOptionsPrefConstants.PREF_DEFAULT_SORT, oldValue)
            remove(TodoOptionsPrefConstants.PREF_DEFAULT_SORT_OLD)
        }
        true
    } catch (e: Exception) {
        false
    }
}

val Context.TaskSharedPrefFileMigration
    get() = conditionalMigration(
        onShouldMigrate = { shouldMigrateTaskSharedPrefFile() },
        onMigrate = ::migrateTaskSharedPrefFile
    )

val Context.TaskSharedPrefValuesMigration
    get() = conditionalMigration(
        onShouldMigrate = { shouldMigrateTaskOptsPrefs() },
        onMigrate = ::migrateTaskOptsPrefs
    )
