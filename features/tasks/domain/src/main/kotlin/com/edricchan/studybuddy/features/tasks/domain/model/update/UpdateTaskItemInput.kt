package com.edricchan.studybuddy.features.tasks.domain.model.update

import com.edricchan.studybuddy.data.serialization.jtime.SerializableInstant
import com.edricchan.studybuddy.domain.common.updater.FieldUpdate
import kotlinx.serialization.Serializable

@Serializable
data class UpdateTaskItemInput(
    val title: FieldUpdate<String> = FieldUpdate.Unchanged,
    val description: FieldUpdate<String?> = FieldUpdate.Unchanged,
    val dueDate: FieldUpdate<SerializableInstant?> = FieldUpdate.Unchanged,
    val isCompleted: FieldUpdate<Boolean> = FieldUpdate.Unchanged,
    val isArchived: FieldUpdate<Boolean> = FieldUpdate.Unchanged,
    val isSoftDeleted: FieldUpdate<Boolean> = FieldUpdate.Unchanged,
    val tags: FieldUpdate<Set<String>> = FieldUpdate.Unchanged,
    val projectId: FieldUpdate<String?> = FieldUpdate.Unchanged
)
