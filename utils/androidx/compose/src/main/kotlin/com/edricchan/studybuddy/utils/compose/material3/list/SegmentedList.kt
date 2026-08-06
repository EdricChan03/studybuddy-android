package com.edricchan.studybuddy.utils.compose.material3.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

private sealed interface SegmentedItemData {
    @Composable
    fun SegmentedListItemScope.Content()

    data class Default(
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
                colors = this.colors ?: colors
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
    /** Desired [ListItemShapes] for this list item. */
    val shapes: ListItemShapes

    /** Desired [ListItemColors] for this list item. */
    val colors: ListItemColors
}

@Immutable
private class SegmentedListItemScopeImpl(
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
        items += SegmentedItemData.Default(
            content = content,
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
    listItemColors: ListItemColors = ListItemDefaults.segmentedColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface
    ),
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
