package com.edricchan.studybuddy.features.tasks.ui.utils

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

/**
 * Converts the [String] receiver to its [Color] equivalent, as specified by [toColorInt].
 * @see toColorInt
 */
internal val String.composeColor: Color
    get() = Color(toColorInt())
