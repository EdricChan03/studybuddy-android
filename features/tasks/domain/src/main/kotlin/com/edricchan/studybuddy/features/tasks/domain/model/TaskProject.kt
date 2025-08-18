package com.edricchan.studybuddy.features.tasks.domain.model

import com.edricchan.studybuddy.data.common.Color
import com.edricchan.studybuddy.data.common.HasId
import com.edricchan.studybuddy.domain.common.WithTimestampMetadata
import java.time.Instant

/**
 * Specifies a project that a [TaskItem] is assigned to.
 * @property name The name of this project.
 * @property color The colour of this project.
 */
data class TaskProject(
    override val id: String,
    val name: String,
    val color: Color? = null,
    override val createdAt: Instant,
    override val lastModified: Instant
) : HasId, WithTimestampMetadata
