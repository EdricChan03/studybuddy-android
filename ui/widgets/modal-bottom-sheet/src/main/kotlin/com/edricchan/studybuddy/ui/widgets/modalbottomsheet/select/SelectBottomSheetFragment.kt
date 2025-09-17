package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.annotation.VisibleForTesting
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.fragment.compose.content
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edricchan.studybuddy.ui.insets.enableEdgeToEdge
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.SelectBottomSheetFragment.Companion.newInstance
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetGroup
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetItem
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.selectedItems
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.vm.SelectBottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.parcelize.Parcelize

/**
 * Modal bottom sheet dialog which allows for a desired item or a list of items
 * to be chosen from a list of items.
 *
 * The [newInstance] companion object function should be used to instantiate an instance
 * of this class.
 */
class SelectBottomSheetFragment<Id : Any> : BottomSheetDialogFragment() {
    private val viewModel by viewModels<SelectBottomSheetViewModel<Id>>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = content {
        StudyBuddyTheme {
            Surface(
                color = BottomSheetDefaults.ContainerColor
            ) {
                val headerTitle by viewModel.headerTitle.collectAsStateWithLifecycle()
                val hideDragHandle by viewModel.hideDragHandle.collectAsStateWithLifecycle()
                val items by viewModel.items.collectAsStateWithLifecycle()

                SelectBottomSheetContent(
                    headerTitle = headerTitle,
                    itemsList = items,
                    hideDragHandle = hideDragHandle,
                    onItemClick = viewModel::toggleSelectedItem,
                    onCancelClick = ::onCancelClick,
                    onConfirmClick = ::onConfirmClick,
                    confirmEnabled = items.hasSelection
                )
            }
        }
    }

    private fun dismissWithResult(
        isCanceled: Boolean,
        selectedItems: Set<OptionBottomSheetItem<Id>>? = null
    ) {
        setFragmentResult(
            requestKey = RESULT_KEY,
            result = FragmentResult(
                isCanceled = isCanceled,
                selectedItems = selectedItems
            ).toBundle()
        )
        dismiss()
    }

    private fun onConfirmClick() {
        dismissWithResult(
            isCanceled = false,
            selectedItems = viewModel.items.value.selectedItems
        )
    }

    private fun onCancelClick() {
        dismissWithResult(
            isCanceled = true
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parentLayout =
            (dialog as BottomSheetDialog).findViewById<ViewGroup>(com.google.android.material.R.id.design_bottom_sheet)
        parentLayout?.apply {
            // Clip the background such that the corners are rounded
            outlineProvider = ViewOutlineProvider.BACKGROUND
            clipToOutline = true
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also {
            it.window?.enableEdgeToEdge()
        }
    }

    companion object {
        const val TAG_ITEMS_DATA = "select-sheet:itemsData"
        const val TAG_HEADER_TITLE = "select-sheet:headerTitle"
        const val TAG_HIDE_DRAG_HANDLE = "select-sheet:hideDragHandle"

        /**
         * Create a new instance of [SelectBottomSheetFragment] with the specified
         * arguments.
         * @param itemsData The items data to show.
         * @param headerTitle The bottom sheet's header title.
         * @param hideDragHandle Whether the bottom sheet's drag handle should be hidden.
         */
        fun <Id : Any> newInstance(
            itemsData: OptionBottomSheetGroup<*>,
            headerTitle: String,
            hideDragHandle: Boolean = false
        ): SelectBottomSheetFragment<Id> = SelectBottomSheetFragment<Id>().apply {
            arguments = getArguments(itemsData, headerTitle, hideDragHandle)
        }

        @VisibleForTesting
        fun <Id> getArguments(
            itemsData: OptionBottomSheetGroup<Id>,
            headerTitle: String,
            hideDragHandle: Boolean = false
        ) = bundleOf(
            TAG_ITEMS_DATA to itemsData,
            TAG_HEADER_TITLE to headerTitle,
            TAG_HIDE_DRAG_HANDLE to hideDragHandle
        )

        const val RESULT_KEY = "select-sheet:result"

        const val RESULT_DATA_KEY = "select-sheet:result-data"
    }

    /**
     * Data class used for this fragment's [setFragmentResult].
     * @property isCanceled Whether the bottom sheet was cancelled, i.e. the "Cancel"
     * button was pressed.
     * @property selectedItems The set of selected items, or a single item if
     * [OptionBottomSheetGroup.SingleSelect] was used.
     */
    @Parcelize
    data class FragmentResult<Id>(
        val isCanceled: Boolean,
        val selectedItems: Set<OptionBottomSheetItem<Id>>? = null
    ) : Parcelable {
        fun toBundle() = bundleOf(RESULT_DATA_KEY to this)

        companion object {
            @Suppress("UNCHECKED_CAST")
            fun <Id> fromBundle(bundle: Bundle): FragmentResult<Id>? =
                BundleCompat.getParcelable(
                    bundle,
                    RESULT_DATA_KEY,
                    FragmentResult::class.java
                ) as? FragmentResult<Id>
        }
    }

    /**
     * Gets the single selected item, or null if no item was selected.
     * @see Iterable.single
     */
    val <Id> FragmentResult<Id>.selectedItem: OptionBottomSheetItem<Id>?
        get() = selectedItems?.single()
}
