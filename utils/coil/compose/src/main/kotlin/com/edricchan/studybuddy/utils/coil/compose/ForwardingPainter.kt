// From https://gist.github.com/colinrtwhite/c2966e0b8584b4cdf0a5b05786b20ae1
package com.edricchan.studybuddy.utils.coil.compose

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter

/**
 * Create and return a new [Painter] that wraps [painter] with its [alpha], [colorFilter], or [onDraw] overwritten.
 */
fun forwardingPainter(
    painter: Painter,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    onDraw: DrawScope.(ForwardingDrawInfo) -> Unit = DefaultOnDraw,
): Painter = ForwardingPainter(painter, alpha, colorFilter, onDraw)

/**
 * Variant of [forwardingPainter] which tints the [painter] with the given [tint] colour.
 * @see forwardingPainter
 */
@Composable
fun tintedPainter(
    painter: Painter,
    alpha: Float = DefaultAlpha,
    tint: Color = LocalContentColor.current
): Painter = forwardingPainter(painter, alpha, colorFilter = ColorFilter.tint(tint))

data class ForwardingDrawInfo(
    val painter: Painter,
    val alpha: Float,
    val colorFilter: ColorFilter?,
)

private class ForwardingPainter(
    private val painter: Painter,
    private var alpha: Float,
    private var colorFilter: ColorFilter?,
    private val onDraw: DrawScope.(ForwardingDrawInfo) -> Unit,
) : Painter() {

    private var info = newInfo()

    override val intrinsicSize get() = painter.intrinsicSize

    override fun applyAlpha(alpha: Float): Boolean {
        if (alpha != DefaultAlpha) {
            this.alpha = alpha
            this.info = newInfo()
        }
        return true
    }

    override fun applyColorFilter(colorFilter: ColorFilter?): Boolean {
        if (colorFilter != null) {
            this.colorFilter = colorFilter
            this.info = newInfo()
        }
        return true
    }

    override fun DrawScope.onDraw() = onDraw(info)

    private fun newInfo() = ForwardingDrawInfo(painter, alpha, colorFilter)
}

private val DefaultOnDraw: DrawScope.(ForwardingDrawInfo) -> Unit = { info ->
    with(info.painter) {
        draw(size, info.alpha, info.colorFilter)
    }
}
