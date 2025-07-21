package com.edricchan.studybuddy.features.settings.task.model

import androidx.annotation.StringRes
import com.edricchan.studybuddy.core.settings.tasks.resources.R

enum class TaskSortOptionCompat(
    @field:StringRes val stringResource: Int,
    val value: String
) {
    None(
        stringResource = R.string.pref_task_default_sort_entry_none,
        value = "none"
    ),
    TitleAsc(
        stringResource = R.string.pref_task_default_sort_entry_title_asc,
        value = "title_asc"
    ),
    TitleDesc(
        stringResource = R.string.pref_task_default_sort_entry_title_desc,
        value = "title_desc"
    ),
    DueDateNewToOld(
        stringResource = R.string.pref_task_default_sort_entry_due_date_new_to_old,
        value = "due_date_new_to_old"
    ),
    DueDateOldToNew(
        stringResource = R.string.pref_task_default_sort_entry_due_date_old_to_new,
        value = "due_date_old_to_new"
    );

    companion object {
        fun fromValue(value: String): TaskSortOptionCompat =
            entries.find { it.value == value } ?: None
    }
}
