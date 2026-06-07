package com.edricchan.studybuddy.utils.compose.foundation.shape

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.ZeroCornerSize

/** Returns a copy of the receiver [CornerBasedShape] with only the top corners. */
fun CornerBasedShape.top(): CornerBasedShape = copy(
    bottomStart = ZeroCornerSize,
    bottomEnd = ZeroCornerSize
)

/** Returns a copy of the receiver [CornerBasedShape] with only the bottom corners. */
fun CornerBasedShape.bottom(): CornerBasedShape = copy(
    topStart = ZeroCornerSize,
    topEnd = ZeroCornerSize
)

/** Returns a copy of the receiver [CornerBasedShape] with only the starting corners. */
fun CornerBasedShape.start(): CornerBasedShape = copy(
    topEnd = ZeroCornerSize,
    bottomEnd = ZeroCornerSize
)

/** Returns a copy of the receiver [CornerBasedShape] with only the ending corners. */
fun CornerBasedShape.end(): CornerBasedShape = copy(
    topStart = ZeroCornerSize,
    bottomStart = ZeroCornerSize
)
