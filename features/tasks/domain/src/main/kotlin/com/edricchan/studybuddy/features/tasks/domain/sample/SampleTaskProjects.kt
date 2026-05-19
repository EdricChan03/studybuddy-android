package com.edricchan.studybuddy.features.tasks.domain.sample

import com.edricchan.studybuddy.data.common.Color
import com.edricchan.studybuddy.features.tasks.domain.model.TaskProject
import java.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
private fun generateId() = Uuid.random().toString()

private fun generateProject(
    name: String,
    color: Color? = null,
    createdAt: Instant = Instant.now(),
    lastModified: Instant = Instant.now()
): TaskProject = TaskProject(
    id = generateId(),
    name = name,
    color = color,
    createdAt = createdAt,
    lastModified = lastModified
)

val SampleTaskProjects: List<TaskProject> = listOf(
    generateProject(
        name = "StudyBuddy rewrite",
        color = Color(0x0CF0C2)
    ),
    generateProject(
        name = "Mathematics"
    ),
    generateProject(
        name = "Physics",
        color = Color(0xFFEF9F)
    ),
    generateProject(
        name = "Chemistry",
        color = Color(0xABE0F0)
    ),
    generateProject(
        name = "English",
        color = Color(0x0000FF)
    )
)
