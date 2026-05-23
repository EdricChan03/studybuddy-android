package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * Outlined `arrow_back` icon from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:arrow_back:FILL@0;wght@400;GRAD@0;opsz@24).
 */
val AppIcons.Outlined.ArrowBack: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.ArrowBack",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f,
        autoMirror = true
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveToRelative(313f, 520f)
            lineToRelative(224f, 224f)
            lineToRelative(-57f, 56f)
            lineToRelative(-320f, -320f)
            lineToRelative(320f, -320f)
            lineToRelative(57f, 56f)
            lineToRelative(-224f, 224f)
            horizontalLineToRelative(487f)
            verticalLineToRelative(80f)
            lineTo(313f, 520f)
            close()
        }
    }.build()
}
