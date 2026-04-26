package com.edricchan.studybuddy.features.tasks.data.model.create

import androidx.annotation.ColorInt
import com.edricchan.studybuddy.data.common.DtoModel
import com.edricchan.studybuddy.features.tasks.domain.model.create.CreateTaskProjectInput

data class CreateTaskProjectDto(
    val name: String,
    @field:ColorInt
    val colorInt: Int,
    @Deprecated("Kept for backwards compatibility, use colorInt where preferable")
    val color: String = "#%06X".format(colorInt and 0xFFFFFF)
) : DtoModel

fun CreateTaskProjectInput.toDto(): CreateTaskProjectDto = CreateTaskProjectDto(
    name = name,
    colorInt = color.value,
    color = color.toRgbHexString()
)
