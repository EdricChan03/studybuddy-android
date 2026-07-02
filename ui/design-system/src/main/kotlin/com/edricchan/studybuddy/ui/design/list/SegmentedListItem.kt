package com.edricchan.studybuddy.ui.design.list

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.SegmentedListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SegmentedListItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    overlineContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    shapes: ListItemShapes,
    colors: ListItemColors = ListItemDefaults.segmentedColors(),
    content: @Composable () -> Unit,
) {
    SegmentedListItem(
        modifier = modifier,
        enabled = enabled,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        overlineContent = overlineContent,
        supportingContent = supportingContent,
        shapes = shapes,
        colors = colors,
        content = content,
    )
}

@Composable
fun SegmentedListItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    overlineContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    onLongClickLabel: String? = null,
    shapes: ListItemShapes,
    colors: ListItemColors = ListItemDefaults.segmentedColors(),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit,
) {
    SegmentedListItem(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        onLongClick = onLongClick,
        onLongClickLabel = onLongClickLabel,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        overlineContent = overlineContent,
        supportingContent = supportingContent,
        shapes = shapes,
        colors = colors,
        interactionSource = interactionSource,
        content = content,
    )
}

@Composable
fun SegmentedListItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean,
    onClick: () -> Unit,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    overlineContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    shapes: ListItemShapes,
    colors: ListItemColors = ListItemDefaults.segmentedColors(),
    onLongClick: (() -> Unit)? = null,
    onLongClickLabel: String? = null,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit,
) {
    SegmentedListItem(
        modifier = modifier,
        enabled = enabled,
        selected = selected,
        onClick = onClick,
        onLongClick = onLongClick,
        onLongClickLabel = onLongClickLabel,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        overlineContent = overlineContent,
        supportingContent = supportingContent,
        shapes = shapes,
        colors = colors,
        interactionSource = interactionSource,
        content = content,
    )
}

@Composable
fun SegmentedListItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    overlineContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    shapes: ListItemShapes,
    colors: ListItemColors = ListItemDefaults.segmentedColors(),
    onLongClick: (() -> Unit)? = null,
    onLongClickLabel: String? = null,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit,
) {
    SegmentedListItem(
        modifier = modifier,
        enabled = enabled,
        checked = checked,
        onCheckedChange = onCheckedChange,
        onLongClick = onLongClick,
        onLongClickLabel = onLongClickLabel,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        overlineContent = overlineContent,
        supportingContent = supportingContent,
        shapes = shapes,
        colors = colors,
        interactionSource = interactionSource,
        content = content,
    )
}
