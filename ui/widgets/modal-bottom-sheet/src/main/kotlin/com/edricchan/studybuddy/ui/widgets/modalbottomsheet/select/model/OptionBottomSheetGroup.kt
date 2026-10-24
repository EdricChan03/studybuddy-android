package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model

import androidx.compose.runtime.Immutable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.serialization.Serializable

// TODO: Move to common bottom-sheet module
/**
 * A selectable list of [OptionBottomSheetItem]s.
 *
 * This should be used as the replacement of [BottomSheetOptionGroup] where applicable.
 * @property items The list of items.
 * @property enabled Whether this group's items are selectable.
 * @property checkableBehavior [CheckableBehavior] for this group.
 */
@Serializable
@Immutable
sealed interface OptionBottomSheetGroup : List<OptionBottomSheetItem> {
    val items: List<OptionBottomSheetItem>
    val enabled: Boolean
    val checkableBehavior: CheckableBehavior

    /** Whether the specified [item] has been marked as selected in this group. */
    fun isChecked(item: OptionBottomSheetItem): Boolean

    @IgnoredOnParcel
        /** Whether any items have been selected. */
    val hasSelection: Boolean

    /**
     * Sub-class of [OptionBottomSheetGroup] which supports **single**-selection -
     * i.e. only one item can be selected, typically represented by a selected radio button.
     * @property selectedIndex Selected index, if any, which should be an existing index in [items].
     */
    @Serializable
    @Immutable
    data class SingleSelect(
        override val items: List<OptionBottomSheetItem>,
        override val enabled: Boolean = true,
        val selectedIndex: Int? = null
    ) : OptionBottomSheetGroup, List<OptionBottomSheetItem> by items {
        /** Indicates that this group supports [CheckableBehavior.Single] selection. */
        @IgnoredOnParcel
        override val checkableBehavior: CheckableBehavior = CheckableBehavior.Single

        @IgnoredOnParcel
        override val hasSelection: Boolean = selectedIndex != null

        /** Retrieves the selected item of this group, or `null` if no selection was made. */
        val selectedItem get() = selectedIndex?.let { items[it] }

        override fun isChecked(item: OptionBottomSheetItem): Boolean {
            // Don't perform a lookup if no index was set in the first place
            if (selectedIndex == null) return false

            return items.indexOf(item) == selectedIndex
        }
    }

    /**
     * Sub-class of [OptionBottomSheetGroup] which supports **multi**-selection -
     * i.e. multiple items can be selected, typically represented by selected checkbox(es).
     * @property selectedIndices The [Set] of selected indices, if any. Each index in this set
     * should exist in [items].
     */
    @Serializable
    @Immutable
    data class MultiSelect(
        override val items: List<OptionBottomSheetItem>,
        override val enabled: Boolean = true,
        val selectedIndices: Set<Int> = setOf()
    ) : OptionBottomSheetGroup, List<OptionBottomSheetItem> by items {
        constructor(
            itemsSelectionMap: Map<OptionBottomSheetItem, Boolean>,
            enabled: Boolean = true
        ) : this(
            items = itemsSelectionMap.keys.toList(),
            enabled = enabled,
            selectedIndices = List(
                itemsSelectionMap.filterValues { it }.entries.size
            ) { it }.toSet()
        )

        /** Indicates that this group supports [CheckableBehavior.Multi] selection. */
        @IgnoredOnParcel
        override val checkableBehavior: CheckableBehavior = CheckableBehavior.Multi

        @IgnoredOnParcel
        override val hasSelection: Boolean = selectedItems.isNotEmpty()

        /** Retrieves the [Set] of selected items of this group. */
        val selectedItems: Set<OptionBottomSheetItem>
            get() = items.filterIndexed { index, _ -> index in selectedIndices }
                .toSet()

        override fun isChecked(item: OptionBottomSheetItem): Boolean = item in selectedItems
    }
}

// For copy extension function
private val OptionBottomSheetGroup.selectedIndices: Set<Int>
    get() = when (this) {
        is OptionBottomSheetGroup.SingleSelect -> setOfNotNull(selectedIndex)
        is OptionBottomSheetGroup.MultiSelect -> selectedIndices
    }

// For copy extension function
private val OptionBottomSheetGroup.selectedIndex: Int?
    get() = when (this) {
        is OptionBottomSheetGroup.SingleSelect -> selectedIndex
        is OptionBottomSheetGroup.MultiSelect -> selectedIndices.firstOrNull()
    }

/**
 * Creates a copy of the receiver [OptionBottomSheetGroup.MultiSelect]
 * with the specified [item] toggled.
 */
fun OptionBottomSheetGroup.MultiSelect.toggleSelectedItem(
    item: OptionBottomSheetItem,
    isSelected: Boolean
): OptionBottomSheetGroup.MultiSelect {
    require(item in items) { "Specified item does not exist in this group's items" }
    val isChecked = isChecked(item)
    // Nothing to do if the item was not selected in the first place
    if ((isChecked && isSelected) || (!isChecked && !isSelected)) return this

    return copy(
        selectedIndices = if (isSelected) selectedIndices + items.indexOf(item) else
            selectedIndices - items.indexOf(item)
    )
}

/**
 * Creates a copy of the receiver [OptionBottomSheetGroup.SingleSelect]
 * with the specified [item] selected.
 */
fun OptionBottomSheetGroup.SingleSelect.withSelectedItem(
    item: OptionBottomSheetItem
) = copy(
    selectedIndex = items.indexOf(item)
)

/**
 * Creates a copy of the receiver [OptionBottomSheetGroup] with the
 * specified options.
 * @param items The new list of items.
 * @param enabled Whether any of the items are selectable.
 * @param selectedIndices The list of selected indices, which should be present
 * in [items]. (Note: If the receiver
 * [OptionBottomSheetGroup] is a [OptionBottomSheetGroup.SingleSelect], the
 * [selectedIndices] is assumed to **only** contain a single item with [Iterable.single],
 * which will be passed to [OptionBottomSheetGroup.SingleSelect.selectedIndex].)
 */
@JvmName("copyWithIndices")
fun OptionBottomSheetGroup.copy(
    items: List<OptionBottomSheetItem> = this.items,
    enabled: Boolean = this.enabled,
    selectedIndices: Set<Int> = this.selectedIndices
): OptionBottomSheetGroup = when (this) {
    is OptionBottomSheetGroup.SingleSelect -> copy(
        items = items,
        enabled = enabled,
        selectedIndex = selectedIndices.single()
    )

    is OptionBottomSheetGroup.MultiSelect -> copy(
        items = items,
        enabled = enabled,
        selectedIndices = selectedIndices
    )
}

/**
 * Creates a copy of the receiver [OptionBottomSheetGroup] with the
 * specified options.
 * @param items The new list of items.
 * @param enabled Whether any of the items are selectable.
 * @param selectedIndex The selected item's index. (Note: If the receiver
 * [OptionBottomSheetGroup] is a [OptionBottomSheetGroup.MultiSelect], a [Set]
 * consisting of this item and the existing items will be used for
 * [OptionBottomSheetGroup.MultiSelect.selectedIndices], or the original value will
 * be used.)
 */
fun OptionBottomSheetGroup.copy(
    items: List<OptionBottomSheetItem> = this.items,
    enabled: Boolean = this.enabled,
    selectedIndex: Int? = this.selectedIndex
): OptionBottomSheetGroup = when (this) {
    is OptionBottomSheetGroup.SingleSelect -> copy(
        items = items,
        enabled = enabled,
        selectedIndex = selectedIndex
    )

    is OptionBottomSheetGroup.MultiSelect -> copy(
        items = items,
        enabled = enabled,
        selectedIndices = selectedIndex?.let { selectedIndices + it } ?: selectedIndices
    )
}

/**
 * Creates a copy of the receiver [OptionBottomSheetGroup] with the
 * specified options.
 * @param items The new list of items.
 * @param enabled Whether any of the items are selectable.
 * @param selectedItems The list of selected items, which should be present
 * in [items]. (Note: If the receiver
 * [OptionBottomSheetGroup] is a [OptionBottomSheetGroup.SingleSelect], the
 * [selectedItems] is assumed to **only** contain a single item with [Iterable.single],
 * which will be passed to [OptionBottomSheetGroup.SingleSelect.selectedIndex].)
 */
fun OptionBottomSheetGroup.copy(
    items: List<OptionBottomSheetItem> = this.items,
    enabled: Boolean = this.enabled,
    selectedItems: Set<OptionBottomSheetItem> = this.selectedItems
): OptionBottomSheetGroup = copy(
    items = items,
    enabled = enabled,
    selectedIndices = items.withIndex()
        .mapNotNull { (index, value) -> index.takeIf { value in selectedItems } }
        .toSet()
)

/**
 * Creates a copy of the receiver [OptionBottomSheetGroup] with the
 * specified options.
 * @param items The new list of items.
 * @param enabled Whether any of the items are selectable.
 * @param selectedItem The selected item's index. (Note: If the receiver
 * [OptionBottomSheetGroup] is a [OptionBottomSheetGroup.MultiSelect], a [Set]
 * consisting of this item and the existing items will be used for
 * [OptionBottomSheetGroup.MultiSelect.selectedIndices], or the original value will
 * be used.)
 */
fun OptionBottomSheetGroup.copy(
    items: List<OptionBottomSheetItem> = this.items,
    enabled: Boolean = this.enabled,
    selectedItem: OptionBottomSheetItem? = this.selectedItem
): OptionBottomSheetGroup = copy(
    items = items,
    enabled = enabled,
    selectedIndices =
        if (this is OptionBottomSheetGroup.MultiSelect) selectedIndices else setOf<Int>()
            + items.withIndex()
            .mapNotNull { (index, value) -> index.takeIf { value == selectedItem } }
            .toSet()
)

/**
 * Gets the [Set] of selected items of the receiver [OptionBottomSheetGroup].
 *
 * If the receiver is a [OptionBottomSheetGroup.SingleSelect], a set consisting of the
 * single element [OptionBottomSheetGroup.SingleSelect.selectedItem] will be returned,
 * or an empty set if no value was selected.
 * @see selectedItem
 */
val OptionBottomSheetGroup.selectedItems: Set<OptionBottomSheetItem>
    get() = when (this) {
        is OptionBottomSheetGroup.SingleSelect -> setOfNotNull(selectedItem)
        is OptionBottomSheetGroup.MultiSelect -> selectedItems
    }

/**
 * Gets the selected item of the receiver [OptionBottomSheetGroup].
 *
 * If the receiver is a [OptionBottomSheetGroup.MultiSelect], the first element from
 * [OptionBottomSheetGroup.MultiSelect.selectedItems] will be used. If this behaviour
 * is not desired, use [selectedItems] instead.
 * @see selectedItems
 */
val OptionBottomSheetGroup.selectedItem: OptionBottomSheetItem?
    get() = when (this) {
        is OptionBottomSheetGroup.SingleSelect -> selectedItem
        is OptionBottomSheetGroup.MultiSelect -> selectedItems.firstOrNull()
    }
