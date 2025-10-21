package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * `article` from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:article:FILL@0;wght@400;GRAD@0;opsz@24).
 */
val AppIcons.Outlined.Article: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.Article",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveTo(280f, 680f)
            horizontalLineToRelative(280f)
            verticalLineToRelative(-80f)
            lineTo(280f, 600f)
            verticalLineToRelative(80f)
            close()
            moveTo(280f, 520f)
            horizontalLineToRelative(400f)
            verticalLineToRelative(-80f)
            lineTo(280f, 440f)
            verticalLineToRelative(80f)
            close()
            moveTo(280f, 360f)
            horizontalLineToRelative(400f)
            verticalLineToRelative(-80f)
            lineTo(280f, 280f)
            verticalLineToRelative(80f)
            close()
            moveTo(200f, 840f)
            quadToRelative(-33f, 0f, -56.5f, -23.5f)
            reflectiveQuadTo(120f, 760f)
            verticalLineToRelative(-560f)
            quadToRelative(0f, -33f, 23.5f, -56.5f)
            reflectiveQuadTo(200f, 120f)
            horizontalLineToRelative(560f)
            quadToRelative(33f, 0f, 56.5f, 23.5f)
            reflectiveQuadTo(840f, 200f)
            verticalLineToRelative(560f)
            quadToRelative(0f, 33f, -23.5f, 56.5f)
            reflectiveQuadTo(760f, 840f)
            lineTo(200f, 840f)
            close()
            moveTo(200f, 760f)
            horizontalLineToRelative(560f)
            verticalLineToRelative(-560f)
            lineTo(200f, 200f)
            verticalLineToRelative(560f)
            close()
            moveTo(200f, 200f)
            verticalLineToRelative(560f)
            verticalLineToRelative(-560f)
            close()
        }
    }.build()
}
