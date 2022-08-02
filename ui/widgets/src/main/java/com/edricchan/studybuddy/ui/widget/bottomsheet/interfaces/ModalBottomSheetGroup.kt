package com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces

import com.edricchan.studybuddy.ui.widget.bottomsheet.ModalBottomSheetAdapter
import com.edricchan.studybuddy.ui.widget.bottomsheet.annotations.ModalBottomSheetCheckableBehavior
import com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces.ModalBottomSheetGroup.Companion.CHECKABLE_BEHAVIOR_NONE

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
    override fun toString(): String {
        return "ModalBottomSheetGroup(id=$id, checkableBehavior=$checkableBehavior, visible=$visible, enabled=$enabled," +
                "selected=${selected.joinToString(prefix = "[", postfix = "]")}"
    }

    companion object {
        /**
         * Represents that only one item can be checked
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