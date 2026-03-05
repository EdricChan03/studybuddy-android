package com.edricchan.studybuddy.core.resources.icons.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * Filled `settings` icon from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:settings:FILL@1;wght@400;GRAD@0;opsz@24&icon.query=settings&icon.size=24&icon.color=%23e3e3e3)
 */
val AppIcons.Filled.Settings: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Filled.Settings",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveToRelative(370f, 880f)
            lineToRelative(-16f, -128f)
            quadToRelative(-13f, -5f, -24.5f, -12f)
            reflectiveQuadTo(307f, 725f)
            lineToRelative(-119f, 50f)
            lineTo(78f, 585f)
            lineToRelative(103f, -78f)
            quadToRelative(-1f, -7f, -1f, -13.5f)
            verticalLineToRelative(-27f)
            quadToRelative(0f, -6.5f, 1f, -13.5f)
            lineTo(78f, 375f)
            lineToRelative(110f, -190f)
            lineToRelative(119f, 50f)
            quadToRelative(11f, -8f, 23f, -15f)
            reflectiveQuadToRelative(24f, -12f)
            lineToRelative(16f, -128f)
            horizontalLineToRelative(220f)
            lineToRelative(16f, 128f)
            quadToRelative(13f, 5f, 24.5f, 12f)
            reflectiveQuadToRelative(22.5f, 15f)
            lineToRelative(119f, -50f)
            lineToRelative(110f, 190f)
            lineToRelative(-103f, 78f)
            quadToRelative(1f, 7f, 1f, 13.5f)
            verticalLineToRelative(27f)
            quadToRelative(0f, 6.5f, -2f, 13.5f)
            lineToRelative(103f, 78f)
            lineToRelative(-110f, 190f)
            lineToRelative(-118f, -50f)
            quadToRelative(-11f, 8f, -23f, 15f)
            reflectiveQuadToRelative(-24f, 12f)
            lineTo(590f, 880f)
            lineTo(370f, 880f)
            close()
            moveTo(482f, 620f)
            quadToRelative(58f, 0f, 99f, -41f)
            reflectiveQuadToRelative(41f, -99f)
            quadToRelative(0f, -58f, -41f, -99f)
            reflectiveQuadToRelative(-99f, -41f)
            quadToRelative(-59f, 0f, -99.5f, 41f)
            reflectiveQuadTo(342f, 480f)
            quadToRelative(0f, 58f, 40.5f, 99f)
            reflectiveQuadToRelative(99.5f, 41f)
            close()
        }
    }.build()
}
