package com.edricchan.studybuddy.features.tasks.domain.model.create

import com.edricchan.studybuddy.data.common.Color

data class CreateTaskProjectInput(
    val name: String,
    val color: Color,
)
