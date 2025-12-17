package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * `open_in_new` from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:open_in_new:FILL@0;wght@400;GRAD@0;opsz@24).
 */
val AppIcons.Outlined.OpenInNew: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.OpenInNew",
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
            horizontalLineToRelative(280f)
            verticalLineToRelative(80f)
            lineTo(200f, 200f)
            verticalLineToRelative(560f)
            horizontalLineToRelative(560f)
            verticalLineToRelative(-280f)
            horizontalLineToRelative(80f)
            verticalLineToRelative(280f)
            quadToRelative(0f, 33f, -23.5f, 56.5f)
            reflectiveQuadTo(760f, 840f)
            lineTo(200f, 840f)
            close()
            moveTo(388f, 628f)
            lineTo(332f, 572f)
            lineTo(704f, 200f)
            lineTo(560f, 200f)
            verticalLineToRelative(-80f)
            horizontalLineToRelative(280f)
            verticalLineToRelative(280f)
            horizontalLineToRelative(-80f)
            verticalLineToRelative(-144f)
            lineTo(388f, 628f)
            close()
        }
    }.build()
}
