package com.edricchan.studybuddy.features.tasks.ui.fields

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TaskProjectIconSurface(
    color: Color?,
    content: @Composable () -> Unit
) {
    Surface(
        shape = MaterialShapes.Circle.toShape(),
        color = color ?: MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        content()
    }
}
