package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.annotation.VisibleForTesting
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.fragment.compose.content
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.savedstate.savedState
import androidx.savedstate.serialization.decodeFromSavedState
import androidx.savedstate.serialization.encodeToSavedState
import com.edricchan.studybuddy.ui.insets.enableEdgeToEdge
import com.edricchan.studybuddy.ui.theming.common.AppearancePreferences
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetGroup
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetItem
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.selectedItems
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.vm.SelectBottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import javax.inject.Inject

/**
 * Modal bottom sheet dialog which allows for a desired item or a list of items
 * to be chosen from a list of items.
 *
 * The [newInstance] companion object function should be used to instantiate an instance
 * of this class.
 */
@AndroidEntryPoint
class SelectBottomSheetFragment : BottomSheetDialogFragment() {
    @Inject
    lateinit var appearancePreferences: AppearancePreferences

    private val viewModel by viewModels<SelectBottomSheetViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = content {
        StudyBuddyTheme(appearancePreferences = appearancePreferences) {
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
        selectedItems: Set<OptionBottomSheetItem>? = null
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
            (dialog as? BottomSheetDialog)?.findViewById<ViewGroup>(com.google.android.material.R.id.design_bottom_sheet)
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
        fun newInstance(
            itemsData: OptionBottomSheetGroup,
            headerTitle: String,
            hideDragHandle: Boolean = false
        ): SelectBottomSheetFragment = SelectBottomSheetFragment().apply {
            arguments = getArguments(itemsData, headerTitle, hideDragHandle)
        }

        @VisibleForTesting
        fun getArguments(
            itemsData: OptionBottomSheetGroup,
            headerTitle: String,
            hideDragHandle: Boolean = false
        ) = savedState {
            putSavedState(TAG_ITEMS_DATA, encodeToSavedState(itemsData))
            putString(TAG_HEADER_TITLE, headerTitle)
            putBoolean(TAG_HIDE_DRAG_HANDLE, hideDragHandle)
        }

        const val RESULT_KEY = "select-sheet:result"
    }

    /**
     * Data class used for this fragment's [setFragmentResult].
     * @property isCanceled Whether the bottom sheet was cancelled, i.e. the "Cancel"
     * button was pressed.
     * @property selectedItems The set of selected items, or a single item if
     * [OptionBottomSheetGroup.SingleSelect] was used.
     */
    @Serializable
    data class FragmentResult(
        val isCanceled: Boolean,
        val selectedItems: Set<OptionBottomSheetItem>? = null
    ) {
        fun toBundle(): androidx.savedstate.SavedState = encodeToSavedState(this)

        companion object {
            fun fromBundle(bundle: Bundle): FragmentResult? = decodeFromSavedState(
                savedState = bundle
            )
        }
    }

    /**
     * Gets the single selected item, or null if no item was selected.
     * @see Iterable.single
     */
    val FragmentResult.selectedItem: OptionBottomSheetItem?
        get() = selectedItems?.single()
}
