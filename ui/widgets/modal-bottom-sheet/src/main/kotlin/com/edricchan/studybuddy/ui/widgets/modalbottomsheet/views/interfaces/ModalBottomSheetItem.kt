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
    constructor(
        id: Int = ID_NONE,
        title: String,
        @DrawableRes icon: Int,
        onItemClickListener: ModalBottomSheetAdapter.OnItemClickListener? = null,
        visible: Boolean = true,
        enabled: Boolean = true,
        group: ModalBottomSheetGroup? = null
    ) : this(
        id = id,
        title = title,
        icon = icon,
        iconDrawable = null,
        onItemClickListener = onItemClickListener,
        visible = visible,
        enabled = enabled,
        group = group
    )

    constructor(
        id: Int = ID_NONE,
        title: String,
        icon: Drawable,
        onItemClickListener: ModalBottomSheetAdapter.OnItemClickListener? = null,
        visible: Boolean = true,
        enabled: Boolean = true,
        group: ModalBottomSheetGroup? = null
    ) : this(
        id = id,
        title = title,
        icon = null,
        iconDrawable = icon,
        onItemClickListener = onItemClickListener,
        visible = visible,
        enabled = enabled,
        group = group
    )

    companion object {
        /** Represents that the item should not have an ID */
        const val ID_NONE = 0
    }
}
