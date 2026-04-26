package com.edricchan.studybuddy.features.tasks.domain.model

import com.edricchan.studybuddy.data.common.HasId
import com.edricchan.studybuddy.domain.common.WithTimestampMetadata
import java.time.Instant
import com.edricchan.studybuddy.data.common.Color as ColorValue

/**
 * Specifies a project that a [TaskItem] is assigned to.
 * @property name The name of this project.
 * @property color The colour of this project.
 */
data class TaskProject(
    override val id: String,
    val name: String,
    val color: ColorValue? = null,
    override val createdAt: Instant,
    override val lastModified: Instant
) : HasId, WithTimestampMetadata {
    enum class Field {
        Name,
        Color,
        CreatedAt,
        LastModified
    }

    sealed class FieldValue<T>(
        val field: Field,
        open val value: T
    ) {
        data class Name(override val value: String) : FieldValue<String>(
            field = Field.Name,
            value = value
        )

        data class Color(override val value: ColorValue?) : FieldValue<ColorValue?>(
            field = Field.Color,
            value = value
        )
    }
}
