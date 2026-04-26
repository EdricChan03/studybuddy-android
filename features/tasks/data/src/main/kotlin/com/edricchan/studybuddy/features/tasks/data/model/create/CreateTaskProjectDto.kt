package com.edricchan.studybuddy.features.tasks.data.model.create

import androidx.annotation.ColorInt
import com.edricchan.studybuddy.data.common.DtoModel
import com.edricchan.studybuddy.features.tasks.domain.model.create.CreateTaskProjectInput

data class CreateTaskProjectDto(
    val name: String,
    @field:ColorInt
    val colorInt: Int
) : DtoModel

fun CreateTaskProjectInput.toDto(): CreateTaskProjectDto = CreateTaskProjectDto(
    name = name,
    colorInt = color.value
)
