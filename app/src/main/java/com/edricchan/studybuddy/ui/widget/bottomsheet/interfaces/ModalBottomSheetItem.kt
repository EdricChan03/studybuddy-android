package com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.edricchan.studybuddy.ui.widget.bottomsheet.ModalBottomSheetAdapter

/**
 * Represents an item in a modal bottom sheet
 * @property itemId A unique ID for the item
 * @property title The title of the item **(required)**
 * @property icon A drawable as a resource identifier
 * @property iconDrawable A [Drawable]
 * @property onItemClickListener A listener that is triggered when clicked on
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
		var iconDrawable: Drawable? = null,
		var onItemClickListener: ModalBottomSheetAdapter.OnItemClickListener? = null,
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