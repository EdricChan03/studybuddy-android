package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * Outlined `close` icon from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:close:FILL@0;wght@400;GRAD@0;opsz@24).
 */
val AppIcons.Outlined.Close: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.Close",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveToRelative(256f, 760f)
            lineToRelative(-56f, -56f)
            lineToRelative(224f, -224f)
            lineToRelative(-224f, -224f)
            lineToRelative(56f, -56f)
            lineToRelative(224f, 224f)
            lineToRelative(224f, -224f)
            lineToRelative(56f, 56f)
            lineToRelative(-224f, 224f)
            lineToRelative(224f, 224f)
            lineToRelative(-56f, 56f)
            lineToRelative(-224f, -224f)
            lineToRelative(-224f, 224f)
            close()
        }
    }.build()
}
