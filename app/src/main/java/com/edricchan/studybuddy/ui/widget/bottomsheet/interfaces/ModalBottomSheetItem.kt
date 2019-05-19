package com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces

import android.view.View
import androidx.annotation.DrawableRes

/**
 * Represents an item in a modal bottom sheet
 * @property itemId A unique ID for the item
 * @property title The title of the item **(required)**
 * @property icon A drawable as a resource identifier
 * @property onClickListener A listener that is triggered when clicked on
 * @property visible Whether the item is visible
 * @property enabled Whether the item is enabled
 * @property groupId A group ID
 * @see ModalBottomSheetGroup.id
 * @see com.edricchan.studybuddy.ui.widget.bottomsheet.ModalBottomSheetFragment
 */
data class ModalBottomSheetItem(
		var itemId: Int = ITEM_ID_NONE,
		var title: String,
		@DrawableRes var icon: Int? = null,
		var onClickListener: View.OnClickListener? = null,
		var visible: Boolean = true,
		var enabled: Boolean = true,
		var groupId: Int = GROUP_ID_NONE
) {
	companion object {
		/** Represents that the item should not have an ID */
		const val ITEM_ID_NONE = 0
		/** Represents that the item should not have a group ID assigned to it */
		const val GROUP_ID_NONE = 0
	}
}