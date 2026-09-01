package com.edricchan.studybuddy.ui.widgets.compose.list.segmented

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.SegmentedListItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.edricchan.studybuddy.utils.compose.material3.list.SegmentedListItemPosition
import com.edricchan.studybuddy.utils.compose.material3.list.segmentedShapes

private sealed interface SegmentedItemData {
    @Composable
    fun SegmentedListItemScope.Content()

    data class NonInteractive(
        val content: @Composable () -> Unit,
        val leadingContent: @Composable (() -> Unit)? = null,
        val trailingContent: @Composable (() -> Unit)? = null,
        val overlineContent: @Composable (() -> Unit)? = null,
        val supportingContent: @Composable (() -> Unit)? = null,
        val colors: ListItemColors? = null
    ) : SegmentedItemData {
        @Composable
        override fun SegmentedListItemScope.Content() {
            SegmentedListItem(
                shapes = shapes,
                content = content,
                leadingContent = leadingContent,
                trailingContent = trailingContent,
                overlineContent = overlineContent,
                supportingContent = supportingContent,
                colors = this@NonInteractive.colors ?: colors
            )
        }
    }

    data class Clickable(
        val onClick: () -> Unit,
        val content: @Composable () -> Unit,
        val enabled: Boolean = true,
        val onLongClick: (() -> Unit)? = null,
        val onLongClickLabel: String? = null,
        val leadingContent: @Composable (() -> Unit)? = null,
        val trailingContent: @Composable (() -> Unit)? = null,
        val overlineContent: @Composable (() -> Unit)? = null,
        val supportingContent: @Composable (() -> Unit)? = null,
        val colors: ListItemColors? = null
    ) : SegmentedItemData {
        @Composable
        override fun SegmentedListItemScope.Content() {
            SegmentedListItem(
                onClick = onClick,
                enabled = enabled,
                onLongClick = onLongClick,
                onLongClickLabel = onLongClickLabel,
                shapes = shapes,
                content = content,
                leadingContent = leadingContent,
                trailingContent = trailingContent,
                overlineContent = overlineContent,
                supportingContent = supportingContent,
                colors = this@Clickable.colors ?: colors
            )
        }
    }

    data class Checkable(
        val checked: Boolean,
        val onCheckedChange: (Boolean) -> Unit,
        val content: @Composable () -> Unit,
        val enabled: Boolean = true,
        val onLongClick: (() -> Unit)? = null,
        val onLongClickLabel: String? = null,
        val leadingContent: @Composable (() -> Unit)? = null,
        val trailingContent: @Composable (() -> Unit)? = null,
        val overlineContent: @Composable (() -> Unit)? = null,
        val supportingContent: @Composable (() -> Unit)? = null,
        val colors: ListItemColors? = null
    ) : SegmentedItemData {
        @Composable
        override fun SegmentedListItemScope.Content() {
            SegmentedListItem(
                checked = checked,
                onCheckedChange = onCheckedChange,
                onLongClick = onLongClick,
                onLongClickLabel = onLongClickLabel,
                enabled = enabled,
                shapes = shapes,
                content = content,
                leadingContent = leadingContent,
                trailingContent = trailingContent,
                overlineContent = overlineContent,
                supportingContent = supportingContent,
                colors = this@Checkable.colors ?: colors
            )
        }
    }

    data class Selectable(
        val selected: Boolean,
        val onClick: () -> Unit,
        val content: @Composable () -> Unit,
        val enabled: Boolean = true,
        val onLongClick: (() -> Unit)? = null,
        val onLongClickLabel: String? = null,
        val leadingContent: @Composable (() -> Unit)? = null,
        val trailingContent: @Composable (() -> Unit)? = null,
        val overlineContent: @Composable (() -> Unit)? = null,
        val supportingContent: @Composable (() -> Unit)? = null,
        val colors: ListItemColors? = null
    ) : SegmentedItemData {
        @Composable
        override fun SegmentedListItemScope.Content() {
            SegmentedListItem(
                selected = selected,
                onClick = onClick,
                onLongClick = onLongClick,
                onLongClickLabel = onLongClickLabel,
                enabled = enabled,
                shapes = shapes,
                content = content,
                leadingContent = leadingContent,
                trailingContent = trailingContent,
                overlineContent = overlineContent,
                supportingContent = supportingContent,
                colors = this@Selectable.colors ?: colors
            )
        }
    }

    data class Custom(
        val content: @Composable SegmentedListItemScope.() -> Unit
    ) : SegmentedItemData {
        @Composable
        override fun SegmentedListItemScope.Content() {
            content()
        }
    }
}

interface SegmentedListScope {
    /**
     * Adds a [SegmentedListItem] to the segmented list.
     * @param leadingContent Leading (start) content for the list item.
     * @param trailingContent Trailing (end) content for the list item.
     * @param overlineContent Overline content for the list item.
     * @param supportingContent Supporting content for the list item.
     * @param colors Desired [ListItemColors] for this list item. If not specified, it
     * defaults to the [ListItemColors] that are passed to the segmented list.
     * @param content Main content for the list item.
     */
    fun item(
        leadingContent: @Composable (() -> Unit)? = null,
        trailingContent: @Composable (() -> Unit)? = null,
        overlineContent: @Composable (() -> Unit)? = null,
        supportingContent: @Composable (() -> Unit)? = null,
        colors: ListItemColors? = null,
        content: @Composable () -> Unit,
    )

    /**
     * Adds a clickable [SegmentedListItem] to the segmented list.
     * @param onClick Invoked when the list item is clicked on.
     * @param enabled Controls the enabled state of this list item. When `false`,
     * this component will not respond to user input, and it will appear visually
     * disabled and disabled to accessibility services.
     * @param onLongClick Invoked when the list item is long clicked on.
     * @param onLongClickLabel Accessible label for the long click event.
     * @param leadingContent Leading (start) content for the list item.
     * @param trailingContent Trailing (end) content for the list item.
     * @param overlineContent Overline content for the list item.
     * @param supportingContent Supporting content for the list item.
     * @param colors Desired [ListItemColors] for this list item. If not specified, it
     * defaults to the [ListItemColors] that are passed to the segmented list.
     * @param content Main content for the list item.
     */
    fun item(
        onClick: () -> Unit,
        enabled: Boolean = true,
        onLongClick: (() -> Unit)? = null,
        onLongClickLabel: String? = null,
        leadingContent: @Composable (() -> Unit)? = null,
        trailingContent: @Composable (() -> Unit)? = null,
        overlineContent: @Composable (() -> Unit)? = null,
        supportingContent: @Composable (() -> Unit)? = null,
        colors: ListItemColors? = null,
        content: @Composable () -> Unit,
    )

    /**
     * Adds a checkable [SegmentedListItem] to the segmented list.
     * @param checked Whether the list item is checked.
     * @param onCheckedChange Invoked when the list item's checked state has changed.
     * @param enabled Controls the enabled state of this list item. When `false`,
     * this component will not respond to user input, and it will appear visually
     * disabled and disabled to accessibility services.
     * @param onLongClick Invoked when the list item is long clicked on.
     * @param onLongClickLabel Accessible label for the long click event.
     * @param leadingContent Leading (start) content for the list item.
     * @param trailingContent Trailing (end) content for the list item.
     * @param overlineContent Overline content for the list item.
     * @param supportingContent Supporting content for the list item.
     * @param colors Desired [ListItemColors] for this list item. If not specified, it
     * defaults to the [ListItemColors] that are passed to the segmented list.
     * @param content Main content for the list item.
     */
    fun item(
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        enabled: Boolean = true,
        onLongClick: (() -> Unit)? = null,
        onLongClickLabel: String? = null,
        leadingContent: @Composable (() -> Unit)? = null,
        trailingContent: @Composable (() -> Unit)? = null,
        overlineContent: @Composable (() -> Unit)? = null,
        supportingContent: @Composable (() -> Unit)? = null,
        colors: ListItemColors? = null,
        content: @Composable () -> Unit,
    )

    /**
     * Adds a selectable [SegmentedListItem] to the segmented list.
     * @param selected Whether the list item is selected.
     * @param onClick Invoked when the list item is clicked on.
     * @param enabled Controls the enabled state of this list item. When `false`,
     * this component will not respond to user input, and it will appear visually
     * disabled and disabled to accessibility services.
     * @param onLongClick Invoked when the list item is long clicked on.
     * @param onLongClickLabel Accessible label for the long click event.
     * @param leadingContent Leading (start) content for the list item.
     * @param trailingContent Trailing (end) content for the list item.
     * @param overlineContent Overline content for the list item.
     * @param supportingContent Supporting content for the list item.
     * @param colors Desired [ListItemColors] for this list item. If not specified, it
     * defaults to the [ListItemColors] that are passed to the segmented list.
     * @param content Main content for the list item.
     */
    fun item(
        selected: Boolean,
        onClick: () -> Unit,
        enabled: Boolean = true,
        onLongClick: (() -> Unit)? = null,
        onLongClickLabel: String? = null,
        leadingContent: @Composable (() -> Unit)? = null,
        trailingContent: @Composable (() -> Unit)? = null,
        overlineContent: @Composable (() -> Unit)? = null,
        supportingContent: @Composable (() -> Unit)? = null,
        colors: ListItemColors? = null,
        content: @Composable () -> Unit,
    )

    /**
     * Adds a custom item to the segmented list.
     * @param content Desired content to be shown. The [ListItemShapes] and [ListItemColors]
     * are passed to the content lambda respectively from the [SegmentedListItemScope].
     * @see SegmentedListItemScope
     */
    fun customItem(
        content: @Composable SegmentedListItemScope.() -> Unit
    )
}

@Stable
interface SegmentedListItemScope : ColumnScope {
    /** Current index for this list item. */
    val index: Int

    /** Number of items added to the surrounding [SegmentedListScope]. */
    val itemsCount: Int

    /** Whether this item is the first list item. */
    val isFirst: Boolean get() = index == 0

    /** Whether this item is the last list item. */
    val isLast: Boolean get() = index == itemsCount - 1

    /** Desired [ListItemShapes] for this list item. */
    val shapes: ListItemShapes

    /** Desired [ListItemColors] for this list item. */
    val colors: ListItemColors
}

@Immutable
private class SegmentedListItemScopeImpl(
    override val index: Int,
    override val itemsCount: Int,
    override val shapes: ListItemShapes,
    override val colors: ListItemColors,
    val columnScope: ColumnScope
) : SegmentedListItemScope, ColumnScope by columnScope

private class SegmentedListScopeImpl(
    val items: MutableList<SegmentedItemData> = mutableListOf()
) : SegmentedListScope {
    override fun item(
        leadingContent: @Composable (() -> Unit)?,
        trailingContent: @Composable (() -> Unit)?,
        overlineContent: @Composable (() -> Unit)?,
        supportingContent: @Composable (() -> Unit)?,
        colors: ListItemColors?,
        content: @Composable (() -> Unit),
    ) {
        items += SegmentedItemData.NonInteractive(
            content = content,
            leadingContent = leadingContent,
            trailingContent = trailingContent,
            overlineContent = overlineContent,
            supportingContent = supportingContent,
            colors = colors
        )
    }

    override fun item(
        onClick: () -> Unit,
        enabled: Boolean,
        onLongClick: (() -> Unit)?,
        onLongClickLabel: String?,
        leadingContent: @Composable (() -> Unit)?,
        trailingContent: @Composable (() -> Unit)?,
        overlineContent: @Composable (() -> Unit)?,
        supportingContent: @Composable (() -> Unit)?,
        colors: ListItemColors?,
        content: @Composable (() -> Unit)
    ) {
        items += SegmentedItemData.Clickable(
            content = content,
            onClick = onClick,
            enabled = enabled,
            onLongClick = onLongClick,
            onLongClickLabel = onLongClickLabel,
            leadingContent = leadingContent,
            trailingContent = trailingContent,
            overlineContent = overlineContent,
            supportingContent = supportingContent,
            colors = colors
        )
    }

    override fun item(
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        enabled: Boolean,
        onLongClick: (() -> Unit)?,
        onLongClickLabel: String?,
        leadingContent: @Composable (() -> Unit)?,
        trailingContent: @Composable (() -> Unit)?,
        overlineContent: @Composable (() -> Unit)?,
        supportingContent: @Composable (() -> Unit)?,
        colors: ListItemColors?,
        content: @Composable (() -> Unit)
    ) {
        items += SegmentedItemData.Checkable(
            content = content,
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            onLongClick = onLongClick,
            onLongClickLabel = onLongClickLabel,
            leadingContent = leadingContent,
            trailingContent = trailingContent,
            overlineContent = overlineContent,
            supportingContent = supportingContent,
            colors = colors
        )
    }

    override fun item(
        selected: Boolean,
        onClick: () -> Unit,
        enabled: Boolean,
        onLongClick: (() -> Unit)?,
        onLongClickLabel: String?,
        leadingContent: @Composable (() -> Unit)?,
        trailingContent: @Composable (() -> Unit)?,
        overlineContent: @Composable (() -> Unit)?,
        supportingContent: @Composable (() -> Unit)?,
        colors: ListItemColors?,
        content: @Composable (() -> Unit)
    ) {
        items += SegmentedItemData.Selectable(
            content = content,
            selected = selected,
            onClick = onClick,
            enabled = enabled,
            onLongClick = onLongClick,
            onLongClickLabel = onLongClickLabel,
            leadingContent = leadingContent,
            trailingContent = trailingContent,
            overlineContent = overlineContent,
            supportingContent = supportingContent,
            colors = colors
        )
    }

    override fun customItem(content: @Composable SegmentedListItemScope.() -> Unit) {
        items += SegmentedItemData.Custom(
            content = content
        )
    }
}

@Composable
fun SegmentedListColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    listItemColors: ListItemColors = ListItemDefaults.segmentedColors(),
    itemsInit: SegmentedListScope.() -> Unit
) {
    val listScope = remember { SegmentedListScopeImpl().apply(itemsInit) }

    val items = listScope.items

    // We're not using a LazyColumn here for cases where this SegmentedListColumn is nested
    // within a LazyColumn
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        repeat(items.size) { i ->
            key(i) {
                val item = items[i]
                val itemScope = SegmentedListItemScopeImpl(
                    index = i,
                    itemsCount = items.size,
                    shapes = ListItemDefaults.segmentedShapes(
                        position = SegmentedListItemPosition.fromIndex(i, listScope.items.size)
                    ),
                    colors = listItemColors,
                    columnScope = this
                )
                with(item) {
                    itemScope.Content()
                }
            }
        }
    }
}
