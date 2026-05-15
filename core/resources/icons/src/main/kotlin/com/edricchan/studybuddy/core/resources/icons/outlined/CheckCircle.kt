package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * Outlined `check_circle` icon from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:check_circle:FILL@0;wght@400;GRAD@0;opsz@24).
 */
val AppIcons.Outlined.CheckCircle: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.CheckCircle",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveToRelative(424f, 664f)
            lineToRelative(282f, -282f)
            lineToRelative(-56f, -56f)
            lineToRelative(-226f, 226f)
            lineToRelative(-114f, -114f)
            lineToRelative(-56f, 56f)
            lineToRelative(170f, 170f)
            close()
            moveTo(480f, 880f)
            quadToRelative(-83f, 0f, -156f, -31.5f)
            reflectiveQuadTo(197f, 763f)
            quadToRelative(-54f, -54f, -85.5f, -127f)
            reflectiveQuadTo(80f, 480f)
            quadToRelative(0f, -83f, 31.5f, -156f)
            reflectiveQuadTo(197f, 197f)
            quadToRelative(54f, -54f, 127f, -85.5f)
            reflectiveQuadTo(480f, 80f)
            quadToRelative(83f, 0f, 156f, 31.5f)
            reflectiveQuadTo(763f, 197f)
            quadToRelative(54f, 54f, 85.5f, 127f)
            reflectiveQuadTo(880f, 480f)
            quadToRelative(0f, 83f, -31.5f, 156f)
            reflectiveQuadTo(763f, 763f)
            quadToRelative(-54f, 54f, -127f, 85.5f)
            reflectiveQuadTo(480f, 880f)
            close()
            moveTo(480f, 800f)
            quadToRelative(134f, 0f, 227f, -93f)
            reflectiveQuadToRelative(93f, -227f)
            quadToRelative(0f, -134f, -93f, -227f)
            reflectiveQuadToRelative(-227f, -93f)
            quadToRelative(-134f, 0f, -227f, 93f)
            reflectiveQuadToRelative(-93f, 227f)
            quadToRelative(0f, 134f, 93f, 227f)
            reflectiveQuadToRelative(227f, 93f)
            close()
            moveTo(480f, 480f)
            close()
        }
    }.build()
}
