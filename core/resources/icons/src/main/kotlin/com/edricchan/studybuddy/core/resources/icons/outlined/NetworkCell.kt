package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * `network_cell` from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:network_cell:FILL@0;wght@400;GRAD@0;opsz@24).
 */
val AppIcons.Outlined.NetworkCell: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.NetworkCell",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveToRelative(80f, 880f)
            lineToRelative(800f, -800f)
            verticalLineToRelative(800f)
            lineTo(80f, 880f)
            close()
            moveTo(680f, 800f)
            horizontalLineToRelative(120f)
            verticalLineToRelative(-526f)
            lineTo(680f, 394f)
            verticalLineToRelative(406f)
            close()
        }
    }.build()
}
