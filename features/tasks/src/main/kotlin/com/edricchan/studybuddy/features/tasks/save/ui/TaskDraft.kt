package com.edricchan.studybuddy.features.tasks.save.ui

import com.edricchan.studybuddy.features.tasks.domain.model.TaskProject
import java.time.LocalDate
import java.time.LocalTime

data class TaskDraft(
    val title: String,
    val description: String,
    val dueDate: LocalDate?,
    val dueTime: LocalTime?,
    val isCompleted: Boolean,
    val isArchived: Boolean,
    val tags: Set<String>,
    val project: TaskProject?
)
