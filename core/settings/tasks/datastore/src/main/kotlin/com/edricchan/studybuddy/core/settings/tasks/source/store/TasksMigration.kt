package com.edricchan.studybuddy.core.settings.tasks.source.store

import android.content.Context
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.migrations.SharedPreferencesView
import com.edricchan.studybuddy.core.settings.tasks.proto.TasksSettings
import com.edricchan.studybuddy.data.common.proto.SortDirection
import com.edricchan.studybuddy.features.tasks.constants.sharedprefs.TodoOptionsPrefConstants
import com.edricchan.studybuddy.features.tasks.constants.sharedprefs.TodoOptionsPrefConstants.TodoSortValues
import com.edricchan.studybuddy.features.tasks.proto.TaskField
import com.edricchan.studybuddy.features.tasks.proto.TaskSortEntry

private const val oldTaskPrefsFile = "TodoFragPrefs"

private val oldSortToNewMap = mapOf(
    TodoSortValues.NONE to null,
    TodoSortValues.UNSET to null,
    TodoSortValues.TITLE_ASC to TaskSortEntry(
        field_ = TaskField.Title,
        direction = SortDirection.Ascending
    ),
    TodoSortValues.TITLE_DESC to TaskSortEntry(
        field_ = TaskField.Title,
        direction = SortDirection.Descending
    ),
    TodoSortValues.DUE_DATE_NEW_TO_OLD to TaskSortEntry(
        field_ = TaskField.DueDate,
        direction = SortDirection.Descending
    ),
    TodoSortValues.DUE_DATE_OLD_TO_NEW to TaskSortEntry(
        field_ = TaskField.DueDate,
        direction = SortDirection.Ascending
    ),
)

@Suppress("DEPRECATION")
private fun migrate(
    view: SharedPreferencesView, settings: TasksSettings
): TasksSettings {
    if (TodoOptionsPrefConstants.PREF_DEFAULT_SORT !in view || TodoOptionsPrefConstants.PREF_DEFAULT_SORT_OLD !in view)
        return settings

    val defaultSort = view.getString(
        TodoOptionsPrefConstants.PREF_DEFAULT_SORT,
        view.getString(TodoOptionsPrefConstants.PREF_DEFAULT_SORT_OLD, null)
    )
    return settings.copy(
        filter_options = settings.filter_options?.copy(
            order_by_fields = oldSortToNewMap[defaultSort]?.let {
                settings.filter_options?.order_by_fields?.plusElement(it)
            } ?: settings.filter_options?.order_by_fields ?: emptyList()
        )
    )
}

val Context.tasksPrefsMigration
    get() = SharedPreferencesMigration(
        this,
        TodoOptionsPrefConstants.FILE_TODO_OPTIONS,
        migrate = ::migrate
    )

val Context.tasksPrefsOldMigration
    get() = SharedPreferencesMigration(
        this,
        oldTaskPrefsFile,
        migrate = ::migrate
    )
