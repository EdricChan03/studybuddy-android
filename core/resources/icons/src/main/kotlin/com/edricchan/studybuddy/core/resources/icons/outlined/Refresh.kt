package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * Outlined `refresh` icon from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:refresh:FILL@0;wght@400;GRAD@0;opsz@24&icon.query=refresh&icon.size=24&icon.color=%23e3e3e3)
 */
val AppIcons.Outlined.Refresh: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.Refresh",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveTo(480f, 800f)
            quadToRelative(-134f, 0f, -227f, -93f)
            reflectiveQuadToRelative(-93f, -227f)
            quadToRelative(0f, -134f, 93f, -227f)
            reflectiveQuadToRelative(227f, -93f)
            quadToRelative(69f, 0f, 132f, 28.5f)
            reflectiveQuadTo(720f, 270f)
            verticalLineToRelative(-110f)
            horizontalLineToRelative(80f)
            verticalLineToRelative(280f)
            lineTo(520f, 440f)
            verticalLineToRelative(-80f)
            horizontalLineToRelative(168f)
            quadToRelative(-32f, -56f, -87.5f, -88f)
            reflectiveQuadTo(480f, 240f)
            quadToRelative(-100f, 0f, -170f, 70f)
            reflectiveQuadToRelative(-70f, 170f)
            quadToRelative(0f, 100f, 70f, 170f)
            reflectiveQuadToRelative(170f, 70f)
            quadToRelative(77f, 0f, 139f, -44f)
            reflectiveQuadToRelative(87f, -116f)
            horizontalLineToRelative(84f)
            quadToRelative(-28f, 106f, -114f, 173f)
            reflectiveQuadToRelative(-196f, 67f)
            close()
        }
    }.build()
}
