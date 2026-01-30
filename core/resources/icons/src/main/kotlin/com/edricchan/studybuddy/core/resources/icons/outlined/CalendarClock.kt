package com.edricchan.studybuddy.core.resources.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons

val AppIcons.Outlined.CalendarClock: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Outlined.CalendarClock",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color(0xFFE3E3E3))) {
            moveTo(200f, 320f)
            horizontalLineToRelative(560f)
            verticalLineToRelative(-80f)
            lineTo(200f, 240f)
            verticalLineToRelative(80f)
            close()
            moveTo(200f, 320f)
            verticalLineToRelative(-80f)
            verticalLineToRelative(80f)
            close()
            moveTo(200f, 880f)
            quadToRelative(-33f, 0f, -56.5f, -23.5f)
            reflectiveQuadTo(120f, 800f)
            verticalLineToRelative(-560f)
            quadToRelative(0f, -33f, 23.5f, -56.5f)
            reflectiveQuadTo(200f, 160f)
            horizontalLineToRelative(40f)
            verticalLineToRelative(-80f)
            horizontalLineToRelative(80f)
            verticalLineToRelative(80f)
            horizontalLineToRelative(320f)
            verticalLineToRelative(-80f)
            horizontalLineToRelative(80f)
            verticalLineToRelative(80f)
            horizontalLineToRelative(40f)
            quadToRelative(33f, 0f, 56.5f, 23.5f)
            reflectiveQuadTo(840f, 240f)
            verticalLineToRelative(227f)
            quadToRelative(-19f, -9f, -39f, -15f)
            reflectiveQuadToRelative(-41f, -9f)
            verticalLineToRelative(-43f)
            lineTo(200f, 400f)
            verticalLineToRelative(400f)
            horizontalLineToRelative(252f)
            quadToRelative(7f, 22f, 16.5f, 42f)
            reflectiveQuadTo(491f, 880f)
            lineTo(200f, 880f)
            close()
            moveTo(720f, 920f)
            quadToRelative(-83f, 0f, -141.5f, -58.5f)
            reflectiveQuadTo(520f, 720f)
            quadToRelative(0f, -83f, 58.5f, -141.5f)
            reflectiveQuadTo(720f, 520f)
            quadToRelative(83f, 0f, 141.5f, 58.5f)
            reflectiveQuadTo(920f, 720f)
            quadToRelative(0f, 83f, -58.5f, 141.5f)
            reflectiveQuadTo(720f, 920f)
            close()
            moveTo(787f, 815f)
            lineTo(815f, 787f)
            lineTo(740f, 712f)
            verticalLineToRelative(-112f)
            horizontalLineToRelative(-40f)
            verticalLineToRelative(128f)
            lineToRelative(87f, 87f)
            close()
        }
    }.build()
}
