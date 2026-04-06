package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * Outlined `label` icon from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:label:FILL@0;wght@400;GRAD@0;opsz@24).
 */
val AppIcons.Outlined.Label: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.Label",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveTo(160f, 800f)
            quadToRelative(-33f, 0f, -56.5f, -23.5f)
            reflectiveQuadTo(80f, 720f)
            verticalLineToRelative(-480f)
            quadToRelative(0f, -33f, 23.5f, -56.5f)
            reflectiveQuadTo(160f, 160f)
            horizontalLineToRelative(440f)
            quadToRelative(19f, 0f, 36f, 8.5f)
            reflectiveQuadToRelative(28f, 23.5f)
            lineToRelative(216f, 288f)
            lineToRelative(-216f, 288f)
            quadToRelative(-11f, 15f, -28f, 23.5f)
            reflectiveQuadToRelative(-36f, 8.5f)
            lineTo(160f, 800f)
            close()
            moveTo(160f, 720f)
            horizontalLineToRelative(440f)
            lineToRelative(180f, -240f)
            lineToRelative(-180f, -240f)
            lineTo(160f, 240f)
            verticalLineToRelative(480f)
            close()
            moveTo(380f, 480f)
            close()
        }
    }.build()
}
