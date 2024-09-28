package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces

import android.os.Parcelable
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.ModalBottomSheetAdapter
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.annotations.ModalBottomSheetCheckableBehavior
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces.ModalBottomSheetGroup.CheckableBehavior
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Represents a group of [ModalBottomSheetItem]s
 * @property id An non-zero integer ID so that [ModalBottomSheetItem]s can use the ID
 * @property checkableBehaviorEnum The type of the checkable behavior for the group (Default:
 * [CheckableBehavior.None])
 * @property onItemCheckedChangeListener A listener which is called when an item's checked state is
 * toggled
 * @property visible Whether the group is visible
 * @property enabled Whether the group is enabled
 * @property selected The selected items
 */
@Parcelize
data class ModalBottomSheetGroup(
    val id: Int,
    val checkableBehaviorEnum: CheckableBehavior = CheckableBehavior.None,
    @IgnoredOnParcel val onItemCheckedChangeListener: ModalBottomSheetAdapter.OnItemCheckedChangeListener? = null,
    val visible: Boolean = true,
    val enabled: Boolean = true,
    val selected: List<ModalBottomSheetItem> = listOf()
) : Parcelable {
    @Deprecated("Use the overload which takes an enum for the checkable behaviour")
    constructor(
        id: Int,
        @ModalBottomSheetCheckableBehavior
        checkableBehavior: String,
        onItemCheckedChangeListener: ModalBottomSheetAdapter.OnItemCheckedChangeListener? = null,
        visible: Boolean = true,
        enabled: Boolean = true,
        selected: MutableList<ModalBottomSheetItem> = mutableListOf()
    ) : this(
        id = id,
        checkableBehaviorEnum = CheckableBehavior.fromValue(checkableBehavior),
        onItemCheckedChangeListener = onItemCheckedChangeListener,
        visible = visible,
        enabled = enabled,
        selected = selected
    )

    @Deprecated("Use checkableBehaviorEnum instead")
    @ModalBottomSheetCheckableBehavior
    val checkableBehavior: String = checkableBehaviorEnum.value

    enum class CheckableBehavior(@ModalBottomSheetCheckableBehavior internal val value: String) {
        /** No items can be checked. */
        None(value = CHECKABLE_BEHAVIOR_NONE),

        /** All items can be checked. */
        All(value = CHECKABLE_BEHAVIOR_ALL),

        /** Only one item can be checked. */
        Single(value = CHECKABLE_BEHAVIOR_SINGLE);

        companion object {
            internal fun fromValue(
                @ModalBottomSheetCheckableBehavior value: String
            ): CheckableBehavior = when (value) {
                CHECKABLE_BEHAVIOR_ALL -> All
                CHECKABLE_BEHAVIOR_SINGLE -> Single
                else -> None // Default to None if no valid value was specified
            }
        }
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

/**
 * Whether the receiver [ModalBottomSheetGroup] is checkable.
 * @return `true` if [ModalBottomSheetGroup.checkableBehaviorEnum] is
 * either [CheckableBehavior.All] or [CheckableBehavior.Single], `false` otherwise.
 */
fun ModalBottomSheetGroup.isCheckable() = checkableBehaviorEnum != CheckableBehavior.None
