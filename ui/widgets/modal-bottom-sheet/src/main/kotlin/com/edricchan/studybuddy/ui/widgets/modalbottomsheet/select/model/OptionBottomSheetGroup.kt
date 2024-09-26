package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

// TODO: Move to common bottom-sheet module
/**
 * A selectable list of [OptionBottomSheetItem]s.
 *
 * This should be used as the replacement of [BottomSheetOptionGroup] where applicable.
 * @property items The list of items.
 * @property enabled Whether this group's items are selectable.
 * @property checkableBehavior [CheckableBehavior] for this group.
 */
@Parcelize
sealed interface OptionBottomSheetGroup<Id> : Parcelable, List<OptionBottomSheetItem<Id>> {
    val items: List<OptionBottomSheetItem<Id>>
    val enabled: Boolean
    val checkableBehavior: CheckableBehavior

    /** Whether the specified [item] has been marked as selected in this group. */
    fun isChecked(item: OptionBottomSheetItem<Id>): Boolean

    @IgnoredOnParcel
        /** Whether any items have been selected. */
    val hasSelection: Boolean

    /**
     * Sub-class of [OptionBottomSheetGroup] which supports **single**-selection -
     * i.e. only one item can be selected, typically represented by a selected radio button.
     * @property selectedIndex Selected index, if any, which should be an existing index in [items].
     */
    @Parcelize
    data class SingleSelect<Id>(
        override val items: List<OptionBottomSheetItem<Id>>,
        override val enabled: Boolean = true,
        val selectedIndex: Int? = null
    ) : OptionBottomSheetGroup<Id>, List<OptionBottomSheetItem<Id>> by items {
        /** Indicates that this group supports [CheckableBehavior.Single] selection. */
        @IgnoredOnParcel
        override val checkableBehavior: CheckableBehavior = CheckableBehavior.Single

        @IgnoredOnParcel
        override val hasSelection: Boolean = selectedIndex != null

        /** Retrieves the selected item of this group, or `null` if no selection was made. */
        val selectedItem get() = selectedIndex?.let { items[it] }

        override fun isChecked(item: OptionBottomSheetItem<Id>): Boolean {
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
    @Parcelize
    data class MultiSelect<Id>(
        override val items: List<OptionBottomSheetItem<Id>>,
        override val enabled: Boolean = true,
        val selectedIndices: Set<Int> = setOf()
    ) : OptionBottomSheetGroup<Id>, List<OptionBottomSheetItem<Id>> by items {
        constructor(
            itemsSelectionMap: Map<OptionBottomSheetItem<Id>, Boolean>,
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
        val selectedItems: Set<OptionBottomSheetItem<Id>>
            get() = items.filterIndexed { index, _ -> index in selectedIndices }
                .toSet()

        override fun isChecked(item: OptionBottomSheetItem<Id>): Boolean = item in selectedItems
    }
}

// For copy extension function
private val <Id> OptionBottomSheetGroup<Id>.selectedIndices: Set<Int>
    get() = when (this) {
        is OptionBottomSheetGroup.SingleSelect -> setOfNotNull(selectedIndex)
        is OptionBottomSheetGroup.MultiSelect -> selectedIndices
    }

/**
 * Creates a copy of the receiver [OptionBottomSheetGroup.MultiSelect]
 * with the specified [item] toggled.
 */
fun <Id> OptionBottomSheetGroup.MultiSelect<Id>.toggleSelectedItem(
    item: OptionBottomSheetItem<Id>,
    isSelected: Boolean
): OptionBottomSheetGroup.MultiSelect<Id> {
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
fun <Id> OptionBottomSheetGroup.SingleSelect<Id>.withSelectedItem(
    item: OptionBottomSheetItem<Id>
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
fun <Id> OptionBottomSheetGroup<Id>.copy(
    items: List<OptionBottomSheetItem<Id>> = this.items,
    enabled: Boolean = this.enabled,
    selectedIndices: Set<Int> = this.selectedIndices
): OptionBottomSheetGroup<Id> = when (this) {
    is OptionBottomSheetGroup.SingleSelect<Id> -> copy(
        items = items,
        enabled = enabled,
        selectedIndex = selectedIndices.single()
    )

    is OptionBottomSheetGroup.MultiSelect<Id> -> copy(
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
fun <Id> OptionBottomSheetGroup<Id>.copy(
    items: List<OptionBottomSheetItem<Id>> = this.items,
    enabled: Boolean = this.enabled,
    selectedIndex: Int? = null
): OptionBottomSheetGroup<Id> = when (this) {
    is OptionBottomSheetGroup.SingleSelect<Id> -> copy(
        items = items,
        enabled = enabled,
        selectedIndex = selectedIndex
    )

    is OptionBottomSheetGroup.MultiSelect<Id> -> copy(
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
fun <Id> OptionBottomSheetGroup<Id>.copy(
    items: List<OptionBottomSheetItem<Id>> = this.items,
    enabled: Boolean = this.enabled,
    selectedItems: Set<OptionBottomSheetItem<Id>> = setOf()
): OptionBottomSheetGroup<Id> = copy(
    items = items,
    enabled = enabled,
    selectedIndices = items.withIndex()
        .filter { it.value in selectedItems }
        .mapTo(mutableSetOf()) { it.index }
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
fun <Id> OptionBottomSheetGroup<Id>.copy(
    items: List<OptionBottomSheetItem<Id>> = this.items,
    enabled: Boolean = this.enabled,
    selectedItem: OptionBottomSheetItem<Id>? = null
): OptionBottomSheetGroup<Id> = copy(
    items = items,
    enabled = enabled,
    selectedIndices =
    if (this is OptionBottomSheetGroup.MultiSelect<Id>) selectedIndices else setOf<Int>()
        + items.withIndex()
        .filter { it.value == selectedItem }
        .mapTo(mutableSetOf()) { it.index }
)

/**
 * Gets the [Set] of selected items of the receiver [OptionBottomSheetGroup].
 *
 * If the receiver is a [OptionBottomSheetGroup.SingleSelect], a set consisting of the
 * single element [OptionBottomSheetGroup.SingleSelect.selectedItem] will be returned,
 * or an empty set if no value was selected.
 * @see selectedItem
 */
val <Id> OptionBottomSheetGroup<Id>.selectedItems: Set<OptionBottomSheetItem<Id>>
    get() = when (this) {
        is OptionBottomSheetGroup.SingleSelect<Id> -> setOfNotNull(selectedItem)
        is OptionBottomSheetGroup.MultiSelect<Id> -> selectedItems
    }

/**
 * Gets the selected item of the receiver [OptionBottomSheetGroup].
 *
 * If the receiver is a [OptionBottomSheetGroup.MultiSelect], the first element from
 * [OptionBottomSheetGroup.MultiSelect.selectedItems] will be used. If this behaviour
 * is not desired, use [selectedItems] instead.
 * @see selectedItems
 */
val <Id> OptionBottomSheetGroup<Id>.selectedItem: OptionBottomSheetItem<Id>?
    get() = when (this) {
        is OptionBottomSheetGroup.SingleSelect<Id> -> selectedItem
        is OptionBottomSheetGroup.MultiSelect<Id> -> selectedItems.firstOrNull()
    }
