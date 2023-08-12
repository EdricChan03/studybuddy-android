package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces

import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.ModalBottomSheetAdapter
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.annotations.ModalBottomSheetCheckableBehavior
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces.ModalBottomSheetGroup.Companion.CHECKABLE_BEHAVIOR_NONE

/**
 * Represents a group of [ModalBottomSheetItem]s
 * @property id An non-zero integer ID so that [ModalBottomSheetItem]s can use the ID
 * @property checkableBehavior The type of the checkable behavior for the group (Default:
 * [CHECKABLE_BEHAVIOR_NONE])
 * @property onItemCheckedChangeListener A listener which is called when an item's checked state is
 * toggled
 * @property visible Whether the group is visible
 * @property enabled Whether the group is enabled
 * @property selected The selected items
 */
class ModalBottomSheetGroup(
    var id: Int,
    @ModalBottomSheetCheckableBehavior var checkableBehavior: String = CHECKABLE_BEHAVIOR_NONE,
    var onItemCheckedChangeListener: ModalBottomSheetAdapter.OnItemCheckedChangeListener? = null,
    var visible: Boolean = true,
    var enabled: Boolean = true,
    var selected: MutableList<ModalBottomSheetItem> = mutableListOf()
) {
    constructor(
        id: Int,
        checkableBehavior: CheckableBehavior,
        onItemCheckedChangeListener: ModalBottomSheetAdapter.OnItemCheckedChangeListener? = null,
        visible: Boolean = true,
        enabled: Boolean = true,
        selected: MutableList<ModalBottomSheetItem> = mutableListOf()
    ) : this(
        id = id,
        checkableBehavior = checkableBehavior.value,
        onItemCheckedChangeListener = onItemCheckedChangeListener,
        visible = visible,
        enabled = enabled,
        selected = selected
    )

    override fun toString(): String {
        return "ModalBottomSheetGroup(id=$id, checkableBehavior=$checkableBehavior, visible=$visible, enabled=$enabled," +
            "selected=${selected.joinToString(prefix = "[", postfix = "]")}"
    }

    enum class CheckableBehavior(@ModalBottomSheetCheckableBehavior internal val value: String) {
        /** No items can be checked. */
        None(value = CHECKABLE_BEHAVIOR_NONE),

        /** All items can be checked. */
        All(value = CHECKABLE_BEHAVIOR_ALL),

        /** Only one item can be checked. */
        Single(value = CHECKABLE_BEHAVIOR_SINGLE),
    }

    companion object {
        /**
         * Represents that no items can be checked
         */
        const val CHECKABLE_BEHAVIOR_NONE = "none"

        /**
         * Represents that all items can be checked
         */
        const val CHECKABLE_BEHAVIOR_ALL = "all"

        /**
         * Represents that only one item can be checked
         */
        const val CHECKABLE_BEHAVIOR_SINGLE = "single"

        /**
         * Represents that the group should not have an ID
         */
        const val ID_NONE = 0
    }
}
