package com.edricchan.studybuddy.ui.widget.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.edricchan.studybuddy.databinding.FragModalBottomSheetBinding
import com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces.ModalBottomSheetItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * A bottom sheet which can be used to show more options for a particular action
 */
class ModalBottomSheetFragment : BottomSheetDialogFragment() {

    /** The current list of items */
    var items: MutableList<ModalBottomSheetItem> = mutableListOf()

    /** The bottom sheet's title to be shown on top of the list of items */
    var headerTitle: String? = null

    private lateinit var binding: FragModalBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragModalBottomSheetBinding.inflate(
            layoutInflater,
            container,
            false
        ).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            headerTitle = savedInstanceState.getString(HEADER_TITLE_TAG)
        }

        if (headerTitle != null) {
            setHeaderViewVisibility(View.VISIBLE)
            setHeaderViewTitle(headerTitle)
        } else {
            setHeaderViewVisibility(View.GONE)
            setHeaderViewTitle(null)
        }

        binding.bottomSheetRecyclerView.apply {
            adapter = ModalBottomSheetAdapter(requireContext(), items)
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setHeaderViewVisibility(visibility: Int) {
        binding.bottomSheetHeader.apply {
            setVisibility(visibility)
        }
    }

    private fun setHeaderViewTitle(headerTitle: String?) {
        binding.bottomSheetHeaderTitleTextView.apply {
            text = headerTitle
        }
    }

    /**
     * Adds an item to the current list of items
     * @param item The item to add
     */
    fun addItem(item: ModalBottomSheetItem) {
        items += item
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

    companion object {
        private const val HEADER_TITLE_TAG = "headerTitle"
    }
}
