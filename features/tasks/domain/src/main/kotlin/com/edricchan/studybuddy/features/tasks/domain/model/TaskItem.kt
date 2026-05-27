package com.edricchan.studybuddy.features.tasks.domain.model

import androidx.annotation.IntRange
import com.edricchan.studybuddy.data.common.HasId
import com.edricchan.studybuddy.data.serialization.jtime.SerializableInstant
import com.edricchan.studybuddy.domain.common.WithTimestampMetadata
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Specifies a task.
 * @property title The title of this task.
 * @property content The contents of this task.
 * @property dueDate The due date of this task.
 * @property completedDate Timestamp when the task was last marked as completed.
 * @property archivedDate Timestamp when the task was last archived.
 * @property priority Priority of this task - lower numbers indicate a higher priority.
 * @property tags A list of tags applied to this task, if any.
 * @property project A project that this task is assigned to, if any.
 * @property deletedDate Timestamp when the task was soft deleted.
 */
@Serializable
data class TaskItem(
    override val id: String,
    val title: String,
    val content: String? = null,
    val dueDate: SerializableInstant? = null,
    val completedDate: SerializableInstant? = null,
    val archivedDate: SerializableInstant? = null,
    @field:IntRange(from = 0)
    val priority: Int = 1,
    val tags: Set<String>? = null,
    val project: TaskProject? = null,
    val deletedDate: SerializableInstant? = null,
    override val createdAt: SerializableInstant,
    override val lastModified: SerializableInstant
) : HasId, WithTimestampMetadata {
    /** Whether the task was marked as completed. */
    val isCompleted: Boolean = completedDate != null

    /** Whether the task was archived. */
    val isArchived: Boolean = archivedDate != null

    /** Whether the task was marked as deleted. */
    val isSoftDeleted: Boolean = deletedDate != null

    enum class Field {
        Title,
        Content,
        DueDate,

        @Deprecated("Use CompletedDate instead")
        IsCompleted,

        @Deprecated("Use ArchivedDate instead")
        IsArchived,
        Priority,
        Tags,
        Project,
        CreatedAt,
        LastModified,
        CompletedDate,
        ArchivedDate,
        DeletedDate
    }

    @Serializable
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

        data class CompletedDate(override val value: Instant?) : FieldValue<Instant?>(
            field = Field.CompletedDate,
            value = value
        )

        data class ArchivedDate(override val value: Instant?) : FieldValue<Instant?>(
            field = Field.ArchivedDate,
            value = value
        )

        data class DeletedDate(override val value: Instant?) : FieldValue<Instant?>(
            field = Field.DeletedDate,
            value = value
        )

        data class Priority(
            @field:IntRange(from = 0)
            override val value: Int
        ) : FieldValue<Int>(
            field = Field.Priority,
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
