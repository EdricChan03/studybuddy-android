package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * Outlined `event` icon from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:event:FILL@0;wght@400;GRAD@0;opsz@24).
 */
val AppIcons.Outlined.Event: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.Event",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveTo(509f, 691f)
            quadToRelative(-29f, -29f, -29f, -71f)
            reflectiveQuadToRelative(29f, -71f)
            quadToRelative(29f, -29f, 71f, -29f)
            reflectiveQuadToRelative(71f, 29f)
            quadToRelative(29f, 29f, 29f, 71f)
            reflectiveQuadToRelative(-29f, 71f)
            quadToRelative(-29f, 29f, -71f, 29f)
            reflectiveQuadToRelative(-71f, -29f)
            close()
            moveTo(200f, 880f)
            quadToRelative(-33f, 0f, -56.5f, -23.5f)
            reflectiveQuadTo(120f, 800f)
            verticalLineToRelative(-560f)
            quadToRelative(0f, -33f, 23.5f, -56.5f)
            reflectiveQuadTo(200f, 160f)
            horizontalLineToRelative(40f)
            verticalLineToRelative(-80f)
            horizontalLineToRelative(80f)
            verticalLineToRelative(80f)
            horizontalLineToRelative(320f)
            verticalLineToRelative(-80f)
            horizontalLineToRelative(80f)
            verticalLineToRelative(80f)
            horizontalLineToRelative(40f)
            quadToRelative(33f, 0f, 56.5f, 23.5f)
            reflectiveQuadTo(840f, 240f)
            verticalLineToRelative(560f)
            quadToRelative(0f, 33f, -23.5f, 56.5f)
            reflectiveQuadTo(760f, 880f)
            lineTo(200f, 880f)
            close()
            moveTo(200f, 800f)
            horizontalLineToRelative(560f)
            verticalLineToRelative(-400f)
            lineTo(200f, 400f)
            verticalLineToRelative(400f)
            close()
            moveTo(200f, 320f)
            horizontalLineToRelative(560f)
            verticalLineToRelative(-80f)
            lineTo(200f, 240f)
            verticalLineToRelative(80f)
            close()
            moveTo(200f, 320f)
            verticalLineToRelative(-80f)
            verticalLineToRelative(80f)
            close()
        }
    }.build()
}
