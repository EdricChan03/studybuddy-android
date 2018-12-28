package com.edricchan.studybuddy.adapter.itemdetailslookup

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.edricchan.studybuddy.adapter.TasksAdapter
import com.edricchan.studybuddy.adapter.itemdetails.TaskItemDetails

class TaskItemLookup(
		private val recyclerView: RecyclerView
) : ItemDetailsLookup<String>() {

	override fun getItemDetails(e: MotionEvent): TaskItemDetails? {
		val view = recyclerView.findChildViewUnder(e.x, e.y)
		if (view != null) {
			val viewHolder = recyclerView.getChildViewHolder(view)
			if (viewHolder is TasksAdapter.Holder) {
				return viewHolder.itemDetails
			}
		}
		return null
	}
}
