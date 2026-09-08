package com.edricchan.studybuddy.core.settings.tasks.ui.sort.compat

import androidx.annotation.StringRes
import com.edricchan.studybuddy.core.settings.tasks.model.sort.compat.TaskSortOptionCompat
import com.edricchan.studybuddy.core.settings.tasks.resources.R

@get:StringRes
val TaskSortOptionCompat.labelResource
    get() = when (this) {
        TaskSortOptionCompat.None -> R.string.pref_task_default_sort_entry_none
        TaskSortOptionCompat.TitleAsc -> R.string.pref_task_default_sort_entry_title_asc
        TaskSortOptionCompat.TitleDesc -> R.string.pref_task_default_sort_entry_title_desc
        TaskSortOptionCompat.DueDateNewToOld -> R.string.pref_task_default_sort_entry_due_date_new_to_old
        TaskSortOptionCompat.DueDateOldToNew -> R.string.pref_task_default_sort_entry_due_date_old_to_new
    }
