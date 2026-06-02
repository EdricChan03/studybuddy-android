package com.edricchan.studybuddy.utils.compose.graphics

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

/** Returns whether the receiver [Color] is a dark colour. */
val Color.isDark: Boolean get() = luminance() <= 0.5f

/** Returns a colour that contrasts against the receiver [Color]. */
val Color.contrastingColor: Color get() = if (isDark) Color.White else Color.Black
