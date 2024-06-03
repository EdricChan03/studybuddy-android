package io.github.edricchan03.optionbottomsheet.models

import androidx.compose.runtime.Immutable
import io.github.edricchan03.optionbottomsheet.models.BottomSheetOptionGroup.CheckableBehavior

typealias BottomSheetOptionGroupCheckedChangeListener = (item: BottomSheetOption, checked: Boolean) -> Unit

/**
 * Represents a group of [BottomSheetOption]s.
 * @property title Title for the group.
 * @property items The [BottomSheetOption]s to display for this group.
 * @property checkableBehavior The type of the checkable behavior for the group (Default:
 * [CheckableBehavior.None])
 * @property onCheckedChange Lambda which is invoked when an item's checked state is
 * toggled.
 * @property visible Whether the group is visible.
 * @property enabled Whether the group is enabled.
 * @property selectedIndices The selected indices. To get the selected items as a list
 * of [BottomSheetOption]s, use [selectedItems].
 *
 * Note that there is a runtime check to assert that the number of selected indices does not
 * exceed that of the list of [items].
 */
@Immutable
data class BottomSheetOptionGroup(
    val title: String? = null,
    val items: List<BottomSheetOption>,
    val visible: Boolean = true,
    val enabled: Boolean = true,
    val onCheckedChange: BottomSheetOptionGroupCheckedChangeListener = { _, _ -> },
    val checkableBehavior: CheckableBehavior = CheckableBehavior.None,
    val selectedIndices: Set<Int> = setOf()
) {
    init {
        require(selectedIndices.size <= items.size) {
            "There should not be more selected indices of size ${selectedIndices.size} " +
                "than items of size ${items.size}"
        }
        require(selectedIndices.all { it in items.indices }) {
            "The selected indices should only exist in the list of items' indices but " +
                "selected indices ${selectedIndices.filterNot { it in items.indices }} were found " +
                "that are not in the range of items indices ${items.indices}"
        }
    }

    constructor(
        title: String? = null,
        itemsSelectionMap: Map<BottomSheetOption, Boolean>,
        visible: Boolean = true,
        enabled: Boolean = true,
        onCheckedChange: BottomSheetOptionGroupCheckedChangeListener = { _, _ -> },
        checkableBehavior: CheckableBehavior = CheckableBehavior.None
    ) : this(
        title = title,
        items = itemsSelectionMap.keys.toList(),
        visible = visible,
        enabled = enabled,
        onCheckedChange = onCheckedChange,
        checkableBehavior = checkableBehavior,
        selectedIndices = itemsSelectionMap.values
            .withIndex().filter { it.value }.map { it.index }
            .toSet()
    )

    /** Retrieves the [current list of selected items][selectedIndices]. */
    val selectedItems
        get() = items
            .filterIndexed { index, _ -> index in selectedIndices }

    /** Retrieves the list of items associated with its selected state. */
    val itemsSelectionMap
        get() = items.withIndex()
            .associateBy(
                keySelector = { it.value },
                valueTransform = { it.index in selectedIndices }
            )

    /** The checkable behaviour for a [group][BottomSheetOptionGroup]. */
    enum class CheckableBehavior {
        /** No selection controls should be shown. */
        None,

        /**
         * Checkbox selection controls should be shown - one or more of the items can be
         * selected.
         */
        All,

        /**
         * Radio selection controls should be shown - only one item can be selected at
         * a time.
         */
        Single
    }
}
