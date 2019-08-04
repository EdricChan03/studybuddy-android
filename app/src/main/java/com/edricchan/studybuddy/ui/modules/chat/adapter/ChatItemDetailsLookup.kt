package com.edricchan.studybuddy.ui.modules.chat.adapter

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class ChatItemDetailsLookup(
		private val recyclerView: RecyclerView
) : ItemDetailsLookup<String>() {
	override fun getItemDetails(e: MotionEvent): ChatItemDetails? {
		val view = recyclerView.findChildViewUnder(e.x, e.y)
		if (view != null) {
			val viewHolder = recyclerView.getChildViewHolder(view)
			if (viewHolder is ChatsAdapter.ViewHolder) {
				return viewHolder.getItemDetails()
			}
		}
		return null
	}

	class ChatItemDetails(
			private val position: Int,
			private val selectionKey: String?
	) : ItemDetailsLookup.ItemDetails<String>() {
		override fun getPosition(): Int = position

		override fun getSelectionKey(): String? = selectionKey

	}
}