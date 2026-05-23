package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * Outlined `more_vert` icon from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:more_vert:FILL@0;wght@400;GRAD@0;opsz@24).
 */
val AppIcons.Outlined.MoreVert: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.MoreVert",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveTo(480f, 800f)
            quadToRelative(-33f, 0f, -56.5f, -23.5f)
            reflectiveQuadTo(400f, 720f)
            quadToRelative(0f, -33f, 23.5f, -56.5f)
            reflectiveQuadTo(480f, 640f)
            quadToRelative(33f, 0f, 56.5f, 23.5f)
            reflectiveQuadTo(560f, 720f)
            quadToRelative(0f, 33f, -23.5f, 56.5f)
            reflectiveQuadTo(480f, 800f)
            close()
            moveTo(480f, 560f)
            quadToRelative(-33f, 0f, -56.5f, -23.5f)
            reflectiveQuadTo(400f, 480f)
            quadToRelative(0f, -33f, 23.5f, -56.5f)
            reflectiveQuadTo(480f, 400f)
            quadToRelative(33f, 0f, 56.5f, 23.5f)
            reflectiveQuadTo(560f, 480f)
            quadToRelative(0f, 33f, -23.5f, 56.5f)
            reflectiveQuadTo(480f, 560f)
            close()
            moveTo(480f, 320f)
            quadToRelative(-33f, 0f, -56.5f, -23.5f)
            reflectiveQuadTo(400f, 240f)
            quadToRelative(0f, -33f, 23.5f, -56.5f)
            reflectiveQuadTo(480f, 160f)
            quadToRelative(33f, 0f, 56.5f, 23.5f)
            reflectiveQuadTo(560f, 240f)
            quadToRelative(0f, 33f, -23.5f, 56.5f)
            reflectiveQuadTo(480f, 320f)
            close()
        }
    }.build()
}
