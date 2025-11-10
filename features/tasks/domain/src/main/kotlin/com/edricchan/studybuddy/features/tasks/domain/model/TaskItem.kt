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
) : HasId, WithTimestampMetadata {
    enum class Field {
        Title,
        Content,
        DueDate,
        IsCompleted,
        IsArchived,
        Tags,
        Project,
        CreatedAt,
        LastModified
    }

    sealed class FieldValue<T>(
        val field: Field,
        open val value: T
    ) {
        data class Title(override val value: String) : FieldValue<String>(
            field = Field.Title,
            value = value
        )

        data class Content(override val value: String?) : FieldValue<String?>(
            field = Field.Content,
            value = value
        )

        data class DueDate(override val value: Instant?) : FieldValue<Instant?>(
            field = Field.DueDate,
            value = value
        )

        data class IsCompleted(override val value: Boolean) : FieldValue<Boolean>(
            field = Field.IsCompleted,
            value = value
        )

        data class IsArchived(override val value: Boolean) : FieldValue<Boolean>(
            field = Field.IsArchived,
            value = value
        )

        data class Tags(override val value: Set<String>?) : FieldValue<Set<String>?>(
            field = Field.Tags,
            value = value
        )

        data class Project(override val value: TaskProject?) : FieldValue<TaskProject?>(
            field = Field.Project,
            value = value
        )
    }
}
