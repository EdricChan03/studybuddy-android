package com.edricchan.studybuddy.ui.theming.compose.spacing

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** A spacing value for [SpacingTokens]. */
@JvmInline
value class Spacing(val value: Dp) : Comparable<Spacing> {
    override fun compareTo(other: Spacing): Int = value.compareTo(other.value)

    companion object {
        /** Special value for no spacing. */
        val None: Spacing = Spacing(0.dp)

        val VectorConverter: TwoWayConverter<Spacing, AnimationVector1D> = TwoWayConverter(
            convertToVector = { AnimationVector1D(it.value.value) },
            convertFromVector = { Spacing(it.value.dp) }
        )
    }
}

/**
 * Fire-and-forget animation for any [Spacing] value.
 *
 * [animateSpacingAsState] returns a [State] object. The value of the state object will continuously
 * be updated by the animation until the animation finishes.
 *
 * Note, [animateSpacingAsState] cannot be canceled/stopped without removing this composable function
 * from the tree. See [Animatable] for cancelable animations.
 *
 * @see animateValueAsState
 * @see androidx.compose.animation.core.animateDpAsState
 */
@Composable
fun animateSpacingAsState(
    targetValue: Spacing,
    animationSpec: AnimationSpec<Spacing> = spring(visibilityThreshold = Spacing(Dp.VisibilityThreshold)),
    label: String = "SpacingAnimation",
    finishedListener: ((Spacing) -> Unit)? = null
): State<Spacing> = animateValueAsState(
    targetValue = targetValue,
    typeConverter = Spacing.VectorConverter,
    animationSpec = animationSpec,
    label = label,
    finishedListener = finishedListener
)

/** Creates a padding of [all] spacing along all 4 edges. */
@Stable
fun PaddingValues(all: Spacing): PaddingValues = PaddingValues(all = all.value)

/**
 * Creates a padding of [horizontal] spacing along the left and right edges,
 * and of [vertical] spacing along the top and bottom edges.
 */
@Stable
fun PaddingValues(
    horizontal: Spacing,
    vertical: Spacing
): PaddingValues = PaddingValues(horizontal = horizontal.value, vertical = vertical.value)

/**
 * Creates a padding to be applied along the edges inside a box.
 *
 * - In LTR contexts [start] will be applied along the left edge and
 * [end] will be applied along the right edge.
 * - In RTL contexts, [start] will correspond to the right edge and [end] to the left.
 */
@Stable
fun PaddingValues(
    start: Spacing = Spacing.None,
    top: Spacing = Spacing.None,
    end: Spacing = Spacing.None,
    bottom: Spacing = Spacing.None
): PaddingValues = PaddingValues(
    start = start.value,
    top = top.value,
    end = end.value,
    bottom = bottom.value
)

/**
 * Applies the desired [spacings][all] along the edges of the content.
 * @see padding
 */
fun Modifier.spacing(all: Spacing): Modifier = padding(all.value)

/**
 * Applies the desired [horizontal][horizontal] and [vertical][vertical]
 * spacings along the edges of the content.
 * @see padding
 */
fun Modifier.spacing(
    horizontal: Spacing = Spacing.None,
    vertical: Spacing = Spacing.None
): Modifier = padding(
    horizontal = horizontal.value,
    vertical = vertical.value
)

/**
 * Applies the desired [start], [top], [end] and [bottom]
 * spacings along each edge of the content.
 * @see padding
 */
fun Modifier.spacing(
    start: Spacing = Spacing.None,
    top: Spacing = Spacing.None,
    end: Spacing = Spacing.None,
    bottom: Spacing = Spacing.None
): Modifier = padding(
    start = start.value,
    top = top.value,
    end = end.value,
    bottom = bottom.value
)

/** Spacing values as per the [Material spec](https://m3.material.io/styles/spacing/tokens). */
@Immutable
class SpacingTokens(private val base: Dp = BaseSpacing) {
    companion object {
        /** Base measurement that all spacing tokens get computed from. */
        val BaseSpacing: Dp = 8.dp
    }

    /** Use zero spacing. */
    val space0: Spacing = Spacing(0.dp)
    val space25: Spacing by lazy { computeSpacing(0.25f) }
    val space50: Spacing by lazy { computeSpacing(0.50f) }
    val space75: Spacing by lazy { computeSpacing(0.75f) }

    /** The base spacing to use. Equivalent to using [base] directly. */
    val space100: Spacing by lazy { computeSpacing(1) }
    val space125: Spacing by lazy { computeSpacing(1.25f) }
    val space150: Spacing by lazy { computeSpacing(1.50f) }
    val space175: Spacing by lazy { computeSpacing(1.75f) }
    val space200: Spacing by lazy { computeSpacing(2) }
    val space250: Spacing by lazy { computeSpacing(2.5f) }
    val space300: Spacing by lazy { computeSpacing(3) }
    val space400: Spacing by lazy { computeSpacing(4) }
    val space450: Spacing by lazy { computeSpacing(4.5f) }
    val space500: Spacing by lazy { computeSpacing(5) }
    val space600: Spacing by lazy { computeSpacing(6) }
    val space700: Spacing by lazy { computeSpacing(7) }
    val space800: Spacing by lazy { computeSpacing(8) }
    val space900: Spacing by lazy { computeSpacing(9) }

    /**
     * Computes the desired spacing value to use based on the [multi].
     *
     * This is equivalent to `base * multi`.
     */
    fun computeSpacing(multi: Int): Spacing = Spacing(base * multi)

    /**
     * Computes the desired spacing value to use based on the [multi].
     *
     * This is equivalent to `base * multi`.
     */
    fun computeSpacing(multi: Float): Spacing = Spacing(base * multi)

    fun copy(base: Dp): SpacingTokens = SpacingTokens(base = base)
    fun copy(baseSpacing: Spacing): SpacingTokens = SpacingTokens(base = baseSpacing.value)
}

val LocalThemeSpacing: ProvidableCompositionLocal<SpacingTokens> =
    staticCompositionLocalOf { SpacingTokens() }

/** [SpacingTokens] for the receiver [MaterialTheme]. */
@get:Composable
val MaterialTheme.spacing: SpacingTokens get() = LocalThemeSpacing.current
