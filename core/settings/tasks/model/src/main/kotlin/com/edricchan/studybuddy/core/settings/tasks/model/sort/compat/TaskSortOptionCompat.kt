package com.edricchan.studybuddy.core.settings.tasks.model.sort.compat

import com.edricchan.studybuddy.features.tasks.constants.sharedprefs.TodoOptionsPrefConstants

@Deprecated("Use the orderByFields setting in TaskSettings.filterOptions instead")
enum class TaskSortOptionCompat(
    val value: String
) {
    None(
        value = TodoOptionsPrefConstants.TodoSortValues.NONE
    ),
    TitleAsc(
        value = TodoOptionsPrefConstants.TodoSortValues.TITLE_ASC
    ),
    TitleDesc(
        value = TodoOptionsPrefConstants.TodoSortValues.TITLE_DESC
    ),
    DueDateNewToOld(
        value = TodoOptionsPrefConstants.TodoSortValues.DUE_DATE_NEW_TO_OLD
    ),
    DueDateOldToNew(
        value = TodoOptionsPrefConstants.TodoSortValues.DUE_DATE_OLD_TO_NEW
    );

    companion object {
        fun fromValue(value: String): TaskSortOptionCompat =
            entries.find { it.value == value } ?: None
    }
}
