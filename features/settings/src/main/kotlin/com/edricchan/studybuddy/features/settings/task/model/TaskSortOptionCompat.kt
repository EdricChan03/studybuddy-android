package com.edricchan.studybuddy.features.settings.task.model

import androidx.annotation.StringRes
import com.edricchan.studybuddy.core.settings.tasks.resources.R
import com.edricchan.studybuddy.features.tasks.constants.sharedprefs.TodoOptionsPrefConstants.TodoSortValues

enum class TaskSortOptionCompat(
    @field:StringRes val stringResource: Int,
    val value: String
) {
    None(
        stringResource = R.string.pref_task_default_sort_entry_none,
        value = TodoSortValues.NONE
    ),
    TitleAsc(
        stringResource = R.string.pref_task_default_sort_entry_title_asc,
        value = TodoSortValues.TITLE_ASC
    ),
    TitleDesc(
        stringResource = R.string.pref_task_default_sort_entry_title_desc,
        value = TodoSortValues.TITLE_DESC
    ),
    DueDateNewToOld(
        stringResource = R.string.pref_task_default_sort_entry_due_date_new_to_old,
        value = TodoSortValues.DUE_DATE_NEW_TO_OLD
    ),
    DueDateOldToNew(
        stringResource = R.string.pref_task_default_sort_entry_due_date_old_to_new,
        value = TodoSortValues.DUE_DATE_OLD_TO_NEW
    );

    companion object {
        fun fromValue(value: String): TaskSortOptionCompat =
            entries.find { it.value == value } ?: None
    }
}
