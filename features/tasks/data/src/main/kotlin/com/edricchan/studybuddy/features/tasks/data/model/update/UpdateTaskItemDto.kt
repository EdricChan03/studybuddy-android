package com.edricchan.studybuddy.features.tasks.data.model.update

import com.edricchan.studybuddy.domain.common.updater.ifAssigned
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import com.edricchan.studybuddy.features.tasks.domain.model.update.UpdateTaskItemInput
import java.time.Instant

fun UpdateTaskItemInput.toFields(): List<TaskItem.FieldValue<*>> {
    return buildList {
        title.ifAssigned { this += TaskItem.FieldValue.Title(it) }
        description.ifAssigned { this += TaskItem.FieldValue.Content(it) }
        dueDate.ifAssigned { this += TaskItem.FieldValue.DueDate(it) }
        isCompleted.ifAssigned { this += TaskItem.FieldValue.IsCompleted(it) }
        isArchived.ifAssigned { this += TaskItem.FieldValue.IsArchived(it) }
        isSoftDeleted.ifAssigned { this += TaskItem.FieldValue.DeletedDate(Instant.now()) }
        tags.ifAssigned { this += TaskItem.FieldValue.Tags(it) }
        projectId.ifAssigned { this += TaskItem.FieldValue.ProjectId(it) }
    }
}
