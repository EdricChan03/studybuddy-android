package com.edricchan.studybuddy.core.resources.icons.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * Filled `system_update_alt` icon from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:mobile_arrow_down:FILL@1;wght@400;GRAD@0;opsz@24&icon.size=24&icon.color=%23e3e3e3)
 */
val AppIcons.Filled.MobileArrowDown: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Filled.MobileArrowDown",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveTo(280f, 920f)
            quadToRelative(-33f, 0f, -56.5f, -23.5f)
            reflectiveQuadTo(200f, 840f)
            verticalLineToRelative(-720f)
            quadToRelative(0f, -33f, 23.5f, -56.5f)
            reflectiveQuadTo(280f, 40f)
            horizontalLineToRelative(400f)
            quadToRelative(33f, 0f, 56.5f, 23.5f)
            reflectiveQuadTo(760f, 120f)
            verticalLineToRelative(124f)
            quadToRelative(18f, 7f, 29f, 22f)
            reflectiveQuadToRelative(11f, 34f)
            verticalLineToRelative(80f)
            quadToRelative(0f, 19f, -11f, 34f)
            reflectiveQuadToRelative(-29f, 22f)
            verticalLineToRelative(404f)
            quadToRelative(0f, 33f, -23.5f, 56.5f)
            reflectiveQuadTo(680f, 920f)
            lineTo(280f, 920f)
            close()
            moveTo(480f, 640f)
            lineTo(640f, 480f)
            lineTo(584f, 424f)
            lineTo(520f, 486f)
            verticalLineToRelative(-166f)
            horizontalLineToRelative(-80f)
            verticalLineToRelative(166f)
            lineToRelative(-64f, -62f)
            lineToRelative(-56f, 56f)
            lineToRelative(160f, 160f)
            close()
        }
    }.build()
}
