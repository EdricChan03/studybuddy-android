package com.edricchan.studybuddy.ui.widgets.compose.list.collapsible.m3

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.edricchan.studybuddy.ui.widgets.compose.list.m3.ExpListItem
import com.edricchan.studybuddy.ui.widgets.compose.list.m3.ExpListItemDefaults

/**
 * List group that can be collapsed/expanded to reveal a list of grouped items.
 *
 * If desired, the [androidx.compose.foundation.selection.selectableGroup] semantic
 * [Modifier] should be applied to this list composable.
 *
 * The [animateContentSize] modifier is also applied to the surrounding [Column] to
 * animate the changing content size.
 * @param modifier [Modifier] for the parent [Column].
 * @param shape Desired [Shape] to [clip] the entire bounds of the list group to.
 * @param verticalArrangement Vertical arrangement for the layout's children.
 * @param horizontalAlignment Horizontal alignment for the layout's children.
 * @param isExpanded Whether the children items should be shown.
 * @param header Composable to be shown in its collapsed and expanded state. Ideally,
 * this composable should also toggle the [isExpanded] state when clicked. (See
 * [CollapsibleListHeader] for a default implementation)
 * @param content Desired content to be shown when [isExpanded] is `true`.
 * @sample CollapsibleListSample
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CollapsibleListColumn(
    modifier: Modifier = Modifier,
    shape: Shape = ExpListItemDefaults.baseShape,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(
        ExpListItemDefaults.groupedItemsSpacing
    ),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    isExpanded: Boolean,
    header: @Composable ColumnScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {

    val defaultSpatialSpec = MaterialTheme.motionScheme.defaultSpatialSpec<IntSize>()
    val defaultEffectsSpec = MaterialTheme.motionScheme.defaultEffectsSpec<Float>()

    Column(
        modifier = modifier
            .clip(shape)
            .animateContentSize(
                animationSpec = defaultSpatialSpec
            ),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        header()
        AnimatedVisibility(
            label = "Collapsible list expansion",
            visible = isExpanded,
            enter = fadeIn(animationSpec = defaultEffectsSpec) + expandVertically(
                animationSpec = defaultSpatialSpec
            ),
            exit = fadeOut(animationSpec = defaultEffectsSpec) + shrinkVertically(
                animationSpec = defaultSpatialSpec
            ),
        ) {
            Column(
                verticalArrangement = verticalArrangement,
                horizontalAlignment = horizontalAlignment
            ) {
                content()
            }
        }
    }
}

/**
 * Opinionated header composable for [CollapsibleListColumn].
 * @param modifier [Modifier] for the header.
 * @param enabled Whether clicking on this header should trigger [onExpansionChange].
 * @param isExpanded Whether the list has been expanded.
 * @param onExpansionChange Triggered when the header is clicked on. The new expansion state
 * is passed as the lambda argument.
 * @param leadingContent Content to be shown on the start/left-hand side of the layout.
 * @param overlineContent Content to be shown above the [headlineContent].
 * @param headlineContent Content to be shown above the [supportingContent].
 * This could be used to provide a title.
 * @param supportingContent Additional content to be shown. This could be used to provide
 * a description.
 * @param trailingContent Content to be shown on the end/right-hand side of the layout.
 * [CollapsibleListDefaults.ExpandIcon] is used if this parameter is not specified.
 * @param shape The shape of this header.
 * @param colors Desired [CollapsibleListHeaderColors] for this composable.
 * [CollapsibleListDefaults.headerColors] can be used to create these colours, or the opinionated
 * [CollapsibleListDefaults.primaryHeaderColors] and [CollapsibleListDefaults.tertiaryHeaderColors]
 * methods can be used for a primary or tertiary colour palette when the header is in
 * its expanded state.
 */
@Composable
fun CollapsibleListHeader(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isExpanded: Boolean,
    onExpansionChange: (Boolean) -> Unit,
    leadingContent: @Composable (() -> Unit)? = null,
    overlineContent: @Composable (() -> Unit)? = null,
    headlineContent: @Composable () -> Unit,
    supportingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)?,
    shape: Shape = CollapsibleListDefaults.headerShape(isExpanded = isExpanded),
    colors: CollapsibleListHeaderColors,
) {
    ExpListItem(
        modifier = modifier,
        enabled = enabled,
        checked = isExpanded,
        onCheckedChange = onExpansionChange,
        shape = shape,
        leadingContent = leadingContent,
        overlineContent = overlineContent,
        headlineContent = headlineContent,
        supportingContent = supportingContent,
        trailingContent = trailingContent,
        colors = colors.asExpListItemColors(isExpanded = isExpanded)
    )
}

/**
 * Opinionated header composable for [CollapsibleListColumn].
 *
 * This overload uses the default [CollapsibleListDefaults.ExpandIcon] for the
 * `trailingContent` parameter.
 * @param modifier [Modifier] for the header.
 * @param enabled Whether clicking on this header should trigger [onExpansionChange].
 * @param isExpanded Whether the list has been expanded.
 * @param onExpansionChange Triggered when the header is clicked on. The new expansion state
 * is passed as the lambda argument.
 * @param leadingContent Content to be shown on the start/left-hand side of the layout.
 * @param overlineContent Content to be shown above the [headlineContent].
 * @param headlineContent Content to be shown above the [supportingContent].
 * This could be used to provide a title.
 * @param supportingContent Additional content to be shown. This could be used to provide
 * a description.
 * @param shape The shape of this header.
 * @param colors Desired [CollapsibleListHeaderColors] for this composable.
 * [CollapsibleListDefaults.headerColors] can be used to create these colours, or the opinionated
 * [CollapsibleListDefaults.primaryHeaderColors] and [CollapsibleListDefaults.tertiaryHeaderColors]
 * methods can be used for a primary or tertiary colour palette when the header is in
 * its expanded state.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CollapsibleListHeader(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isExpanded: Boolean,
    onExpansionChange: (Boolean) -> Unit,
    leadingContent: @Composable (() -> Unit)? = null,
    overlineContent: @Composable (() -> Unit)? = null,
    headlineContent: @Composable () -> Unit,
    supportingContent: @Composable (() -> Unit)? = null,
    shape: Shape = CollapsibleListDefaults.headerShape(isExpanded = isExpanded),
    colors: CollapsibleListHeaderColors
) {
    val indicatorContainerColor by colors.expandIndicatorContainerColor(
        enabled = enabled,
        isExpanded = isExpanded
    )
    val indicatorContentColor by colors.expandIndicatorContentColor(
        enabled = enabled,
        isExpanded = isExpanded
    )
    val indicatorRotation by animateFloatAsState(
        label = "Collapsible list indicator rotation",
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
    )

    CollapsibleListHeader(
        modifier = modifier,
        enabled = enabled,
        isExpanded = isExpanded,
        onExpansionChange = onExpansionChange,
        shape = shape,
        leadingContent = leadingContent,
        overlineContent = overlineContent,
        headlineContent = headlineContent,
        supportingContent = supportingContent,
        trailingContent = {
            CollapsibleListDefaults.ExpandIcon(
                color = indicatorContainerColor,
                contentColor = indicatorContentColor,
                rotationDeg = indicatorRotation
            )
        },
        colors = colors
    )
}

@Composable
private fun CollapsibleListSample(modifier: Modifier = Modifier) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    CollapsibleListColumn(
        modifier = modifier,
        isExpanded = isExpanded,
        header = {
            CollapsibleListHeader(
                isExpanded = isExpanded,
                onExpansionChange = { isExpanded = it },
                colors = CollapsibleListDefaults.primaryHeaderColors(),
                leadingContent = {
                    Icon(Icons.Outlined.Build, contentDescription = null)
                },
                overlineContent = {
                    Text(text = "Overline")
                },
                headlineContent = {
                    Text(text = "Click to expand/collapse")
                },
                supportingContent = {
                    Text(text = "Supporting")
                }
            )
        }
    ) {
        repeat(5) {
            ExpListItem(
                shape = ExpListItemDefaults.itemShape(it, 5),
                headlineContent = {
                    Text(text = "Item $it")
                }
            )
        }
    }
}

@Preview
@Composable
private fun CollapsibleListPreview() {
    StudyBuddyTheme {
        CollapsibleListSample()
    }
}
