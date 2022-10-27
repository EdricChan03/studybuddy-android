package com.edricchan.studybuddy.ui.widget.iconpicker.adapter

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class IconPickerItemDetailsLookup(
    private val recyclerView: RecyclerView
) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): IconPickerItemDetails? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        if (view != null) {
            val viewHolder = recyclerView.getChildViewHolder(view)
            if (viewHolder is IconPickerAdapter.Holder) {
                return viewHolder.itemDetails
            }
        }
        return null
    }

    class IconPickerItemDetails(
        private val position: Int,
//			private val selectionKey: String?
        private val selectionKey: Long?
    ) : ItemDetails<Long>() {
        override fun getPosition(): Int = position

        override fun getSelectionKey(): Long? = selectionKey

    }
}
