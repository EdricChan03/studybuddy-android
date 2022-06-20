package com.edricchan.studybuddy.ui.widget.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.edricchan.studybuddy.databinding.FragModalBottomSheetBinding
import com.edricchan.studybuddy.ui.widget.bottomsheet.dsl.ModalBottomSheetDSL
import com.edricchan.studybuddy.ui.widget.bottomsheet.dsl.items
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return FragModalBottomSheetBinding.inflate(
            layoutInflater, container, false
        ).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.run {
            headerTitle = getString(HEADER_TITLE_TAG)
        }

        binding.apply {
            bottomSheetHeader.isVisible = headerTitle != null
            bottomSheetHeaderTitleTextView.text = headerTitle

            bottomSheetRecyclerView.apply {
                adapter = ModalBottomSheetAdapter(requireContext(), items)
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(HEADER_TITLE_TAG, headerTitle)
        super.onSaveInstanceState(outState)
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

    /**
     * Overwrites the current list of items with the specified list of items
     * using DSL-like syntax.
     * Example:
     * ```kt
     * fragment.setItems {
     *     // Add an item
     *     item {
     *         title = "Hello world!"
     *         icon = R.drawable.ic_info_outline_24dp
     *     }
     *     // Add a list of items
     *     items({ title = "Hello item 2!" }, { title = "Another item!" })
     *     // Add a group
     *     group {
     *         // Provide group metadata
     *         id = 123
     *     }.apply {
     *         // Add items
     *         items({ title = "This is in a group!" }, { title = "Yet another item" })
     *     }
     *     // Or specify the items as a second argument
     *     group({
     *         id = 234
     *     }) {
     *         // Add items
     *         item { title = "Hello another group!" }
     *     }
     * }
     * ```
     * @param init The new list of items.
     */
    fun setItems(init: ModalBottomSheetDSL.() -> Unit) {
        this.items = items(init).toMutableList()
    }

    companion object {
        private const val HEADER_TITLE_TAG = "headerTitle"
    }
}
