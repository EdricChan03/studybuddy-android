package com.edricchan.studybuddy.data.common.compose

import androidx.compose.ui.graphics.toArgb
import com.edricchan.studybuddy.data.common.Color
import androidx.compose.ui.graphics.Color as ComposeColor

/** Converts the receiver [Color] to its [Compose equivalent][ComposeColor]. */
fun Color.toComposeColor(): ComposeColor = ComposeColor(color = value)

/** Converts the receiver [ComposeColor] to its [common equivalent][Color]. */
fun ComposeColor.toCommonColor(): Color = Color(value = toArgb())
