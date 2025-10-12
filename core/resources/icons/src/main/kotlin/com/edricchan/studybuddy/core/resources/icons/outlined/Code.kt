package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons


/**
 * `code` from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:code:FILL@0;wght@400;GRAD@0;opsz@24).
 */
val AppIcons.Outlined.Code: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.Code",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveTo(320f, 720f)
            lineTo(80f, 480f)
            lineToRelative(240f, -240f)
            lineToRelative(57f, 57f)
            lineToRelative(-184f, 184f)
            lineToRelative(183f, 183f)
            lineToRelative(-56f, 56f)
            close()
            moveTo(640f, 720f)
            lineTo(583f, 663f)
            lineTo(767f, 479f)
            lineTo(584f, 296f)
            lineTo(640f, 240f)
            lineTo(880f, 480f)
            lineTo(640f, 720f)
            close()
        }
    }.build()
}
