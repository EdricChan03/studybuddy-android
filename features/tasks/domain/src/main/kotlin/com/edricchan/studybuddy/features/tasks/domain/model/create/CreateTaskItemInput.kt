package com.edricchan.studybuddy.features.tasks.domain.model.create

import com.edricchan.studybuddy.data.serialization.jtime.SerializableInstant
import kotlinx.serialization.Serializable

@Serializable
data class CreateTaskItemInput(
    val title: String,
    val description: String? = null,
    val dueDate: SerializableInstant? = null,
    val isCompleted: Boolean = false,
    val isArchived: Boolean = false,
    val tags: Set<String> = setOf(),
    val projectId: String? = null
)
