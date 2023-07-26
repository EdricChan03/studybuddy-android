package com.edricchan.studybuddy.ui.modules.task.adapter.itemdetailslookup

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.edricchan.studybuddy.ui.modules.task.adapter.TodosAdapter
import com.edricchan.studybuddy.ui.modules.task.adapter.itemdetails.TodoItemDetails

class TodoItemLookup(
    private val recyclerView: RecyclerView
) : ItemDetailsLookup<String>() {

    override fun getItemDetails(e: MotionEvent): TodoItemDetails? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        if (view != null) {
            val viewHolder = recyclerView.getChildViewHolder(view)
            if (viewHolder is TodosAdapter.Holder) {
                return viewHolder.itemDetails
            }
        }
        return null
    }
}
