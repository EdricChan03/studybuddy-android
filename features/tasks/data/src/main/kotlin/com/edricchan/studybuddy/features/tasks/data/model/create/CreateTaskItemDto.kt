package com.edricchan.studybuddy.features.tasks.data.model.create

import com.edricchan.studybuddy.data.common.DtoModel
import com.edricchan.studybuddy.exts.firebase.toTimestamp
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.domain.model.create.CreateTaskItemInput
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class CreateTaskItemDto(
    val title: String,
    val content: String? = null,
    val dueDate: Timestamp? = null,
    val done: Boolean = false,
    val archived: Boolean = false,
    val tags: List<String> = listOf(),
    val projectRef: DocumentReference? = null
) : DtoModel {
    companion object {
        @Deprecated("Use CreateTaskItemDto directly where applicable")
        fun fromDto(item: TodoItem): CreateTaskItemDto = CreateTaskItemDto(
            title = item.title.orEmpty(),
            content = item.content,
            dueDate = item.dueDate,
            done = item.done ?: false,
            archived = item.archived ?: false,
            tags = item.tags.orEmpty(),
            projectRef = item.project
        )
    }
}

inline fun CreateTaskItemInput.toDto(
    projectIdMapper: (String) -> DocumentReference
): CreateTaskItemDto = CreateTaskItemDto(
    title = title,
    content = description,
    dueDate = dueDate?.toTimestamp(),
    done = isCompleted,
    archived = isArchived,
    tags = tags.toList(),
    projectRef = projectId?.let(projectIdMapper)
)
