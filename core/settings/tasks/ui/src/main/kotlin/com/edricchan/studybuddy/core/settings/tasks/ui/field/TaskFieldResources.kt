package com.edricchan.studybuddy.core.settings.tasks.ui.field

import androidx.annotation.StringRes
import com.edricchan.studybuddy.core.settings.tasks.model.field.TaskField
import com.edricchan.studybuddy.features.tasks.resources.R

@get:StringRes
val TaskField.labelResource: Int
    get() = when (this) {
        TaskField.Title -> R.string.task_field_title_label
        TaskField.Description -> R.string.task_field_description_label
        TaskField.DueDate -> R.string.task_field_due_date_label
        TaskField.Labels -> R.string.task_field_tags_label
//        TaskField.Project -> R.string.task_field_project_label
        TaskField.IsCompleted -> R.string.task_field_completed_label
        TaskField.CompletedDate -> R.string.task_field_completed_date_label
        TaskField.IsArchived -> R.string.task_field_archived_label
        TaskField.ArchivedDate -> R.string.task_field_archived_date_label
        TaskField.Priority -> R.string.task_field_priority_label
        TaskField.CreatedDate -> R.string.task_field_created_date_label
        TaskField.LastModifiedDate -> R.string.task_field_last_modified_date_label
        TaskField.DeletedDate -> R.string.task_field_deleted_label
    }
