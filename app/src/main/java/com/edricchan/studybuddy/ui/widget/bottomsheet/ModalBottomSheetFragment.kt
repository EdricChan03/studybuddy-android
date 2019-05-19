package com.edricchan.studybuddy.ui.widget.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces.ModalBottomSheetGroup
import com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces.ModalBottomSheetItem
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * A bottom sheet which can be used to show more options for a particular action
 */
class ModalBottomSheetFragment : BottomSheetDialogFragment() {

	/** The current list of items */
	var items: MutableList<ModalBottomSheetItem> = mutableListOf()
	/** The current list of groups */
	var groups: MutableList<ModalBottomSheetGroup> = mutableListOf()
	/** The bottom sheet's title to be shown on top of the list of items */
	var headerTitle: String? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.frag_modal_bottom_sheet, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (savedInstanceState != null) {
			headerTitle = savedInstanceState.getString(HEADER_TITLE_TAG)
		}
		if (headerTitle != null) {
			setHeaderVisibility(view, View.VISIBLE)
			setHeaderTitle(view, headerTitle)
		} else {
			setHeaderVisibility(view, View.GONE)
			setHeaderTitle(view, null)
		}
		view.findViewById<RecyclerView>(R.id.bottomSheetRecyclerView).apply {
			Log.d(TAG, "Current list of items: $items")
			Log.d(TAG, "Current list of groups: $groups")
			adapter = ModalBottomSheetAdapter(requireContext(), items.toTypedArray(), groups.toTypedArray())
			layoutManager = LinearLayoutManager(requireContext())
			setHasFixedSize(false)
		}
	}

	private fun setHeaderVisibility(view: View, visibility: Int) {
		view.findViewById<LinearLayout>(R.id.bottomSheetHeader).apply {
			setVisibility(visibility)
		}
	}

	private fun setHeaderTitle(view: View, headerTitle: String?) {
		view.findViewById<TextView>(R.id.bottomSheetHeaderTitleTextView).apply {
			text = headerTitle
		}
	}

	/**
	 * Adds an item to the current list of items
	 * @param item The item to add
	 */
	fun addItem(item: ModalBottomSheetItem) {
		items.add(item)
	}

	/**
	 * Clears the current list of items
	 */
	fun clearItems() {
		items.clear()
	}

	/**
	 * Retrieves an item at the specified [index]
	 * @param index The index of the item to retrieve
	 * @return The item at that specific index, or [null] if no such item exists
	 */
	fun getItem(index: Int) = items.getOrNull(index)

	/**
	 * Overwrites the current list of items with the specified list of items
	 * @param items The new list of items
	 */
	fun setItems(items: Array<ModalBottomSheetItem>) {
		this.items = items.toMutableList()
	}

	/**
	 * Adds a group to the current list of groups
	 * @param group The group to add
	 */
	fun addGroup(group: ModalBottomSheetGroup) {
		groups.add(group)
	}

	/**
	 * Clears the current list of groups
	 */
	fun clearGroups() {
		groups.clear()
	}

	/**
	 * Retrieves a group at the specified [index]
	 * @param index The index of the group to retrieve
	 * @return The group at that specific index, or [null] if no such group exists
	 */
	fun getGroup(index: Int) = groups.getOrNull(index)

	/**
	 * Overwrites the current list of groups with the specified list of groups
	 * @param groups The new list of groups
	 */
	fun setGroups(groups: Array<ModalBottomSheetGroup>) {
		this.groups = groups.toMutableList()
	}
	companion object {
		private const val HEADER_TITLE_TAG = "headerTitle"
		private val TAG = SharedUtils.getTag(ModalBottomSheetFragment::class.java)
	}
}