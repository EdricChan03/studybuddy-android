package com.edricchan.studybuddy.features.tasks.data.mapper

import android.annotation.SuppressLint
import com.edricchan.studybuddy.data.common.Color
import com.edricchan.studybuddy.exts.firebase.toTimestamp
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.data.model.TodoProject
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import com.edricchan.studybuddy.features.tasks.domain.model.TaskProject
import com.google.firebase.firestore.DocumentReference
import java.time.Instant

inline fun TaskItem.toDto(
    projectMapper: (TaskProject) -> DocumentReference
): TodoItem = TodoItem(
    id = id,
    title = title,
    content = content,
    dueDate = dueDate?.toTimestamp(),
    done = isCompleted,
    archived = isArchived,
    tags = tags?.toList(),
    project = project?.let(projectMapper),
    createdAt = createdAt.toTimestamp(),
    lastModified = lastModified.toTimestamp()
)

@SuppressLint("NewApi")
fun TodoItem.toDomain(
    project: TaskProject?
): TaskItem = TaskItem(
    id = id,
    title = title.orEmpty(),
    content = content,
    dueDate = dueDate?.toInstant(),
    isCompleted = done ?: false,
    isArchived = archived ?: false,
    project = project,
    tags = tags?.toSet(),
    createdAt = createdAt?.toInstant() ?: Instant.now(),
    lastModified = lastModified?.toInstant() ?: Instant.now()
)

fun TaskProject.toDto(): TodoProject = TodoProject(
    id = id,
    name = name,
    color = color?.toRgbHexString(),
    createdAt = createdAt.toTimestamp(),
    lastModified = lastModified.toTimestamp()
)

@Suppress("DEPRECATION") // For deprecated color property
@SuppressLint("NewApi") // For Timestamp#toInstant
fun TodoProject.toDomain(defaultName: String = name.orEmpty()): TaskProject = TaskProject(
    id = id,
    name = name ?: defaultName,
    color = colorInt?.let(::Color) ?: color?.let(::Color),
    createdAt = createdAt?.toInstant() ?: Instant.now(),
    lastModified = lastModified?.toInstant() ?: Instant.now()
)

fun TodoItem.Field.toDomain(): TaskItem.Field = when (this) {
    TodoItem.Field.Title -> TaskItem.Field.Title
    TodoItem.Field.Content -> TaskItem.Field.Content
    TodoItem.Field.DueDate -> TaskItem.Field.DueDate
    TodoItem.Field.Tags -> TaskItem.Field.Tags
    TodoItem.Field.Project -> TaskItem.Field.Project
    TodoItem.Field.IsDone -> TaskItem.Field.IsCompleted
    TodoItem.Field.IsArchived -> TaskItem.Field.IsArchived
    TodoItem.Field.CreatedAt -> TaskItem.Field.CreatedAt
    TodoItem.Field.LastModified -> TaskItem.Field.LastModified
}

fun TaskItem.Field.toDto(): TodoItem.Field = when (this) {
    TaskItem.Field.Title -> TodoItem.Field.Title
    TaskItem.Field.Content -> TodoItem.Field.Content
    TaskItem.Field.DueDate -> TodoItem.Field.DueDate
    TaskItem.Field.Tags -> TodoItem.Field.Tags
    TaskItem.Field.Project -> TodoItem.Field.Project
    TaskItem.Field.IsCompleted -> TodoItem.Field.IsDone
    TaskItem.Field.IsArchived -> TodoItem.Field.IsArchived
    TaskItem.Field.CreatedAt -> TodoItem.Field.CreatedAt
    TaskItem.Field.LastModified -> TodoItem.Field.LastModified
}

fun TodoProject.Field.toDomain(): TaskProject.Field = when (this) {
    TodoProject.Field.Color, TodoProject.Field.ColorInt -> TaskProject.Field.Color
    TodoProject.Field.Name -> TaskProject.Field.Name
    TodoProject.Field.CreatedAt -> TaskProject.Field.CreatedAt
    TodoProject.Field.LastModified -> TaskProject.Field.LastModified
}

fun TaskProject.Field.toDto(): TodoProject.Field = when (this) {
    TaskProject.Field.Name -> TodoProject.Field.Name
    TaskProject.Field.Color -> TodoProject.Field.ColorInt
    TaskProject.Field.CreatedAt -> TodoProject.Field.CreatedAt
    TaskProject.Field.LastModified -> TodoProject.Field.LastModified
}
