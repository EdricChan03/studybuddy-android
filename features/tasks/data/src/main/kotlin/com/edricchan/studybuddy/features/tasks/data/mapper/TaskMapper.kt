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

@SuppressLint("NewApi")
fun TodoProject.toDomain(): TaskProject = TaskProject(
    id = id,
    name = name.orEmpty(),
    color = color?.let { Color(hexString = it) },
    createdAt = createdAt?.toInstant() ?: Instant.now(),
    lastModified = lastModified?.toInstant() ?: Instant.now()
)
