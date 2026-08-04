package com.edricchan.studybuddy.utils.compose.graphics.preview

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider

// Colours randomly generated from https://rgbcolorpicker.com/random/light
val LightColors = listOf(
    Color(0xFFF9CEC2),
    Color(0xFFF8FFFA),
    Color(0xFFFBECED),
    Color(0xFF8DE847),
    Color(0xFFFFDAE3),
    Color(0xFFFFFEF1)
)

// Colours randomly generated from https://rgbcolorpicker.com/random/dark
val DarkColors = listOf(
    Color(0xFF262044),
    Color(0xFF083531),
    Color(0xFF1A0708),
    Color(0xFF000204),
    Color(0xFF2C2807),
    Color(0xFF000000)
)

val MixedColors = LightColors.take(3) + DarkColors.take(3)

private fun Color.toDisplayName(): String = "#%08X".format(toArgb())

/**
 * [androidx.compose.ui.tooling.preview.PreviewParameterProvider] which provides
 * [light][LightColors] and [dark][DarkColors] colours.
 */
class ColorPreviewParameterProvider : CollectionPreviewParameterProvider<Color>(
    collection = MixedColors
) {
    override fun getDisplayName(index: Int): String =
        "colour = ${MixedColors[index].toDisplayName()}"
}

/**
 * [androidx.compose.ui.tooling.preview.PreviewParameterProvider] which provides
 * [light][LightColors] colours.
 */
class LightColorPreviewParameterProvider : CollectionPreviewParameterProvider<Color>(
    collection = LightColors
) {
    override fun getDisplayName(index: Int): String =
        "colour = ${LightColors[index].toDisplayName()}"
}

/**
 * [androidx.compose.ui.tooling.preview.PreviewParameterProvider] which provides
 * [dark][DarkColors] colours.
 */
class DarkColorPreviewParameterProvider : CollectionPreviewParameterProvider<Color>(
    collection = DarkColors
) {
    override fun getDisplayName(index: Int): String =
        "colour = ${DarkColors[index].toDisplayName()}"
}
