package com.edricchan.studybuddy.ui.widgets.compose.list.m3

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme

internal val ListItemHorizontalPadding = 16.dp
internal val ListItemVerticalPadding = 16.dp

@Composable
internal fun ExpListItemSurface(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    shape: Shape,
    colors: ExpListItemColors,
    content: @Composable () -> Unit
) {
    val containerColor by colors.containerColor(enabled = enabled)
    val contentColor by colors.contentColor(enabled = enabled)

    Surface(
        modifier = modifier,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        content = content
    )
}

@Composable
internal fun ExpListItemSurface(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
    shape: Shape,
    colors: ExpListItemColors,
    content: @Composable () -> Unit
) {
    val containerColor by colors.containerColor(enabled = enabled)
    val contentColor by colors.contentColor(enabled = enabled)

    Surface(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        content = content
    )
}

@Composable
internal fun ExpListItemSurface(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    shape: Shape,
    colors: ExpListItemColors,
    content: @Composable () -> Unit
) {
    val containerColor by colors.containerColor(enabled = enabled)
    val contentColor by colors.contentColor(enabled = enabled)

    Surface(
        modifier = modifier,
        enabled = enabled,
        checked = checked,
        onCheckedChange = onCheckedChange,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        content = content
    )
}

@Composable
internal fun ExpListItemSurface(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    selected: Boolean,
    onClick: () -> Unit,
    shape: Shape,
    colors: ExpListItemColors,
    content: @Composable () -> Unit
) {
    val containerColor by colors.containerColor(enabled = enabled)
    val contentColor by colors.contentColor(enabled = enabled)

    Surface(
        modifier = modifier,
        enabled = enabled,
        selected = selected,
        onClick = onClick,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        content = content
    )
}

@Composable
private fun ExpListItemContent(
    modifier: Modifier = Modifier,
    leadingContent: @Composable (() -> Unit)?,
    overlineContent: @Composable (() -> Unit)?,
    headlineContent: @Composable () -> Unit,
    supportingContent: @Composable (() -> Unit)?,
    trailingContent: @Composable (() -> Unit)?,
    enabled: Boolean,
    colors: ExpListItemColors
) {
    val iconColor by colors.iconColor(enabled = enabled)

    Row(
        modifier = modifier.padding(horizontal = ListItemHorizontalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingContent?.let {
            Box(
                modifier = Modifier.padding(end = ListItemHorizontalPadding),
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides iconColor,
                    content = it
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = ListItemVerticalPadding),
        ) {
            ProvideTextStyle(
                value = MaterialTheme.typography.labelSmall,
            ) {
                overlineContent?.invoke()
            }
            ProvideTextStyle(
                value = MaterialTheme.typography.titleMedium,
                content = headlineContent
            )
            ProvideTextStyle(
                value = MaterialTheme.typography.bodyMedium
            ) {
                supportingContent?.invoke()
            }
        }
        trailingContent?.let {
            Box(
                modifier = Modifier.padding(start = ListItemHorizontalPadding),
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides iconColor,
                    content = it
                )
            }
        }
    }
}

/**
 * Expressive variant of the Material3 [androidx.compose.material3.ListItem], which
 * adds a default [Shape].
 * @param modifier [Modifier] for the layout.
 * @param enabled Whether the list item's content is visibly grayed out.
 * @param leadingContent Content to be shown on the start/left-hand side of the layout.
 * @param overlineContent Content to be shown above the [headlineContent].
 * @param headlineContent Content to be shown above the [supportingContent].
 * This could be used to provide a title.
 * @param supportingContent Additional content to be shown. This could be used to provide
 * a description.
 * @param trailingContent Content to be shown on the end/right-hand side of the layout.
 * @param shape Desired [Shape] for the layout. The content is clipped to this shape.
 *
 * To get the recommended shape for this layout, use [ExpListItemDefaults.itemShape] if the
 * list item is in a list of items or [ExpListItemDefaults.baseShape] if this item is
 * displayed on its own.
 * @param colors Desired [ExpListItemColors] to be used for this layout. Use
 * [ExpListItemDefaults.colors] to retrieve the default colours.
 */
@Composable
fun ExpListItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingContent: (@Composable () -> Unit)? = null,
    overlineContent: @Composable (() -> Unit)? = null,
    headlineContent: @Composable () -> Unit,
    supportingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    shape: Shape,
    colors: ExpListItemColors = ExpListItemDefaults.colors()
) {
    ExpListItemSurface(
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors
    ) {
        ExpListItemContent(
            leadingContent = leadingContent,
            overlineContent = overlineContent,
            headlineContent = headlineContent,
            supportingContent = supportingContent,
            trailingContent = trailingContent,
            enabled = enabled,
            colors = colors
        )
    }
}

/**
 * Expressive variant of the Material3 [androidx.compose.material3.ListItem], which
 * adds a default [Shape].
 * @param modifier [Modifier] for the layout.
 * @param enabled Whether interactions should be enabled.
 * @param onClick Invoked when the item is clicked.
 * @param leadingContent Content to be shown on the start/left-hand side of the layout.
 * @param overlineContent Content to be shown above the [headlineContent].
 * @param headlineContent Content to be shown above the [supportingContent].
 * This could be used to provide a title.
 * @param supportingContent Additional content to be shown. This could be used to provide
 * a description.
 * @param trailingContent Content to be shown on the end/right-hand side of the layout.
 * @param shape Desired [Shape] for the layout. The content is clipped to this shape.
 *
 * To get the recommended shape for this layout, use [ExpListItemDefaults.itemShape] if the
 * list item is in a list of items or [ExpListItemDefaults.baseShape] if this item is
 * displayed on its own.
 * @param colors Desired [ExpListItemColors] to be used for this layout. Use
 * [ExpListItemDefaults.colors] to retrieve the default colours.
 */
@Composable
fun ExpListItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    leadingContent: (@Composable () -> Unit)? = null,
    overlineContent: @Composable (() -> Unit)? = null,
    headlineContent: @Composable () -> Unit,
    supportingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    shape: Shape,
    colors: ExpListItemColors = ExpListItemDefaults.colors()
) {
    ExpListItemSurface(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        shape = shape,
        colors = colors
    ) {
        ExpListItemContent(
            leadingContent = leadingContent,
            overlineContent = overlineContent,
            headlineContent = headlineContent,
            supportingContent = supportingContent,
            trailingContent = trailingContent,
            enabled = enabled,
            colors = colors
        )
    }
}

/**
 * Expressive variant of the Material3 [androidx.compose.material3.ListItem], which
 * adds a default [Shape].
 * @param modifier [Modifier] for the layout.
 * @param enabled Whether interactions should be enabled.
 * @param checked Whether the item is checked. It is up to the consumer to specify
 * custom styling if desired when the item is actually checked.
 * @param onCheckedChange Invoked when the [checked] state has been modified. Use the
 * new value to update the [checked] state.
 * @param leadingContent Content to be shown on the start/left-hand side of the layout.
 * @param overlineContent Content to be shown above the [headlineContent].
 * @param headlineContent Content to be shown above the [supportingContent].
 * This could be used to provide a title.
 * @param supportingContent Additional content to be shown. This could be used to provide
 * a description.
 * @param trailingContent Content to be shown on the end/right-hand side of the layout.
 * @param shape Desired [Shape] for the layout. The content is clipped to this shape.
 *
 * To get the recommended shape for this layout, use [ExpListItemDefaults.itemShape] if the
 * list item is in a list of items or [ExpListItemDefaults.baseShape] if this item is
 * displayed on its own.
 * @param colors Desired [ExpListItemColors] to be used for this layout. Use
 * [ExpListItemDefaults.colors] to retrieve the default colours.
 */
@Composable
fun ExpListItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    leadingContent: (@Composable () -> Unit)? = null,
    overlineContent: @Composable (() -> Unit)? = null,
    headlineContent: @Composable () -> Unit,
    supportingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    shape: Shape,
    colors: ExpListItemColors = ExpListItemDefaults.colors()
) {
    ExpListItemSurface(
        modifier = modifier,
        enabled = enabled,
        checked = checked,
        onCheckedChange = onCheckedChange,
        shape = shape,
        colors = colors
    ) {
        ExpListItemContent(
            leadingContent = leadingContent,
            overlineContent = overlineContent,
            headlineContent = headlineContent,
            supportingContent = supportingContent,
            trailingContent = trailingContent,
            enabled = enabled,
            colors = colors
        )
    }
}

/**
 * Expressive variant of the Material3 [androidx.compose.material3.ListItem], which
 * adds a default [Shape].
 * @param modifier [Modifier] for the layout.
 * @param enabled Whether interactions should be enabled.
 * @param selected Whether the item is selected. It is up to the consumer to specify
 * custom styling if desired when the item is actually selected.
 * @param onClick Invoked when the item is clicked.
 * @param leadingContent Content to be shown on the start/left-hand side of the layout.
 * @param overlineContent Content to be shown above the [headlineContent].
 * @param headlineContent Content to be shown above the [supportingContent].
 * This could be used to provide a title.
 * @param supportingContent Additional content to be shown. This could be used to provide
 * a description.
 * @param trailingContent Content to be shown on the end/right-hand side of the layout.
 * @param shape Desired [Shape] for the layout. The content is clipped to this shape.
 *
 * To get the recommended shape for this layout, use [ExpListItemDefaults.itemShape] if the
 * list item is in a list of items or [ExpListItemDefaults.baseShape] if this item is
 * displayed on its own.
 * @param colors Desired [ExpListItemColors] to be used for this layout. Use
 * [ExpListItemDefaults.colors] to retrieve the default colours.
 */
@Composable
fun ExpListItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean,
    onClick: () -> Unit,
    leadingContent: (@Composable () -> Unit)? = null,
    overlineContent: @Composable (() -> Unit)? = null,
    headlineContent: @Composable () -> Unit,
    supportingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    shape: Shape,
    colors: ExpListItemColors = ExpListItemDefaults.colors()
) {
    ExpListItemSurface(
        modifier = modifier,
        enabled = enabled,
        selected = selected,
        onClick = onClick,
        shape = shape,
        colors = colors
    ) {
        ExpListItemContent(
            leadingContent = leadingContent,
            overlineContent = overlineContent,
            headlineContent = headlineContent,
            supportingContent = supportingContent,
            trailingContent = trailingContent,
            enabled = enabled,
            colors = colors
        )
    }
}

/**
 * Colours to be used for a [ExpListItem].
 * @property containerColor Container colour to use when enabled.
 * @property contentColor Content colour to use when enabled.
 * @property iconColor Icon content colour to use when enabled.
 * @property disabledContainerColor Container colour to use when disabled.
 * @property disabledContentColor Content colour to use when disabled.
 * @property disabledIconColor Icon content colour to use when disabled.
 */
@Immutable
data class ExpListItemColors(
    val containerColor: Color,
    val contentColor: Color,
    val iconColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val disabledIconColor: Color
) {
    @Composable
    fun containerColor(enabled: Boolean): State<Color> =
        animateColorAsState(
            label = "Expressive list item container colour",
            targetValue = if (enabled) containerColor else disabledContainerColor
        )

    @Composable
    fun contentColor(enabled: Boolean): State<Color> =
        animateColorAsState(
            label = "Expressive list item content colour",
            targetValue = if (enabled) contentColor else disabledContentColor
        )

    @Composable
    fun iconColor(enabled: Boolean): State<Color> =
        animateColorAsState(
            label = "Expressive list item icon colour",
            targetValue = if (enabled) iconColor else disabledIconColor
        )
}

@Stable
object ExpListItemDefaults {
    /** Desired [Shape] to use when a [ExpListItem] is used on its own. */
    @get:Composable
    val baseShape: CornerBasedShape get() = MaterialTheme.shapes.large

    val categoryCornerDp: Dp = 2.dp

    /** Desired spacing to be used between each [ExpListItem] when used in a group. */
    val groupedItemsSpacing: Dp = 2.dp

    private val categoryCornerSize = CornerSize(categoryCornerDp)

    /** Desired [Shape] to use for the first [ExpListItem] in a list. */
    @get:Composable
    val firstItemShape: CornerBasedShape
        get() = baseShape.copy(
            bottomStart = categoryCornerSize,
            bottomEnd = categoryCornerSize
        )

    /** Desired [Shape] to use for the last [ExpListItem] in a list. */
    @get:Composable
    val lastItemShape: CornerBasedShape
        get() = baseShape.copy(
            topStart = categoryCornerSize,
            topEnd = categoryCornerSize
        )

    /** Desired [Shape] to use for the middle [ExpListItem]s in a list. */
    val middleItemShape: CornerBasedShape = RoundedCornerShape(categoryCornerSize)

    /**
     * Gets the desired [Shape] for an [ExpListItem].
     * @param index Current index of this list item in a lazy list.
     * @param count The number of list items to be shown in the surrounding lazy list.
     */
    @Composable
    fun itemShape(index: Int, count: Int): CornerBasedShape {
        if (count == 1) {
            return baseShape
        }

        return when (index) {
            0 -> firstItemShape
            count - 1 -> lastItemShape
            else -> middleItemShape
        }
    }

    /** The default [ExpListItemColors.containerColor] value if not specified. */
    @get:Composable
    val itemContainerColor: Color get() = MaterialTheme.colorScheme.surfaceContainerHigh

    /** The default [ExpListItemColors.iconColor] value if not specified. */
    @get:Composable
    val itemIconColor: Color get() = MaterialTheme.colorScheme.onSurfaceVariant

    /**
     * Gets the [ExpListItemColors] to be used for a [ExpListItem].
     * @param containerColor Container colour to use when enabled.
     * @param contentColor Content colour to use when enabled.
     * @param iconColor Icon content colour to use when enabled.
     * @param disabledContainerColor Container colour to use when disabled.
     * @param disabledContentColor Content colour to use when disabled.
     * @param disabledIconColor Icon content colour to use when disabled.
     */
    @Composable
    fun colors(
        containerColor: Color = itemContainerColor,
        contentColor: Color = contentColorFor(containerColor),
        iconColor: Color = itemIconColor,
        disabledContainerColor: Color = containerColor.copy(alpha = 0.38f),
        disabledContentColor: Color = contentColor.copy(alpha = 0.38f),
        disabledIconColor: Color = iconColor.copy(alpha = 0.38f)
    ): ExpListItemColors = ExpListItemColors(
        containerColor = containerColor,
        contentColor = contentColor,
        iconColor = iconColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
        disabledIconColor = disabledIconColor
    )
}


@Preview
@Composable
private fun ExpListItemPreview() {
    StudyBuddyTheme {
        ExpListItem(
            leadingContent = {
                Icon(Icons.Outlined.Build, contentDescription = null)
            },
            overlineContent = {
                Text(text = "Overline")
            },
            headlineContent = {
                Text(text = "Example")
            },
            supportingContent = {
                Text(text = "Content")
            },
            trailingContent = {
                Badge { Text(text = "100") }
            },
            shape = ExpListItemDefaults.baseShape
        )
    }
}

@Preview
@Composable
private fun ExpListItemLazyColumnPreview() {
    val count = remember { 100 }
    StudyBuddyTheme {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(ExpListItemDefaults.groupedItemsSpacing)
        ) {
            items(count) {
                ExpListItem(
                    leadingContent = {
                        Icon(Icons.Outlined.Build, contentDescription = null)
                    },
                    headlineContent = {
                        Text(text = "Example $it")
                    },
                    supportingContent = {
                        Text(text = "Content")
                    },
                    shape = ExpListItemDefaults.itemShape(it, count)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ExpListItemLoremIpsumPreview(@PreviewParameter(LoremIpsum::class) lorem: String) {
    StudyBuddyTheme {
        ExpListItem(
            leadingContent = {
                Icon(Icons.Outlined.Build, contentDescription = null)
            },
            headlineContent = {
                Text(text = lorem)
            },
            supportingContent = {
                Text(text = lorem)
            },
            trailingContent = {
                Badge { Text(text = "100") }
            },
            shape = ExpListItemDefaults.baseShape
        )
    }
}
