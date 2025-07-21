package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * An item in a [OptionBottomSheetGroup].
 * @property id Unique ID for this item. This value should be of a class that
 * is [Parcelable].
 * @property title The item's human readable title.
 * @property icon An icon drawable resource to be displayed, if any.
 * @property enabled Whether this item is selectable.
 */
@Parcelize
data class OptionBottomSheetItem<Id>(
    val id: @RawValue Id,
    val title: String,
    @field:DrawableRes
    val icon: Int? = null,
    val enabled: Boolean = true
) : Parcelable
