package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * Outlined `keyboard_arrow_down` icon from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:keyboard_arrow_down:FILL@0;wght@400;GRAD@0;opsz@24).
 */
val AppIcons.Outlined.KeyboardArrowDown: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.KeyboardArrowDown",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveTo(480f, 616f)
            lineTo(240f, 376f)
            lineToRelative(56f, -56f)
            lineToRelative(184f, 184f)
            lineToRelative(184f, -184f)
            lineToRelative(56f, 56f)
            lineToRelative(-240f, 240f)
            close()
        }
    }.build()
}
