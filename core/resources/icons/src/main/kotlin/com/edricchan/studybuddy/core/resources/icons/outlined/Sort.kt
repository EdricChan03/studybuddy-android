package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

val AppIcons.Outlined.Sort: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.Sort",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveTo(120f, 720f)
            verticalLineToRelative(-80f)
            horizontalLineToRelative(240f)
            verticalLineToRelative(80f)
            lineTo(120f, 720f)
            close()
            moveTo(120f, 520f)
            verticalLineToRelative(-80f)
            horizontalLineToRelative(480f)
            verticalLineToRelative(80f)
            lineTo(120f, 520f)
            close()
            moveTo(120f, 320f)
            verticalLineToRelative(-80f)
            horizontalLineToRelative(720f)
            verticalLineToRelative(80f)
            lineTo(120f, 320f)
            close()
        }
    }.build()
}
