package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.ModalBottomSheetAdapter

/**
 * Represents an item in a modal bottom sheet
 * @property id A non-zero integer ID for the item
 * @property title The title of the item
 * @property icon A drawable as a resource identifier
 * @property onItemClickListener A listener that is triggered when clicked on
 * @property visible Whether the item is visible
 * @property enabled Whether the item is enabled
 * @property group A group reference
 * @see ModalBottomSheetGroup.id
 * @see com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.ModalBottomSheetFragment
 */
data class ModalBottomSheetItem(
    val id: Int = ID_NONE,
    val title: String,
    val icon: Icon? = null,
    val onItemClickListener: ModalBottomSheetAdapter.OnItemClickListener? = null,
    val visible: Boolean = true,
    val enabled: Boolean = true,
    val group: ModalBottomSheetGroup? = null
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
        icon = Icon.Resource(icon),
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
        icon = Icon.Raw(icon),
        onItemClickListener = onItemClickListener,
        visible = visible,
        enabled = enabled,
        group = group
    )

    /**
     * The visual graphic to be used for a [ModalBottomSheetItem].
     *
     * * To use an icon resource, use [Icon.Resource].
     * * To use a raw [Drawable], use [Icon.Raw].
     *
     * To retrieve the icon as a [Drawable], use [asDrawable].
     */
    sealed interface Icon {
        /** Converts this [Icon] to its [Drawable] form given the [context]. */
        fun asDrawable(context: Context): Drawable

        companion object {
            internal fun getIcon(
                iconRes: Int?,
                iconDrawable: Drawable?
            ): Icon? = if (iconRes != null) Resource(iconRes)
            else if (iconDrawable != null) Raw(iconDrawable)
            else null
        }

        /** An [Icon] which takes a [drawable resource][iconRes]. */
        data class Resource(@DrawableRes val iconRes: Int) : Icon {
            /**
             * Loads the [iconRes] resource from the specified [context],
             * or throws an [IllegalArgumentException] if no such resource was found.
             */
            override fun asDrawable(context: Context): Drawable =
                requireNotNull(AppCompatResources.getDrawable(context, iconRes)) {
                    "Resource specified (${
                        context.resources.getResourceEntryName(iconRes)
                    }) does not exist"
                }
        }

        /** An [Icon] which takes a [raw drawable][Drawable]. */
        data class Raw(val drawable: Drawable) : Icon {
            /** Returns the [drawable]. */
            override fun asDrawable(context: Context): Drawable = drawable
        }
    }

    companion object {
        /** Represents that the item should not have an ID */
        const val ID_NONE = 0
    }
}
