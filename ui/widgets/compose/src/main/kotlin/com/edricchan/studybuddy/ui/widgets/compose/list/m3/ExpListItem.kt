package com.edricchan.studybuddy.ui.widgets.compose.list.m3

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
object ExpListItemDefaults {
    /** Desired [Shape] to use when a [ExpListItem] is used on its own. */
    @get:Composable
    val baseShape: CornerBasedShape get() = MaterialTheme.shapes.large

    val categoryCornerDp: Dp = 2.dp

    private val categoryCornerSize = CornerSize(categoryCornerDp)

    /** Desired [Shape] to use for the first [ExpListItem] in a list. */
    @get:Composable
    val firstItemShape: CornerBasedShape
        get() = baseShape.copy(
            bottomStart = categoryCornerSize,
            bottomEnd = categoryCornerSize
        )

}
