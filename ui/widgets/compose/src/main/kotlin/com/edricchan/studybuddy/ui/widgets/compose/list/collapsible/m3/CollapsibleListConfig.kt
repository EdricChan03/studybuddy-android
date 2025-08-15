package com.edricchan.studybuddy.ui.widgets.compose.list.collapsible.m3

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.ui.widgets.compose.list.collapsible.m3.CollapsibleListDefaults.headerColors
import com.edricchan.studybuddy.ui.widgets.compose.list.m3.ExpListItemColors
import com.edricchan.studybuddy.ui.widgets.compose.list.m3.ExpListItemDefaults

/**
 * State-list colours to be used for [CollapsibleListDefaults].
 * @property baseColors [SurfaceColors] to be used when the list is in its **collapsed** and
 * **enabled** state.
 * @property expandedColors [SurfaceColors] to be used when the list is in its **expanded** and
 * **enabled** state.
 * @property disabledColors [SurfaceColors] to be used when the list is in its **collapsed** and
 * **disabled** state.
 * @property disabledExpandedColors [SurfaceColors] to be used when the list is in its
 * **expanded** state and **disabled**.
 */
@Immutable
data class CollapsibleListHeaderColors(
    val baseColors: SurfaceColors,
    val expandedColors: SurfaceColors,
    val disabledColors: SurfaceColors,
    val disabledExpandedColors: SurfaceColors
) {
    /**
     * Desired colours to be used for the [CollapsibleListHeader].
     * @param containerColor Container colour for the header.
     * @param contentColor Content colour for the header.
     * @param iconColor Leading icon container colour for the header.
     * @param expandIndicatorContainerColor Expansion indicator container colour for the header.
     * @param expandIndicatorContentColor Expansion indicator content colour for the header.
     */
    @Immutable
    data class SurfaceColors(
        val containerColor: Color,
        val contentColor: Color,
        val iconColor: Color,
        val expandIndicatorContainerColor: Color,
        val expandIndicatorContentColor: Color
    )

    /**
     * State-list colours to be used for [CollapsibleListDefaults].
     * @param containerColor Container colour for the collapsed and enabled state.
     * @param contentColor Content colour (e.g., text) for the collapsed and enabled state.
     * @param iconColor Icon colour for the collapsed and enabled state.
     * @param indicatorContainerColor Container colour for the expansion indicator (e.g., arrow)
     * in the collapsed and enabled state.
     * @param indicatorContentColor Content colour for the expansion indicator (e.g., arrow icon)
     * in the collapsed and enabled state.
     *
     * @param expandedContainerColor Container colour for the expanded and enabled state.
     * @param expandedContentColor Content colour for the expanded and enabled state.
     * @param expandedIconColor Icon colour for the expanded and enabled state.
     * @param expandedIndicatorContainerColor Container colour for the expansion indicator in the
     * expanded and enabled state.
     * @param expandedIndicatorContentColor Content colour for the expansion indicator in the
     * expanded and enabled state.
     *
     * @param disabledContainerColor Container colour for the collapsed and disabled state.
     * @param disabledContentColor Content colour for the collapsed and disabled state.
     * @param disabledIconColor Icon colour for the collapsed and disabled state.
     * @param disabledIndicatorContainerColor Container colour for the expansion indicator in the
     * collapsed and disabled state.
     * @param disabledIndicatorContentColor Content colour for the expansion indicator in the
     * collapsed and disabled state.
     *
     * @param disabledExpandedContainerColor Container colour for the expanded and disabled state.
     * @param disabledExpandedContentColor Content colour for the expanded and disabled state.
     * @param disabledExpandedIconColor Icon colour for the expanded and disabled state.
     * @param disabledExpandedIndicatorContainerColor Container colour for the expansion indicator
     * in the expanded and disabled state.
     * @param disabledExpandedIndicatorContentColor Content colour for the expansion indicator in
     * the expanded and disabled state.
     */
    constructor(
        containerColor: Color,
        contentColor: Color,
        iconColor: Color,
        indicatorContainerColor: Color,
        indicatorContentColor: Color,
        expandedContainerColor: Color,
        expandedContentColor: Color,
        expandedIconColor: Color,
        expandedIndicatorContainerColor: Color,
        expandedIndicatorContentColor: Color,
        disabledContainerColor: Color,
        disabledContentColor: Color,
        disabledIconColor: Color,
        disabledIndicatorContainerColor: Color,
        disabledIndicatorContentColor: Color,
        disabledExpandedContainerColor: Color,
        disabledExpandedContentColor: Color,
        disabledExpandedIconColor: Color,
        disabledExpandedIndicatorContainerColor: Color,
        disabledExpandedIndicatorContentColor: Color,
    ) : this(
        baseColors = SurfaceColors(
            containerColor = containerColor,
            contentColor = contentColor,
            iconColor = iconColor,
            expandIndicatorContainerColor = indicatorContainerColor,
            expandIndicatorContentColor = indicatorContentColor
        ),
        expandedColors = SurfaceColors(
            containerColor = expandedContainerColor,
            contentColor = expandedContentColor,
            iconColor = expandedIconColor,
            expandIndicatorContainerColor = expandedIndicatorContainerColor,
            expandIndicatorContentColor = expandedIndicatorContentColor
        ),
        disabledColors = SurfaceColors(
            containerColor = disabledContainerColor,
            contentColor = disabledContentColor,
            iconColor = disabledIconColor,
            expandIndicatorContainerColor = disabledIndicatorContainerColor,
            expandIndicatorContentColor = disabledIndicatorContentColor
        ),
        disabledExpandedColors = SurfaceColors(
            containerColor = disabledExpandedContainerColor,
            contentColor = disabledExpandedContentColor,
            iconColor = disabledExpandedIconColor,
            expandIndicatorContainerColor = disabledExpandedIndicatorContentColor,
            expandIndicatorContentColor = disabledExpandedIndicatorContentColor
        ),
    )

    internal fun getColorsPair(isExpanded: Boolean): Pair<SurfaceColors, SurfaceColors> =
        if (isExpanded) expandedColors to disabledExpandedColors
        else baseColors to disabledColors

    /** Gets the desired [SurfaceColors] based upon the [enabled] and [isExpanded] states. */
    fun getColors(enabled: Boolean, isExpanded: Boolean): SurfaceColors = when {
        enabled && isExpanded -> expandedColors
        !enabled && !isExpanded -> disabledColors
        !enabled && isExpanded -> disabledExpandedColors
        else -> baseColors
    }

    @Composable
    fun containerColor(enabled: Boolean, isExpanded: Boolean): State<Color> = animateColorAsState(
        label = "Collapsible list header container colour",
        targetValue = getColors(enabled = enabled, isExpanded = isExpanded).containerColor
    )

    @Composable
    fun contentColor(enabled: Boolean, isExpanded: Boolean): State<Color> = animateColorAsState(
        label = "Collapsible list header content colour",
        targetValue = getColors(enabled = enabled, isExpanded = isExpanded).contentColor
    )

    @Composable
    fun expandIndicatorContainerColor(enabled: Boolean, isExpanded: Boolean): State<Color> =
        animateColorAsState(
            label = "Collapsible list header expansion indicator container colour",
            targetValue = getColors(
                enabled = enabled,
                isExpanded = isExpanded
            ).expandIndicatorContainerColor
        )

    @Composable
    fun expandIndicatorContentColor(enabled: Boolean, isExpanded: Boolean): State<Color> =
        animateColorAsState(
            label = "Collapsible list header expansion indicator content colour",
            targetValue = getColors(
                enabled = enabled,
                isExpanded = isExpanded
            ).expandIndicatorContentColor
        )

    @Composable
    internal fun asExpListItemColors(isExpanded: Boolean): ExpListItemColors {
        val (enabledColors, disabledColors) = getColorsPair(isExpanded)
        return ExpListItemColors(
            containerColor = enabledColors.containerColor,
            contentColor = enabledColors.contentColor,
            iconColor = enabledColors.iconColor,
            disabledContainerColor = disabledColors.containerColor,
            disabledContentColor = disabledColors.contentColor,
            disabledIconColor = disabledColors.iconColor
        )
    }
}

private const val DisabledAlpha = 0.38f

private val Color.disabled: Color get() = copy(alpha = DisabledAlpha)


@Stable
object CollapsibleListDefaults {
    /**
     * Creates and returns a [CollapsibleListHeaderColors] object with the desired colours.
     * @param containerColor Container colour for the collapsed and enabled state.
     * @param contentColor Content colour (e.g., text) for the collapsed and enabled state.
     * @param iconColor Icon colour for the collapsed and enabled state.
     * @param indicatorContainerColor Container colour for the expansion indicator (e.g., arrow)
     * in the collapsed and enabled state.
     * @param indicatorContentColor Content colour for the expansion indicator (e.g., arrow icon)
     * in the collapsed and enabled state.
     *
     * @param expandedContainerColor Container colour for the expanded and enabled state.
     * @param expandedContentColor Content colour for the expanded and enabled state.
     * @param expandedIconColor Icon colour for the expanded and enabled state.
     * @param expandedIndicatorContainerColor Container colour for the expansion indicator in the
     * expanded and enabled state.
     * @param expandedIndicatorContentColor Content colour for the expansion indicator in the
     * expanded and enabled state.
     *
     * @param disabledContainerColor Container colour for the collapsed and disabled state.
     * @param disabledContentColor Content colour for the collapsed and disabled state.
     * @param disabledIconColor Icon colour for the collapsed and disabled state.
     * @param disabledIndicatorContainerColor Container colour for the expansion indicator in the
     * collapsed and disabled state.
     * @param disabledIndicatorContentColor Content colour for the expansion indicator in the
     * collapsed and disabled state.
     *
     * @param disabledExpandedContainerColor Container colour for the expanded and disabled state.
     * @param disabledExpandedContentColor Content colour for the expanded and disabled state.
     * @param disabledExpandedIconColor Icon colour for the expanded and disabled state.
     * @param disabledExpandedIndicatorContainerColor Container colour for the expansion indicator
     * in the expanded and disabled state.
     * @param disabledExpandedIndicatorContentColor Content colour for the expansion indicator in
     * the expanded and disabled state.
     */
    @Composable
    fun headerColors(
        containerColor: Color,
        contentColor: Color = contentColorFor(containerColor),
        iconColor: Color,
        indicatorContainerColor: Color,
        indicatorContentColor: Color = contentColorFor(indicatorContainerColor),
        expandedContainerColor: Color,
        expandedContentColor: Color = contentColorFor(expandedContainerColor),
        expandedIconColor: Color,
        expandedIndicatorContainerColor: Color,
        expandedIndicatorContentColor: Color = contentColorFor(expandedIndicatorContainerColor),
        disabledContainerColor: Color = containerColor.disabled,
        disabledContentColor: Color = contentColor.disabled,
        disabledIconColor: Color = iconColor.disabled,
        disabledIndicatorContainerColor: Color = indicatorContainerColor.disabled,
        disabledIndicatorContentColor: Color = indicatorContentColor.disabled,
        disabledExpandedContainerColor: Color = expandedContainerColor.disabled,
        disabledExpandedContentColor: Color = expandedContentColor.disabled,
        disabledExpandedIconColor: Color = expandedIconColor.disabled,
        disabledExpandedIndicatorContainerColor: Color = expandedIndicatorContainerColor.disabled,
        disabledExpandedIndicatorContentColor: Color = expandedIndicatorContentColor.disabled
    ): CollapsibleListHeaderColors = CollapsibleListHeaderColors(
        containerColor = containerColor,
        contentColor = contentColor,
        iconColor = iconColor,
        indicatorContainerColor = indicatorContainerColor,
        indicatorContentColor = indicatorContentColor,
        expandedContainerColor = expandedContainerColor,
        expandedContentColor = expandedContentColor,
        expandedIconColor = expandedIconColor,
        expandedIndicatorContainerColor = expandedIndicatorContainerColor,
        expandedIndicatorContentColor = expandedIndicatorContentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
        disabledIconColor = disabledIconColor,
        disabledIndicatorContainerColor = disabledIndicatorContainerColor,
        disabledIndicatorContentColor = disabledIndicatorContentColor,
        disabledExpandedContainerColor = disabledExpandedContainerColor,
        disabledExpandedContentColor = disabledExpandedContentColor,
        disabledExpandedIconColor = disabledExpandedIconColor,
        disabledExpandedIndicatorContainerColor = disabledExpandedIndicatorContainerColor,
        disabledExpandedIndicatorContentColor = disabledExpandedIndicatorContentColor
    )

    /**
     * Opinionated version of [headerColors] which uses the primary colour palette.
     *
     * Colour mapping:
     *
     * Token | Value
     * ---|---
     * `containerColor` | [ExpListItemDefaults.itemContainerColor]
     * `iconColor` | [ExpListItemDefaults.itemIconColor]
     * `indicatorContainerColor` | [androidx.compose.material3.ColorScheme.primary]
     * `expandedContainerColor` | [androidx.compose.material3.ColorScheme.primaryContainer]
     * `expandedIndicatorContainerColor` | [androidx.compose.material3.ColorScheme.surfaceContainerHigh]
     * `expandedIconColor` | [androidx.compose.material3.ColorScheme.onPrimaryContainer]
     */
    @Composable
    fun primaryHeaderColors(): CollapsibleListHeaderColors = headerColors(
        containerColor = ExpListItemDefaults.itemContainerColor,
        iconColor = ExpListItemDefaults.itemIconColor,
        indicatorContainerColor = MaterialTheme.colorScheme.primary,
        expandedContainerColor = MaterialTheme.colorScheme.primary,
        expandedIndicatorContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        expandedIconColor = MaterialTheme.colorScheme.onPrimary
    )

    /**
     * Opinionated version of [headerColors] which uses the tertiary colour palette.
     *
     * Colour mapping:
     *
     * Token | Value
     * ---|---
     * `containerColor` | [ExpListItemDefaults.itemContainerColor]
     * `iconColor` | [ExpListItemDefaults.itemIconColor]
     * `indicatorContainerColor` | [androidx.compose.material3.ColorScheme.tertiary]
     * `expandedContainerColor` | [androidx.compose.material3.ColorScheme.tertiaryContainer]
     * `expandedIndicatorContainerColor` | [androidx.compose.material3.ColorScheme.surfaceContainerHigh]
     * `expandedIconColor` | [androidx.compose.material3.ColorScheme.onTertiaryContainer]
     */
    @Composable
    fun tertiaryHeaderColors(): CollapsibleListHeaderColors = headerColors(
        containerColor = ExpListItemDefaults.itemContainerColor,
        iconColor = ExpListItemDefaults.itemIconColor,
        indicatorContainerColor = MaterialTheme.colorScheme.tertiary,
        expandedContainerColor = MaterialTheme.colorScheme.tertiary,
        expandedIndicatorContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        expandedIconColor = MaterialTheme.colorScheme.onTertiary
    )

    /**
     * Expansion indicator to be shown for the [CollapsibleListHeader]'s trailing content.
     * @param modifier [Modifier] to be used for the [Surface].
     * @param shape Desired [Shape] to clip the contents to.
     * @param color Desired container colour for the [Surface].
     * @param contentColor Desired content colour for the [Surface].
     * @param rotationDeg Rotation (in degrees) to rotate the expand arrow icon by. (See the
     * [rotate] modifier for more info)
     */
    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    fun ExpandIcon(
        modifier: Modifier = Modifier,
        shape: Shape = MaterialShapes.Clover8Leaf.toShape(),
        color: Color,
        contentColor: Color = contentColorFor(color),
        rotationDeg: Float
    ) {
        Surface(
            modifier = modifier.rotate(rotationDeg),
            shape = shape,
            color = color,
            contentColor = contentColor
        ) {
            Icon(
                modifier = Modifier.padding(4.dp),
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = null
            )
        }
    }

    /**
     * Gets the desired shape for the [CollapsibleListHeader].
     * @param isExpanded Whether the list's children should be shown.
     */
    @Composable
    fun headerShape(isExpanded: Boolean): Shape =
        if (isExpanded) ExpListItemDefaults.firstItemShape else ExpListItemDefaults.baseShape
}
