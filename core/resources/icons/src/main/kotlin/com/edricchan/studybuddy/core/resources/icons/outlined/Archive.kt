package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * Outlined `archive` icon from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:archive:FILL@0;wght@400;GRAD@0;opsz@24).
 */
val AppIcons.Outlined.Archive: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.Archive",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveToRelative(480f, 720f)
            lineToRelative(160f, -160f)
            lineToRelative(-56f, -56f)
            lineToRelative(-64f, 64f)
            verticalLineToRelative(-168f)
            horizontalLineToRelative(-80f)
            verticalLineToRelative(168f)
            lineToRelative(-64f, -64f)
            lineToRelative(-56f, 56f)
            lineToRelative(160f, 160f)
            close()
            moveTo(200f, 320f)
            verticalLineToRelative(440f)
            horizontalLineToRelative(560f)
            verticalLineToRelative(-440f)
            lineTo(200f, 320f)
            close()
            moveTo(200f, 840f)
            quadToRelative(-33f, 0f, -56.5f, -23.5f)
            reflectiveQuadTo(120f, 760f)
            verticalLineToRelative(-499f)
            quadToRelative(0f, -14f, 4.5f, -27f)
            reflectiveQuadToRelative(13.5f, -24f)
            lineToRelative(50f, -61f)
            quadToRelative(11f, -14f, 27.5f, -21.5f)
            reflectiveQuadTo(250f, 120f)
            horizontalLineToRelative(460f)
            quadToRelative(18f, 0f, 34.5f, 7.5f)
            reflectiveQuadTo(772f, 149f)
            lineToRelative(50f, 61f)
            quadToRelative(9f, 11f, 13.5f, 24f)
            reflectiveQuadToRelative(4.5f, 27f)
            verticalLineToRelative(499f)
            quadToRelative(0f, 33f, -23.5f, 56.5f)
            reflectiveQuadTo(760f, 840f)
            lineTo(200f, 840f)
            close()
            moveTo(216f, 240f)
            horizontalLineToRelative(528f)
            lineToRelative(-34f, -40f)
            lineTo(250f, 200f)
            lineToRelative(-34f, 40f)
            close()
            moveTo(480f, 540f)
            close()
        }
    }.build()
}
