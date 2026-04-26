package com.edricchan.studybuddy.features.tasks.data.model.create

import androidx.annotation.ColorInt
import com.edricchan.studybuddy.data.common.DtoModel
import com.edricchan.studybuddy.features.tasks.data.model.TodoProject
import com.edricchan.studybuddy.features.tasks.domain.model.create.CreateTaskProjectInput

data class CreateTaskProjectDto(
    val name: String,
    @field:ColorInt
    val colorInt: Int?,
    @Deprecated("Kept for backwards compatibility, use colorInt where preferable")
    val color: String? = colorInt?.let { "#%06X".format(it and 0xFFFFFF) }
) : DtoModel {
    companion object {
        @Deprecated("Use CreateTaskItemDto directly where applicable")
        fun fromDto(item: TodoProject): CreateTaskProjectDto = CreateTaskProjectDto(
            name = item.name.orEmpty(),
            colorInt = item.colorInt,
            color = item.color
        )
    }
}

fun CreateTaskProjectInput.toDto(): CreateTaskProjectDto = CreateTaskProjectDto(
    name = name,
    colorInt = color?.value,
    color = color?.toRgbHexString()
)
