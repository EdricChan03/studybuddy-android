package com.edricchan.studybuddy.adapter.itemdetails

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup

/**
 * Creates a new item detail
 *
 * @param adapterPosition The adapter position of this item
 * @param selectionKey    The selection key
 */
class TaskItemDetails(
		private val adapterPosition: Int,
		private val selectionKey: String
) : ItemDetailsLookup.ItemDetails<String>() {


	override fun getPosition(): Int {
		return adapterPosition
	}

	override fun getSelectionKey(): String? {
		return selectionKey
	}

	override fun inSelectionHotspot(e: MotionEvent): Boolean {
		return true
	}
}
