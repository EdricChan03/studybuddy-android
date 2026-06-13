package com.edricchan.studybuddy.features.tasks.domain.sample

import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import com.edricchan.studybuddy.features.tasks.domain.model.TaskProject
import java.time.Duration
import java.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
private fun generateId() = Uuid.random().toString()

private fun generateTask(
    title: String,
    content: String? = null,
    dueDate: Instant? = null,
    isCompleted: Boolean = false,
    isArchived: Boolean = false,
    tags: Set<String>? = null,
    project: TaskProject? = null,
    createdAt: Instant = Instant.now(),
    lastModified: Instant = Instant.now()
): TaskItem = TaskItem(
    id = generateId(),
    title = title,
    content = content,
    dueDate = dueDate,
    completedDate = if (isCompleted) Instant.now() else null,
    archivedDate = if (isArchived) Instant.now() else null,
    tags = tags,
    project = project,
    createdAt = createdAt,
    lastModified = lastModified
)

val SampleTaskItems: List<TaskItem> = listOf(
    generateTask(
        title = "Rewrite StudyBuddy to Jetpack Compose",
        project = SampleTaskProjects.first { it.name == "StudyBuddy rewrite" }
    ),
    generateTask(
        title = "Generate sample task data",
        content = "There should be an easy way of previewing the task data",
        dueDate = Instant.now() + Duration.ofDays(14),
        project = SampleTaskProjects.first { it.name == "StudyBuddy rewrite" }
    ),
    generateTask(
        title = "Learn about organic chemicals",
        dueDate = Instant.now() + Duration.ofDays(28),
        isArchived = true,
        tags = setOf("chemistry", "organic chemicals"),
        project = SampleTaskProjects.first { it.name == "Chemistry" }
    ),
    generateTask(
        title = "Learn Japanese fundamentals",
        content = "Learn about Japanese **kanji** beforehand, then maybe the _grammatical syntax_",
        isCompleted = true
    ),
)
