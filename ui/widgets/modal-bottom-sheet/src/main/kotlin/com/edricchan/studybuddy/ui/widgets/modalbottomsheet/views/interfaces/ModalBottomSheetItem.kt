package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.ModalBottomSheetAdapter

/**
 * Represents an item in a modal bottom sheet
 * @property id A non-zero integer ID for the item
 * @property title The title of the item **(required)**
 * @property icon A drawable as a resource identifier
 * @property iconDrawable A [Drawable]
 * @property onItemClickListener A listener that is triggered when clicked on
 * @property visible Whether the item is visible
 * @property enabled Whether the item is enabled
 * @property group A group reference
 * @see ModalBottomSheetGroup.id
 * @see com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.ModalBottomSheetFragment
 */
data class ModalBottomSheetItem(
    var id: Int = ID_NONE,
    var title: String,
    @DrawableRes var icon: Int? = null,
    var iconDrawable: Drawable? = null,
    var onItemClickListener: ModalBottomSheetAdapter.OnItemClickListener? = null,
    var visible: Boolean = true,
    var enabled: Boolean = true,
    var group: ModalBottomSheetGroup? = null
) {
    companion object {
        /** Represents that the item should not have an ID */
        const val ID_NONE = 0
    }
}
