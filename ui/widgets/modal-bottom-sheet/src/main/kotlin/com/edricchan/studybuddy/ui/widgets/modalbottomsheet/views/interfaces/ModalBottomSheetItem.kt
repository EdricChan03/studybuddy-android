package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.ModalBottomSheetAdapter
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Represents an item in a modal bottom sheet
 * @property id A non-zero integer ID for the item
 * @property title The title of the item
 * @property icon A drawable as a resource identifier
 * @property onItemClickListener A listener that is triggered when clicked on
 * @property visible Whether the item is visible
 * @property enabled Whether the item is enabled
 * @property requestDismissOnClick Whether the bottom sheet should be dismissed when this
 * item is clicked.
 * @see com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.ModalBottomSheetFragment
 * @see com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.dsl.item
 */
@Parcelize
data class ModalBottomSheetItem(
    val id: Int = ID_NONE,
    val title: String,
    val icon: Icon? = null,
    @IgnoredOnParcel val onItemClickListener: ModalBottomSheetAdapter.OnItemClickListener? = null,
    val visible: Boolean = true,
    val enabled: Boolean = true,
    val requestDismissOnClick: Boolean = true
) : Parcelable {
    constructor(
        id: Int = ID_NONE,
        title: String,
        @DrawableRes icon: Int,
        onItemClickListener: ModalBottomSheetAdapter.OnItemClickListener? = null,
        visible: Boolean = true,
        enabled: Boolean = true,
        requestDismissOnClick: Boolean = true
    ) : this(
        id = id,
        title = title,
        icon = Icon.Resource(icon),
        onItemClickListener = onItemClickListener,
        visible = visible,
        enabled = enabled,
        requestDismissOnClick = requestDismissOnClick
    )

    constructor(
        id: Int = ID_NONE,
        title: String,
        icon: Drawable,
        onItemClickListener: ModalBottomSheetAdapter.OnItemClickListener? = null,
        visible: Boolean = true,
        enabled: Boolean = true,
        requestDismissOnClick: Boolean = true
    ) : this(
        id = id,
        title = title,
        icon = Icon.Raw(icon.toBitmap()),
        onItemClickListener = onItemClickListener,
        visible = visible,
        enabled = enabled,
        requestDismissOnClick = requestDismissOnClick
    )

    /**
     * The visual graphic to be used for a [ModalBottomSheetItem].
     *
     * * To use an icon resource, use [Icon.Resource].
     * * To use a raw [Drawable], use [Icon.Raw].
     *
     * To retrieve the icon as a [Drawable], use [asDrawable].
     */
    @Parcelize
    sealed class Icon : Parcelable {
        /** Converts this [Icon] to its [Drawable] form given the [context]. */
        abstract fun asBitmap(context: Context): Bitmap

        /** An [Icon] which takes a [drawable resource][iconRes]. */
        data class Resource(@DrawableRes val iconRes: Int) : Icon() {
            /**
             * Loads the [iconRes] resource from the specified [context],
             * or throws an [IllegalArgumentException] if no such resource was found.
             */
            override fun asBitmap(context: Context): Bitmap =
                requireNotNull(AppCompatResources.getDrawable(context, iconRes)) {
                    "Resource specified (${
                        context.resources.getResourceEntryName(iconRes)
                    }) does not exist"
                }.toBitmap()
        }

        /** An [Icon] which takes a [raw bitmap][Bitmap]. */
        // We use a Bitmap instead of a Drawable as it's parcelable
        data class Raw(val bitmap: Bitmap) : Icon() {
            /** Returns the [bitmap]. */
            override fun asBitmap(context: Context): Bitmap = bitmap
        }
    }

    companion object {
        /** Represents that the item should not have an ID */
        const val ID_NONE = 0
    }
}
