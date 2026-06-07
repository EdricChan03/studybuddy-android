package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * Outlined `undo` icon from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:undo:FILL@0;wght@400;GRAD@0;opsz@24).
 */
val AppIcons.Outlined.Undo: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.Undo",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveTo(280f, 760f)
            verticalLineToRelative(-80f)
            horizontalLineToRelative(284f)
            quadToRelative(63f, 0f, 109.5f, -40f)
            reflectiveQuadTo(720f, 540f)
            quadToRelative(0f, -60f, -46.5f, -100f)
            reflectiveQuadTo(564f, 400f)
            lineTo(312f, 400f)
            lineToRelative(104f, 104f)
            lineToRelative(-56f, 56f)
            lineToRelative(-200f, -200f)
            lineToRelative(200f, -200f)
            lineToRelative(56f, 56f)
            lineToRelative(-104f, 104f)
            horizontalLineToRelative(252f)
            quadToRelative(97f, 0f, 166.5f, 63f)
            reflectiveQuadTo(800f, 540f)
            quadToRelative(0f, 94f, -69.5f, 157f)
            reflectiveQuadTo(564f, 760f)
            lineTo(280f, 760f)
            close()
        }
    }.build()
}
