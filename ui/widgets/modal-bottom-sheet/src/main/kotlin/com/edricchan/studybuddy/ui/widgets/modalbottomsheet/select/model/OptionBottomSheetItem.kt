package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model

import androidx.annotation.DrawableRes
import kotlinx.serialization.Serializable

/**
 * An item in a [OptionBottomSheetGroup].
 * @property id Unique ID for this item.
 * @property title The item's human-readable title.
 * @property icon An icon drawable resource to be displayed, if any.
 * @property enabled Whether this item is selectable.
 */
@Serializable
data class OptionBottomSheetItem(
    val id: String,
    val title: String,
    @field:DrawableRes
    val icon: Int? = null,
    val enabled: Boolean = true
)

/**
 * Retrieves the [id] field of the receiver [OptionBottomSheetItem]
 * as its serialised [Id] equivalent via [IdSerializer.deserialize], or `null`
 * if the [id] field could not be parsed.
 */
context(idSerializer: IdSerializer<Id>)
fun <Id : Any> OptionBottomSheetItem.serializedIdOrNull(): Id? = idSerializer.deserialize(id)
