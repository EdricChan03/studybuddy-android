package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * `open_in_browser` from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:open_in_browser:FILL@0;wght@400;GRAD@0;opsz@24).
 */
val AppIcons.Outlined.OpenInBrowser: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.OpenInBrowser",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
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
            lineTo(600f, 840f)
            verticalLineToRelative(-80f)
            horizontalLineToRelative(160f)
            verticalLineToRelative(-480f)
            lineTo(200f, 280f)
            verticalLineToRelative(480f)
            horizontalLineToRelative(160f)
            verticalLineToRelative(80f)
            lineTo(200f, 840f)
            close()
            moveTo(440f, 840f)
            verticalLineToRelative(-246f)
            lineToRelative(-64f, 64f)
            lineToRelative(-56f, -58f)
            lineToRelative(160f, -160f)
            lineToRelative(160f, 160f)
            lineToRelative(-56f, 58f)
            lineToRelative(-64f, -64f)
            verticalLineToRelative(246f)
            horizontalLineToRelative(-80f)
            close()
        }
    }.build()
}
