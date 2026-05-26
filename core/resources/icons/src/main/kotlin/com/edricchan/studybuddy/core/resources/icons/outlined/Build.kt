package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * Outlined `build` icon from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:build:FILL@0;wght@400;GRAD@0;opsz@24).
 */
val AppIcons.Outlined.Build: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.Build",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveTo(686f, 828f)
            lineTo(444f, 584f)
            quadToRelative(-20f, 8f, -40.5f, 12f)
            reflectiveQuadToRelative(-43.5f, 4f)
            quadToRelative(-100f, 0f, -170f, -70f)
            reflectiveQuadToRelative(-70f, -170f)
            quadToRelative(0f, -36f, 10f, -68.5f)
            reflectiveQuadToRelative(28f, -61.5f)
            lineToRelative(146f, 146f)
            lineToRelative(72f, -72f)
            lineToRelative(-146f, -146f)
            quadToRelative(29f, -18f, 61.5f, -28f)
            reflectiveQuadToRelative(68.5f, -10f)
            quadToRelative(100f, 0f, 170f, 70f)
            reflectiveQuadToRelative(70f, 170f)
            quadToRelative(0f, 23f, -4f, 43.5f)
            reflectiveQuadTo(584f, 444f)
            lineToRelative(244f, 242f)
            quadToRelative(12f, 12f, 12f, 29f)
            reflectiveQuadToRelative(-12f, 29f)
            lineToRelative(-84f, 84f)
            quadToRelative(-12f, 12f, -29f, 12f)
            reflectiveQuadToRelative(-29f, -12f)
            close()
            moveTo(715f, 743f)
            lineTo(742f, 716f)
            lineTo(486f, 460f)
            quadToRelative(18f, -20f, 26f, -46.5f)
            reflectiveQuadToRelative(8f, -53.5f)
            quadToRelative(0f, -60f, -38.5f, -104.5f)
            reflectiveQuadTo(386f, 202f)
            lineToRelative(74f, 74f)
            quadToRelative(12f, 12f, 12f, 28f)
            reflectiveQuadToRelative(-12f, 28f)
            lineTo(332f, 460f)
            quadToRelative(-12f, 12f, -28f, 12f)
            reflectiveQuadToRelative(-28f, -12f)
            lineToRelative(-74f, -74f)
            quadToRelative(9f, 57f, 53.5f, 95.5f)
            reflectiveQuadTo(360f, 520f)
            quadToRelative(26f, 0f, 52f, -8f)
            reflectiveQuadToRelative(47f, -25f)
            lineToRelative(256f, 256f)
            close()
            moveTo(472f, 472f)
            close()
        }
    }.build()
}
