package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * Outlined `search` icon from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:search:FILL@0;wght@400;GRAD@0;opsz@24).
 */
val AppIcons.Outlined.Search: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.Search",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveTo(784f, 840f)
            lineTo(532f, 588f)
            quadToRelative(-30f, 24f, -69f, 38f)
            reflectiveQuadToRelative(-83f, 14f)
            quadToRelative(-109f, 0f, -184.5f, -75.5f)
            reflectiveQuadTo(120f, 380f)
            quadToRelative(0f, -109f, 75.5f, -184.5f)
            reflectiveQuadTo(380f, 120f)
            quadToRelative(109f, 0f, 184.5f, 75.5f)
            reflectiveQuadTo(640f, 380f)
            quadToRelative(0f, 44f, -14f, 83f)
            reflectiveQuadToRelative(-38f, 69f)
            lineToRelative(252f, 252f)
            lineToRelative(-56f, 56f)
            close()
            moveTo(380f, 560f)
            quadToRelative(75f, 0f, 127.5f, -52.5f)
            reflectiveQuadTo(560f, 380f)
            quadToRelative(0f, -75f, -52.5f, -127.5f)
            reflectiveQuadTo(380f, 200f)
            quadToRelative(-75f, 0f, -127.5f, 52.5f)
            reflectiveQuadTo(200f, 380f)
            quadToRelative(0f, 75f, 52.5f, 127.5f)
            reflectiveQuadTo(380f, 560f)
            close()
        }
    }.build()
}
