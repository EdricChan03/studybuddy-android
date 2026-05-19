package com.edricchan.studybuddy.features.tasks.domain.model.create

import com.edricchan.studybuddy.data.common.Color
import kotlinx.serialization.Serializable

@Serializable
data class CreateTaskProjectInput(
    val name: String,
    val color: Color?,
)
