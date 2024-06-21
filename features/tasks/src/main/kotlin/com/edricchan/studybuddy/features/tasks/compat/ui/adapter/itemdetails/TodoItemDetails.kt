package com.edricchan.studybuddy.features.tasks.compat.ui.adapter.itemdetails

import androidx.recyclerview.selection.ItemDetailsLookup

/**
 * Creates a new item detail
 *
 * @param adapterPosition The adapter position of this item
 * @param selectionKey    The selection key
 */
class TodoItemDetails(
    private val adapterPosition: Int,
    private val selectionKey: String?
) : ItemDetailsLookup.ItemDetails<String>() {
    override fun getPosition() = adapterPosition

    override fun getSelectionKey() = selectionKey
}
