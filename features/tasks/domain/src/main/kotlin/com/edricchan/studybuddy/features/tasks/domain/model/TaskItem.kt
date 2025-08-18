package com.edricchan.studybuddy.features.tasks.domain.model

import com.edricchan.studybuddy.data.common.HasId
import com.edricchan.studybuddy.domain.common.WithTimestampMetadata
import java.time.Instant

/**
 * Specifies a task.
 * @property title The title of this task.
 * @property content The contents of this task.
 * @property dueDate The due date of this task.
 * @property isCompleted Whether the task is completed.
 * @property isArchived Whether the task has been archived.
 * @property tags A list of tags applied to this task, if any.
 * @property project A project that this task is assigned to, if any.
 */
data class TaskItem(
    override val id: String,
    val title: String,
    val content: String? = null,
    val dueDate: Instant? = null,
    val isCompleted: Boolean = false,
    val isArchived: Boolean = false,
    val tags: Set<String>? = null,
    val project: TaskProject? = null,
    override val createdAt: Instant,
    override val lastModified: Instant
) : HasId, WithTimestampMetadata
