package com.edricchan.studybuddy.core.resources.icons.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * Filled `task` icon from the
 * [Material Symbols icon font](https://fonts.google.com/icons?selected=Material+Symbols+Outlined:task:FILL@1;wght@400;GRAD@0;opsz@24&icon.query=task&icon.size=24&icon.color=%23e3e3e3)
 */
val AppIcons.Filled.Task: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Filled.Task",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveToRelative(438f, 720f)
            lineToRelative(226f, -226f)
            lineToRelative(-58f, -58f)
            lineToRelative(-169f, 169f)
            lineToRelative(-84f, -84f)
            lineToRelative(-57f, 57f)
            lineToRelative(142f, 142f)
            close()
            moveTo(240f, 880f)
            quadToRelative(-33f, 0f, -56.5f, -23.5f)
            reflectiveQuadTo(160f, 800f)
            verticalLineToRelative(-640f)
            quadToRelative(0f, -33f, 23.5f, -56.5f)
            reflectiveQuadTo(240f, 80f)
            horizontalLineToRelative(320f)
            lineToRelative(240f, 240f)
            verticalLineToRelative(480f)
            quadToRelative(0f, 33f, -23.5f, 56.5f)
            reflectiveQuadTo(720f, 880f)
            lineTo(240f, 880f)
            close()
            moveTo(520f, 360f)
            horizontalLineToRelative(200f)
            lineTo(520f, 160f)
            verticalLineToRelative(200f)
            close()
        }
    }.build()
}
