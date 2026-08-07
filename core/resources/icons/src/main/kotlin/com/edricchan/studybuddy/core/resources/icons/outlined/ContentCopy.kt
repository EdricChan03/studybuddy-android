package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * Outlined `content_copy` icon from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:content_copy:FILL@0;wght@400;GRAD@0;opsz@24).
 */
val AppIcons.Outlined.ContentCopy: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.ContentCopy",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveTo(360f, 720f)
            quadToRelative(-33f, 0f, -56.5f, -23.5f)
            reflectiveQuadTo(280f, 640f)
            verticalLineToRelative(-480f)
            quadToRelative(0f, -33f, 23.5f, -56.5f)
            reflectiveQuadTo(360f, 80f)
            horizontalLineToRelative(360f)
            quadToRelative(33f, 0f, 56.5f, 23.5f)
            reflectiveQuadTo(800f, 160f)
            verticalLineToRelative(480f)
            quadToRelative(0f, 33f, -23.5f, 56.5f)
            reflectiveQuadTo(720f, 720f)
            lineTo(360f, 720f)
            close()
            moveTo(360f, 640f)
            horizontalLineToRelative(360f)
            verticalLineToRelative(-480f)
            lineTo(360f, 160f)
            verticalLineToRelative(480f)
            close()
            moveTo(200f, 880f)
            quadToRelative(-33f, 0f, -56.5f, -23.5f)
            reflectiveQuadTo(120f, 800f)
            verticalLineToRelative(-560f)
            horizontalLineToRelative(80f)
            verticalLineToRelative(560f)
            horizontalLineToRelative(440f)
            verticalLineToRelative(80f)
            lineTo(200f, 880f)
            close()
            moveTo(360f, 640f)
            verticalLineToRelative(-480f)
            verticalLineToRelative(480f)
            close()
        }
    }.build()
}
