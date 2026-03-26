package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * `mobile_info` from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:mobile_info:FILL@0;wght@400;GRAD@0;opsz@24).
 */
val AppIcons.Outlined.MobileInfo: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.MobileInfo",
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
            moveTo(280f, 840f)
            horizontalLineToRelative(400f)
            verticalLineToRelative(-720f)
            lineTo(280f, 120f)
            verticalLineToRelative(720f)
            close()
            moveTo(280f, 840f)
            verticalLineToRelative(-720f)
            verticalLineToRelative(720f)
            close()
            moveTo(440f, 660f)
            horizontalLineToRelative(80f)
            verticalLineToRelative(-220f)
            horizontalLineToRelative(-80f)
            verticalLineToRelative(220f)
            close()
            moveTo(508.5f, 348.5f)
            quadTo(520f, 337f, 520f, 320f)
            reflectiveQuadToRelative(-11.5f, -28.5f)
            quadTo(497f, 280f, 480f, 280f)
            reflectiveQuadToRelative(-28.5f, 11.5f)
            quadTo(440f, 303f, 440f, 320f)
            reflectiveQuadToRelative(11.5f, 28.5f)
            quadTo(463f, 360f, 480f, 360f)
            reflectiveQuadToRelative(28.5f, -11.5f)
            close()
        }
    }.build()
}
