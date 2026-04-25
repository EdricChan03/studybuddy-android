package com.edricchan.studybuddy.features.tasks.domain.model.create

import java.time.Instant

data class CreateTaskItemInput(
    val title: String,
    val description: String? = null,
    val dueDate: Instant? = null,
    val isCompleted: Boolean = false,
    val isArchived: Boolean = false,
    val tags: Set<String> = setOf(),
    val projectId: String? = null
)
